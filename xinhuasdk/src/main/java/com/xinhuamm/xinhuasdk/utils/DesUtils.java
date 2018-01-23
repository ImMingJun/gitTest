package com.xinhuamm.xinhuasdk.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DesUtils {
	public static String DES_KEY = "12345678";
    private static byte[] iv = {1,2,3,4,5,6,7,8};  
    public static String encryptDES(String encryptString) {
    	try{
	        IvParameterSpec zeroIv = new IvParameterSpec(iv);
	        SecretKeySpec key = new SecretKeySpec(DES_KEY.getBytes(), "DES");
	        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());  
	        return Base64Utils.encode(encryptedData);
    	}catch(Exception e){
    		return encryptString;
    	}
       
    }



    public static String decryptDES(String decryptString) throws Exception {
        byte[] byteMi = new Base64Utils().decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
//      IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);   
        SecretKeySpec key = new SecretKeySpec(DES_KEY.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte decryptedData[] = cipher.doFinal(byteMi);  
       
        return new String(decryptedData);
    }


    public static String encrypt(String decryptString, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        return Base64Utils.encode(cipher.doFinal(decryptString.getBytes("UTF-8")));
    }

    //解密算法在V400的时候，去掉了 padding属性
    public static String decrypt(String message, String key){

        try{
            byte[] bytesrc = Base64Utils.decode(message);//convertHexString(message);
            //传入参数 old: DES/CBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance("DES");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            byte[] retByte = cipher.doFinal(bytesrc);
            return new String(retByte);
        }catch (Exception e){

        }

        return null;
    }


}  