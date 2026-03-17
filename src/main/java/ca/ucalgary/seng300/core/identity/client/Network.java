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

    private static void AESGenerateKey(KeyStore ks) {

    }

    public static byte[] encrypt(String plaintext) {

    }

    public static String decrypt(byte[] cipherText) {

    }


}