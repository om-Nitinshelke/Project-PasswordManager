import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Scanner;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import javax.crypto.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class PasswordManager {
    private static final String File_Name="vault.dat";
    private static final String ALGORITHM="AES";
    private static SecretKey secretKey;


    private static Map<String,String> credentials=new HashMap<>();

    //Generate AES key from master password
    private static void setMasterPassWord(String masterPassWord) throws Exception {
        byte[] keybytes=Arrays.copyOf(masterPassWord.getBytes(),16);

        secretKey=new SecretKeySpec(keybytes,ALGORITHM);
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

    private static void loadFromFile() throws Exception {
        Path path=Path.of(File_Name);
        if (!Files.exists(path) || Files.size(path)==0)
        return;
        byte[] encrypted=Files.readAllBytes(path);

        String decrypted;
        try {
            decrypted=decrypt(encrypted);
        } catch (Exception e){
            throw new Exception("Failed to decrypt vault:wrong master password or file corrupted.",e);
        }

        for (String line:decrypted.split("\\r?\\n")) {
            if (line.contains(":")) {
                String [] parts=line.split(":",2);

                String account =parts[0].trim();
                String password=parts.length > 1? parts [1].trim():"";
                credentials.put(account,password);
            }
        }
    }

    //Saving the credentials to file
    private static void saveToFile() throws Exception {
        try (FileOutputStream fos=new FileOutputStream(File_Name)) {
            StringBuilder sb=new StringBuilder();
            for (Map.Entry<String,String> entry :credentials.entrySet()) {
                sb.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
            }
            byte[] encrypted=encrypt(sb.toString());
            fos.write(encrypted);
        }
    }

    //Menu

    private static void menu(Scanner sc) throws Exception {
        while (true) {
            System.out.println("\nPassword Manager Menu:");
            System.out.println("1.Add Credential");
            System.out.println("2.View Credential");
            System.out.println("3.Exit");
            System.out.print("Choose:");
            int choice=sc.nextInt();

            switch (choice) {
                case 1:
                System.out.print("Enter account name:");
                String account =sc.nextLine();
                System.out.println("Enter password:");
                String password =sc.nextLine();
                credentials.put(account,password);
                saveToFile();
                System.out.println("Saved Successfully!");
                break;

                case 2:
                System.out.println("Stored credentilas:");
                for (Map.Entry<String,String> entry:credentials.entrySet()) {
                    System.out.println(entry.getKey() + "->" + entry.getKey());
                }

                case 3:
                System.out.println("Exiting...");
                return;

                default:
                System.out.println("Invalid Choice");
            }
        }
    }

    public static void main(String args[]) throws Exception {
        Scanner sc=new Scanner(System.in);
        System.out.print("Set/Enter Master Password:");

        String master=sc.nextLine();
        setMasterPassWord(master);

        loadFromFile();
        menu(sc);


        sc.close();
    }
    
}
