package com.startlinesoft.icore.solidario.android.ais.utilidades;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class ICoreEncripccion {

    /**
     * Retorna los parametros con los cuales se debe crear la llave secreta
     *
     * @return KeyGenParameterSpec
     */
    private static KeyGenParameterSpec generarParametros() {
        int propositos = KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT;
        return new KeyGenParameterSpec
                .Builder(ICoreEncripccion.getAlias(), propositos)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setUserAuthenticationRequired(true)
                .setInvalidatedByBiometricEnrollment(true)
                .build();
    }

    /**
     * Genera una llave secreta
     */
    public static void generarLlaveSecreta() {
        KeyGenParameterSpec keyGenParameterSpec = ICoreEncripccion.generarParametros();
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(keyGenParameterSpec);
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
        } catch (InvalidAlgorithmParameterException e) {
        }
    }

    /**
     * Obtiene la llave secreta desde el repositorio
     *
     * @return
     */
    public static SecretKey getLlaveSecreta() {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            return (SecretKey) keyStore.getKey(ICoreEncripccion.getAlias(), null);
        } catch (KeyStoreException e) {
        } catch (CertificateException e) {
        } catch (UnrecoverableKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (IOException e) {
        }
        return null;
    }

    public static Cipher getCipher() {
        String props = String.format(
                "%s/%s/%s",
                KeyProperties.KEY_ALGORITHM_AES,
                KeyProperties.BLOCK_MODE_CBC,
                KeyProperties.ENCRYPTION_PADDING_PKCS7
        );

        try {
            return Cipher.getInstance(props);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
        }
        return null;
    }

    /**
     * Retorna verdadero o falso si el keystore contiene o no el alias de la llave secreta
     *
     * @return
     */
    private static boolean hasSecretKey() {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            if (keyStore.isKeyEntry(ICoreEncripccion.getAlias())) {
                return true;
            }
            return false;
        } catch (KeyStoreException e) {
            return false;
        } catch (CertificateException e) {
            return false;
        } catch (NoSuchAlgorithmException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean llavePrivadaActiva() {
        SecretKey secretKey = ICoreEncripccion.getLlaveSecreta();
        Cipher cipher = ICoreEncripccion.getCipher();
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return true;
        } catch (InvalidKeyException e) {
            return false;
        }
    }

    public static boolean llaveSecretaActiva() {
        return ICoreEncripccion.hasSecretKey() && ICoreEncripccion.llavePrivadaActiva();
    }

    /**
     * Retorna el alias del keystore
     *
     * @return alias del keystore
     */
    private static String getAlias() {
        return "I-Core";
    }

}
