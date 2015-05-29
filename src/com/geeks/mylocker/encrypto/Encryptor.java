package com.geeks.mylocker.encrypto;

import javax.crypto.SecretKey;

import org.nick.androidpbe.Crypto;

import android.util.Log;


public abstract class Encryptor {
	
	protected final String TAG = getClass().getSimpleName();
	
	public static final int PADDING_ENC_IDX = 0;
	public static final int SHA1PRNG_ENC_IDX = 1;
	public static final int PBKDF2_ENC_IDX = 2;
	public static final int PKCS12_ENC_IDX = 3;
	
    SecretKey key;

    abstract SecretKey deriveKey(String passpword, byte[] salt);

    public abstract String encrypt(String plaintext, String password);

    public abstract String decrypt(String ciphertext, String password);

    String getRawKey() {
        if (key == null) {
            return null;
        }

        return Crypto.toHex(key.getEncoded());
    }

	private final static Encryptor PADDING_ENCRYPTOR = new Encryptor() {
	
	    @Override
	    public SecretKey deriveKey(String password, byte[] salt) {
	        return Crypto.deriveKeyPad(password);
	    }
	
	    @Override
	    public String encrypt(String plaintext, String password) {
	        key = deriveKey(password, null);
	        Log.d(TAG, "Generated key: " + getRawKey());
	
	        return Crypto.encrypt(plaintext, key, null);
	    }
	
	    @Override
	    public String decrypt(String ciphertext, String password) {
	        SecretKey key = deriveKey(password, null);
	
	        return Crypto.decryptNoSalt(ciphertext, key);
	    }
	};
	
	private final static Encryptor SHA1PRNG_ENCRYPTOR = new Encryptor() {
	
	    @Override
	    public SecretKey deriveKey(String password, byte[] salt) {
	        return Crypto.deriveKeySha1prng(password);
	    }
	
	    @Override
	    public String encrypt(String plaintext, String password) {
	        key = deriveKey(password, null);
	        Log.d(TAG, "Generated key: " + getRawKey());
	
	        return Crypto.encrypt(plaintext, key, null);
	    }
	
	    @Override
	    public String decrypt(String ciphertext, String password) {
	        SecretKey key = deriveKey(password, null);
	
	        return Crypto.decryptNoSalt(ciphertext, key);
	    }
	};
	
	private final static Encryptor PKCS12_ENCRYPTOR = new Encryptor() {
	
	    @Override
	    public SecretKey deriveKey(String password, byte[] salt) {
	        return Crypto.deriveKeyPkcs12(salt, password);
	    }
	
	    @Override
	    public String encrypt(String plaintext, String password) {
	        byte[] salt = Crypto.generateSalt();
	        key = deriveKey(password, salt);
	        Log.d(TAG, "Generated key: " + getRawKey());
	
	        return Crypto.encryptPkcs12(plaintext, key, salt);
	    }
	
	    @Override
	    public String decrypt(String ciphertext, String password) {
	        return Crypto.decryptPkcs12(ciphertext, password);
	    }
	};
	
	private final static Encryptor PBKDF2_ENCRYPTOR = new Encryptor() {
	
	    @Override
	    public SecretKey deriveKey(String password, byte[] salt) {
	        return Crypto.deriveKeyPbkdf2(salt, password);
	    }
	
	    @Override
	    public String encrypt(String plaintext, String password) {
	        byte[] salt = Crypto.generateSalt();
	        key = deriveKey(password, salt);
	        Log.d(TAG, "Generated key: " + getRawKey());
	
	        return Crypto.encrypt(plaintext, key, salt);
	    }
	
	    @Override
	    public String decrypt(String ciphertext, String password) {
	        return Crypto.decryptPbkdf2(ciphertext, password);
	    }
	};
	
	public static Encryptor select(int pos) {
        
		Encryptor encryptor;
		
        switch (pos) {
	        case PADDING_ENC_IDX:
	            encryptor = PADDING_ENCRYPTOR;
	            break;
	        case SHA1PRNG_ENC_IDX:
	            encryptor = SHA1PRNG_ENCRYPTOR;
	            break;
	        case PBKDF2_ENC_IDX:
	            encryptor = PBKDF2_ENCRYPTOR;
	            break;
	        case PKCS12_ENC_IDX:
	            encryptor = PKCS12_ENCRYPTOR;
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid option selected");
        }
        
        return encryptor;
    }
}