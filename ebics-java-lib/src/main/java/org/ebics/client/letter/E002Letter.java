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

package org.ebics.client.letter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;
import org.ebics.client.exception.EbicsException;
import org.ebics.client.user.base.EbicsUser;


/**
 * The <code>E002Letter</code> is the initialization letter
 * for the encryption certificate.
 *
 * @author Hachani
 *
 */
public class E002Letter extends AbstractInitLetter {

  /**
   * Constructs a new <code>E002Letter</code>
   * @param locale the application locale
   */
  public E002Letter(Locale locale) {
    super(locale);
  }

    @Override
    public void create(EbicsUser user) throws GeneralSecurityException, IOException, EbicsException {
        if (user.getPartner().getBank().getUseCertificate()) {
            build(user.getPartner().getBank().getHostId(),
                    user.getPartner().getBank().getName(),
                    user.getUserInfo().getUserId(),
                    user.getUserInfo().getName(),
                    user.getPartner().getPartnerId(),
                    getString("HIALetter.e002.version", BUNDLE_NAME, locale),
                    getString("HIALetter.e002.certificate", BUNDLE_NAME, locale),
                    Base64.encodeBase64(user.getE002CertificateBytes(), true),
                    getString("HIALetter.e002.digest", BUNDLE_NAME, locale),
                    getHash(user.getE002CertificateBytes()));
        } else {
            build(user.getPartner().getBank().getHostId(),
                    user.getPartner().getBank().getName(),
                    user.getUserInfo().getUserId(),
                    user.getUserInfo().getName(),
                    user.getPartner().getPartnerId(),
                    getString("HIALetter.e002.version", BUNDLE_NAME, locale),
                    getString("HIALetter.e002.certificate", BUNDLE_NAME, locale),
                    null,
                    getString("HIALetter.e002.digest", BUNDLE_NAME, locale),
                    getHash(user.getE002PublicKey()));
        }
    }

  @Override
  public String getTitle() {
    return getString("HIALetter.e002.title", BUNDLE_NAME, locale);
  }

  @Override
  public String getName() {
    return getString("HIALetter.e002.name", BUNDLE_NAME, locale) + ".txt";
  }
}
