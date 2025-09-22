import java.util.*;

import javax.crypto.spec.SecretKeySpec;

import java.io.*;

import java.security.SecureRandom;

import javax.crypto.*;


public class PasswordManager {
    private static final String File_Name="vault.dat";
    private static final String ALGORITHM="AES";
    private static SecretKey secretKey;


    private static Map<String,String> credentials=new HashMap<>();

    //Generate AES key from master password
    private static void setMasterPassWord(String masterPassWord) throws Exception {
        byte[] keybytes=Array.copyOf(masterPassWord.getBytes(),16);

        secretKey=new SecretKeySpec(keyBytes,ALGORITHM);
    }

    //For Encrypting the data
    private static byte[] encrypt(String data) throws Exception {
        Cipher cipher=Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        return cipher.doFinal(data.getBytes()); 
    }

    //For decrypting the data
    private static String decrypt(byte[] data) throws Exception {
        Cipher cipher=Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,secretKey);
        return new String(cipher.doFinal(data));
    }

    //Saving the credentials to file
    private static void saveToFile() throws Exception {
        try (FileOutputStream fos=new FileOutputStream(File_Name)) {
            StringBuilder sb=new StringBuilder();
            for (Map.Entry<String,String> entry :Credentials.entrySet()) {
                sb.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
            }
            byte[]=encrypted=encrypt(sb.toString());
            fos.write(encrypted);
        }
    }

    public static void main(String args[]) throws Exception {
        Scanner sc=new Scanner(System.in);
        System.out.print("Set/Enter Master Password:");

        String master=sc.nextLine();
        setMasterPassWord(master);

    }
    
}
