/*
 * Copyright (c) 1990-2012 kopiLeft Development SARL, Bizerte, Tunisia
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * $Id$
 */

package org.ebics.client.xml.h004;

import org.ebics.client.exception.EbicsException;
import org.ebics.client.interfaces.ContentFactory;
import org.ebics.client.io.Splitter;
import org.ebics.client.order.EbicsOrderType;
import org.ebics.client.session.EbicsSession;
import org.ebics.client.utils.Utils;
import org.ebics.schema.h004.DataEncryptionInfoType.EncryptionPubKeyDigest;
import org.ebics.schema.h004.*;
import org.ebics.schema.h004.DataTransferRequestType.DataEncryptionInfo;
import org.ebics.schema.h004.DataTransferRequestType.SignatureData;
import org.ebics.schema.h004.EbicsRequestDocument.EbicsRequest;
import org.ebics.schema.h004.EbicsRequestDocument.EbicsRequest.Body;
import org.ebics.schema.h004.EbicsRequestDocument.EbicsRequest.Header;
import org.ebics.schema.h004.ParameterDocument.Parameter;
import org.ebics.schema.h004.ParameterDocument.Parameter.Value;
import org.ebics.schema.h004.StaticHeaderOrderDetailsType.OrderType;
import org.ebics.schema.h004.StaticHeaderType.BankPubKeyDigests;
import org.ebics.schema.h004.StaticHeaderType.BankPubKeyDigests.Authentication;
import org.ebics.schema.h004.StaticHeaderType.BankPubKeyDigests.Encryption;
import org.ebics.schema.h004.StaticHeaderType.Product;

import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * The <code>UInitializationRequestElement</code> is the common initialization
 * element for all ebics file uploads.
 *
 * @author Hachani
 *
 */
public class UploadInitializationRequestElement extends InitializationRequestElement {


/**
   * Constructs a new <code>UInitializationRequestElement</code> for uploads initializations.
   * @param session the current ebics session.
   * @param orderType the upload order type
   * @param userData the user data to be uploaded
   * @throws EbicsException
   */
  public UploadInitializationRequestElement(EbicsSession session,
                                            EbicsOrderType orderType, OrderAttributeType.Enum orderAttribute,
                                            byte[] userData)
    throws EbicsException
  {
    super(session, orderType, generateName(orderType));
    this.userData = userData;
    keySpec = new SecretKeySpec(nonce, "EAS");
    splitter = new Splitter(userData);
    this.orderAttribute = orderAttribute;
  }

  @Override
  public void buildInitialization() throws EbicsException {
    EbicsRequest			request;
    Header 				header;
    Body				body;
    MutableHeaderType 			mutable;
    StaticHeaderType 			xstatic;
    Product 				product;
    BankPubKeyDigests 			bankPubKeyDigests;
    Authentication 			authentication;
    Encryption 				encryption;
    DataTransferRequestType 		dataTransfer;
    DataEncryptionInfo 			dataEncryptionInfo;
    SignatureData 			signatureData;
    EncryptionPubKeyDigest 		encryptionPubKeyDigest;
    OrderType 				orderType;
    FileFormatType 			fileFormat;

    userSignature = new UserSignature(session.getUser(),
				      generateName("UserSignature"),
	                              session.getConfiguration().getSignatureVersion(),
	                              userData);
    userSignature.build();
    userSignature.validate();

    splitter.readInput(session.getConfiguration().isCompressionEnabled(), keySpec);

    mutable = EbicsXmlFactory.createMutableHeaderType("Initialisation", null);
    product = EbicsXmlFactory.createProduct(session.getProduct().getLanguage(), session.getProduct().getName());
    authentication = EbicsXmlFactory.createAuthentication(session.getConfiguration().getAuthenticationVersion(),
	                                                  "http://www.w3.org/2001/04/xmlenc#sha256",
	                                                  decodeHex(session.getUser().getPartner().getBank().getX002Digest()));
    encryption = EbicsXmlFactory.createEncryption(session.getConfiguration().getEncryptionVersion(),
	                                          "http://www.w3.org/2001/04/xmlenc#sha256",
	                                          decodeHex(session.getUser().getPartner().getBank().getE002Digest()));
    bankPubKeyDigests = EbicsXmlFactory.createBankPubKeyDigests(authentication, encryption);
    orderType = EbicsXmlFactory.createOrderType(type.toString());
    fileFormat = EbicsXmlFactory.createFileFormatType(session.getConfiguration().getLocale().getCountry().toUpperCase(),
	                                              session.getSessionParam("FORMAT"));

    String nextOrderId = session.getUser().getPartner().nextOrderId();

    StaticHeaderOrderDetailsType orderDetails;
    if (type == EbicsOrderType.FUL) {
        FULOrderParamsType fULOrderParams = EbicsXmlFactory.createFULOrderParamsType(fileFormat);

        List<Parameter> parameters = new ArrayList<>();
        if (Boolean.valueOf(session.getSessionParam("TEST")).booleanValue()) {
          Value value = EbicsXmlFactory.createValue("String", "TRUE");
          Parameter parameter = EbicsXmlFactory.createParameter("TEST", value);
          parameters.add(parameter);
        }

        if (Boolean.valueOf(session.getSessionParam("EBCDIC")).booleanValue()) {
          Value value = EbicsXmlFactory.createValue("String", "TRUE");
          Parameter parameter = EbicsXmlFactory.createParameter("EBCDIC", value);
          parameters.add(parameter);
        }

        if (parameters.size() > 0) {
          fULOrderParams.setParameterArray(parameters.toArray(new Parameter[parameters.size()]));
        }
        orderDetails = EbicsXmlFactory.createStaticHeaderOrderDetailsType(nextOrderId,
            orderAttribute,
            orderType,
            fULOrderParams);
    } else {
        StandardOrderParamsType standardOrderParamsType = EbicsXmlFactory.createStandardOrderParamsType();
        orderDetails = EbicsXmlFactory.createStaticHeaderOrderDetailsType(nextOrderId,
            orderAttribute,
            orderType,
            standardOrderParamsType);
    }

    xstatic = EbicsXmlFactory.createStaticHeaderType(session.getBankID(),
	                                             nonce,
	                                             splitter.getSegmentNumber(),
	                                             session.getUser().getPartner().getPartnerId(),
	                                             product,
	                                             session.getUser().getSecurityMedium(),
	                                             session.getUser().getUserId(),
	                                             Calendar.getInstance(),
	                                             orderDetails,
	                                             bankPubKeyDigests);
    header = EbicsXmlFactory.createEbicsRequestHeader(true, mutable, xstatic);
    encryptionPubKeyDigest = EbicsXmlFactory.createEncryptionPubKeyDigest(session.getConfiguration().getEncryptionVersion(),
								          "http://www.w3.org/2001/04/xmlenc#sha256",
								          decodeHex(session.getUser().getPartner().getBank().getE002Digest()));
    signatureData = EbicsXmlFactory.createSignatureData(true, Utils.encrypt(Utils.zip(userSignature.prettyPrint()), keySpec));
    dataEncryptionInfo = EbicsXmlFactory.createDataEncryptionInfo(true,
	                                                          encryptionPubKeyDigest,
	                                                          generateTransactionKey());
    dataTransfer = EbicsXmlFactory.createDataTransferRequestType(dataEncryptionInfo, signatureData);
    body = EbicsXmlFactory.createEbicsRequestBody(dataTransfer);
    request = EbicsXmlFactory.createEbicsRequest(session.getConfiguration().getRevision(),
	                                         session.getConfiguration().getVersion(),
	                                         header,
	                                         body);
    document = EbicsXmlFactory.createEbicsRequestDocument(request);
  }

  @Override
  public byte[] toByteArray() {
    setSaveSuggestedPrefixes("http://www.ebics.org/h004", "");

    return super.toByteArray();
  }

  /**
   * Returns the user signature data.
   * @return the user signature data.
   */
  public UserSignature getUserSignature() {
    return userSignature;
  }

  /**
   * Returns the content of a given segment.
   * @param segment the segment number
   * @return the content of the given segment
   */
  public ContentFactory getContent(int segment) {
    return splitter.getContent(segment);
  }

  /**
   * Returns the total segment number.
   * @return the total segment number.
   */
  public int getSegmentNumber() {
    return splitter.getSegmentNumber();
  }

  // --------------------------------------------------------------------
  // DATA MEMBERS
  // --------------------------------------------------------------------

  private final OrderAttributeType.Enum orderAttribute;
  private byte[]			userData;
  private UserSignature userSignature;
  private SecretKeySpec			keySpec;
  private Splitter			splitter;
  private static final long 		serialVersionUID = -8083183483311283608L;
}
