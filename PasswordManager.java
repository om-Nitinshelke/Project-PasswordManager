import java.util.*;
import java.security.*;
import java.io.*;

public class PasswordManager {

    private static void setMasterPassWord(String masterPassWord) throws Exception {
        byte[] keybytes=Array.copyOf(masterPassWord.getBytes(),16);

        secretKey=new SecretKeySpec(keyBytes,ALGORITHM);
    }

    private static void saveToFile() throws Exception {
        try (FileOutputStream fos=new FileOutputStream("Credentials.txt")) {
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
