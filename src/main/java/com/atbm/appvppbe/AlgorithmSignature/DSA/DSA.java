package com.atbm.appvppbe.AlgorithmSignature.DSA;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class DSA {
    public KeyPair createSignature() throws Exception {
        // create sign
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN"); // key DSA
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
        return keyGen.generateKeyPair();
    }

    public String process(String text) throws Exception {
        KeyPair pair = createSignature();
        PublicKey pub = pair.getPublic();
        PrivateKey priv = pair.getPrivate();

        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
        dsa.initSign(priv);

        // Start signature
        byte[] data = text.getBytes(StandardCharsets.UTF_8);
        dsa.update(data); // update
        byte[] realSig = dsa.sign();
        return Base64.getEncoder().encodeToString(realSig);
    }

    public static void main(String[] args) throws Exception {
        String text = "orderId=1,total=500000,userId=10";
        DSA dsa = new DSA();
        System.out.println(dsa.process(text));
    }
}
