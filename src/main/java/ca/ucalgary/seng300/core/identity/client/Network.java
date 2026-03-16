package ca.ucalgary.seng300.core.identity.client;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class Network {
    private static byte[] sharedKey = null;
    private static SecretKey AESKey;
    private static SecureRandom sRan;

    public static byte[] generateSharedKey(byte[] serverPubKeyBytes) throws Exception {

    }

    public static void AESKeyInitial(){

    }

    private static void AESGenerateKey(KeyStore ks) {

    }

    public static byte[] encrypt(String plaintext) {

    }

    public static String decrypt(byte[] cipherText) {

    }


}