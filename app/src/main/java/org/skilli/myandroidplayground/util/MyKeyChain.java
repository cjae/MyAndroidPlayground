package org.skilli.myandroidplayground.util;

import android.content.Context;
import android.util.Base64;

import com.facebook.android.crypto.keychain.SecureRandomFix;
import com.facebook.crypto.cipher.NativeGCMCipher;
import com.facebook.crypto.exception.KeyChainException;
import com.facebook.crypto.keychain.KeyChain;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Created by Jedidiah on 24/08/2016.
 */
public class MyKeyChain implements KeyChain {


    private final SecureRandom mSecureRandom;

    protected byte[] mCipherKey;
    protected boolean mSetCipherKey;
    private String publicKey;

    private static final SecureRandomFix sSecureRandomFix = new SecureRandomFix();


    public MyKeyChain(Context context, String publicBase64Key) {
        mSecureRandom = new SecureRandom();
        this.publicKey = publicBase64Key;
    }

    @Override
    public synchronized byte[] getCipherKey() throws KeyChainException {
        if (!mSetCipherKey) {
            mCipherKey = decodeKey(publicKey);
        }
        mSetCipherKey = true;
        return mCipherKey;
    }

    @Override
    public byte[] getMacKey() throws KeyChainException {
        return null;
    }

    @Override
    public byte[] getNewIV() throws KeyChainException {
        sSecureRandomFix.tryApplyFixes();
        byte[] iv = new byte[NativeGCMCipher.IV_LENGTH];
        mSecureRandom.nextBytes(iv);
        return iv;
    }

    @Override
    public synchronized void destroyKeys() {
        mSetCipherKey = false;
        Arrays.fill(mCipherKey, (byte) 0);
        mCipherKey = null;
    }



    private byte[] decodeKey(String keyString) {
        if (keyString == null) {
            return null;
        }
        return Base64.decode(keyString, Base64.DEFAULT);
    }
}