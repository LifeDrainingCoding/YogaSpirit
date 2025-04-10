package com.pracktic.yogaspirit.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

public class KeyGen {

    public static ArrayList<String> generateKeys(int amount){
        ArrayList<String> keys = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            keys.add(generateKey());
        }
        return keys;
    }

    public static String generateKey(){
        KeyGenerator generator = null;
        try {
            generator = KeyGenerator.getInstance("AES");
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        generator.init(256);
        SecretKey secretKey = generator.generateKey();
        String string = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        string = string.replaceAll("[.$#\\[\\]/\\\\]", "");


        return  string;
    }
}
