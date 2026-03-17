package ca.ucalgary.seng300.core.identity.client;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;


public class Network {
    private static byte[] sharedKey = null;
    private static SecretKey AESKey;
    private static SecureRandom sRan;

    /** Method for creating the shared secret/key
     *
     * @param serverPubKeyBytes
     * @return
     * @throws Exception
     */
    public static byte[] generateSharedKey(byte[] serverPubKeyBytes) throws Exception {

        // build public key from serverPubKeyBytes
        KeyFactory clientKeyFactory = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(serverPubKeyBytes);
        PublicKey serverPublicKey = clientKeyFactory.generatePublic(x509KeySpec);

        // ensuring same dh parameters are used on client and server side (g and m)
        DHParameterSpec dhServerParameterSpec = ((DHPublicKey) serverPublicKey).getParams();

        // making the public/private key pair using server parameters
        KeyPairGenerator clientKeyPairGen = KeyPairGenerator.getInstance("DH");
        clientKeyPairGen.initialize(dhServerParameterSpec);
        KeyPair clientKeyPair = clientKeyPairGen.generateKeyPair();

        // creating key agreement object
        KeyAgreement clientKeyAgreement = KeyAgreement.getInstance("DH");
        clientKeyAgreement.init(clientKeyPair.getPrivate());

        // combining server's key with client's, generating shared key
        clientKeyAgreement.doPhase(serverPublicKey, true);
        sharedKey = clientKeyAgreement.generateSecret();

        return clientKeyPair.getPublic().getEncoded();
    }

    /** Ensuring program has everything it needs to encrypt/decrypt, preparation
     *
     */
    public static void AESKeyInitial(){

        try {
            // check if previous key store exists
            File file = new File("./passwords.jks");
            KeyStore ks = KeyStore.getInstance("pkcs12");
            char[] pwd = "password".toCharArray();

            // make new one if doesn't exist
            if (!file.exists()) {
                ks.load(null, pwd);
                AESGenerateKey(ks);
            } else {
                // trying to load the file
                try (FileInputStream fis = new FileInputStream(file)) {
                    ks.load(fis,pwd);
                }
                AESKey = (SecretKey) ks.getKey("key", pwd);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // create secure RNG
        sRan = new SecureRandom();
    }

    /** Turning the shared key into a usable AES encryption key
     *
     * @param ks
     * @throws Exception
     */
    private static void AESGenerateKey(KeyStore ks) throws Exception {

        // using shared key to generate hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(sharedKey);

        // use first 16 bytes of hash to make key
        byte[] shortHash = new byte[16];
        System.arraycopy(hash, 0, shortHash, 0, 16);
        AESKey = new SecretKeySpec(shortHash, "AES");

        // convert key to secret key
        KeyStore.SecretKeyEntry secKey = new KeyStore.SecretKeyEntry(AESKey);
        KeyStore.ProtectionParameter proPara = new KeyStore.PasswordProtection("password".toCharArray());

        // adding secret key to key store
        ks.setEntry("key", secKey, proPara);

    }

    /** encryption method
     *  takes some string and returns the encrypted byte array
     *
     * @param plainText
     * @return
     */
    public static byte[] encrypt(String plainText) throws Exception {

        // makes 12 byte long nonce (random value) as per NIST standards
        // important a new one is generated for each encryption
        byte[] nonce = new byte[12];
        sRan.nextBytes(nonce);
        GCMParameterSpec gcmParamSpec = new GCMParameterSpec(128, nonce);

        // creating cipher and setting up encryption algorithm
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, AESKey, gcmParamSpec);

        // encrypting text
        byte[] cipherTextTag = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // combining encrypted text with nonce, copying to beginning of message
        byte[] message = new byte[nonce.length + cipherTextTag.length];
        System.arraycopy(nonce, 0, message, 0, 12);
        System.arraycopy(cipherTextTag, 0, message, 12, cipherTextTag.length);

        return message;
    }

    /** decryption method
     *  takes an encrypted byte array and turns it into a string
     *
     * @param cipherText
     * @return
     */
    public static String decrypt(byte[] cipherText) throws Exception {

        // extracting nonce from cipher text (first 12 bytes)
        byte[] nonce = new byte[12];
        System.arraycopy(cipherText, 0, nonce, 0, 12);

        // copying everything after nonce (encrypted data)
        byte[] cipherTextMessage = new byte[cipherText.length - 12];
        System.arraycopy(cipherText, 12, cipherTextMessage, 0, cipherText.length - 12);

        // recreating gcm parameters, ensuring decryption uses 'same settings' as encryption
        GCMParameterSpec gcmParamSpec = new GCMParameterSpec(128, nonce);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        // setting up for decyption
        cipher.init(Cipher.DECRYPT_MODE, AESKey, gcmParamSpec);

        // decrypting
        byte[] plainText = cipher.doFinal(cipherTextMessage);

        return new String(plainText, StandardCharsets.UTF_8);
    }

}