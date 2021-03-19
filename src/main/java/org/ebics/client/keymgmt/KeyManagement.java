package org.ebics.client.keymgmt;

import org.ebics.client.exception.EbicsException;
import org.ebics.client.session.EbicsSession;

import java.io.IOException;
import java.security.GeneralSecurityException;

public abstract class KeyManagement {
    /**
     * Constructs a new <code>KeyManagement</code> instance
     * with a given ebics session
     * @param session the ebics session
     */
    public KeyManagement(EbicsSession session) {
        this.session = session;
    }

    /**
     * Sends the user's signature key (A005) to the bank.
     * After successful operation the user is in state "initialized".
     * @param orderId the order ID. Let it null to generate a random one.
     * @throws EbicsException server generated error message
     * @throws IOException communication error
     */
    public abstract void sendINI(String orderId) throws EbicsException, IOException;

    /**
     * Sends the public part of the protocol keys to the bank.
     * @param orderId the order ID. Let it null to generate a random one.
     * @throws IOException communication error
     * @throws EbicsException server generated error message
     */
    public abstract void sendHIA(String orderId) throws IOException, EbicsException;

    /**
     * Sends encryption and authentication keys to the bank.
     * This order is only allowed for a new user at the bank side that has been created by copying the A005 key.
     * The keys will be activated immediately after successful completion of the transfer.
     * @throws IOException communication error
     * @throws GeneralSecurityException data decryption error
     * @throws EbicsException server generated error message
     */
    public abstract void sendHPB() throws IOException, GeneralSecurityException, EbicsException;

    /**
     * Sends the SPR order to the bank.
     * After that you have to start over with sending INI and HIA.
     * @throws IOException Communication exception
     * @throws EbicsException Error message generated by the bank.
     */
    public abstract void lockAccess() throws IOException, EbicsException;

    // --------------------------------------------------------------------
    // DATA MEMBERS
    // --------------------------------------------------------------------

    protected EbicsSession 				session;
}
