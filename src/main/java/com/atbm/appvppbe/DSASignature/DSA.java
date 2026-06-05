package com.atbm.appvppbe.DSASignature;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;

public class DSA {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN"); // key DSA
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
        KeyPair pair = keyGen.generateKeyPair();
        PublicKey pub = pair.getPublic();
        PrivateKey priv = pair.getPrivate();

        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
        dsa.initSign(priv);
        FileInputStream fis = new FileInputStream("file");
        BufferedInputStream bufin = new BufferedInputStream(fis);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bufin.read(buffer)) != -1) dsa.update(buffer, 0, len);
        bufin.close();
        byte[] realSig = dsa.sign();

        // save signature
        FileOutputStream sigFos = new FileOutputStream("fileout");
        sigFos.write(realSig);
        sigFos.close();

        // save pub key
        byte[] key = pub.getEncoded();
        FileOutputStream keyFos = new FileOutputStream("filepubkey");
        keyFos.write(key);
        keyFos.close();
    }
}
