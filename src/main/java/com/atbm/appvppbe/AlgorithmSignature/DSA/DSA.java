package com.atbm.appvppbe.AlgorithmSignature.DSA;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DSA {
    public KeyPair createSignature() throws Exception {
        // create sign
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN"); // key DSA
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
        return keyGen.generateKeyPair();
    }

    public String exportKey(KeyPair keyPair, boolean isPublicKey) {
        byte[] export;
        if (isPublicKey) {
            export = keyPair.getPublic().getEncoded();
        } else {
            export = keyPair.getPrivate().getEncoded();
        }
        return Base64.getEncoder().encodeToString(export);
    }

    // String => Private Key
    private PrivateKey importPrivateKey(String keyText) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyText);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");

        return keyFactory.generatePrivate(spec);
    }

    private PublicKey importPublicKey(String keyText) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(keyText);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");

        return keyFactory.generatePublic(spec);
    }

    // Sign
    public String sign(String text, String privateKey) throws Exception {
        PrivateKey pri = importPrivateKey(privateKey);

        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
        dsa.initSign(pri);

        // Start signature
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        dsa.update(data); // update
        byte[] realSig = dsa.sign();
        return Base64.getEncoder().encodeToString(realSig);
    }

    // Verify
    public boolean verify(String publicKeyText, String dataText, String signatureText) throws Exception {
        PublicKey publicKey = importPublicKey(publicKeyText);

        Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
        signature.initVerify(publicKey);
        signature.update(dataText.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signatureText);

        return signature.verify(signatureBytes);
    }


    public static void main(String[] args) throws Exception {
        String text = "orderId=1,total=500000,userId=10";
        DSA dsa = new DSA();
//        System.out.println(dsa.process(text));
    }
}
