import java.util.*;

public class PasswordManager {

    private static void setMasterPassWord(String masterPassWord) throws Exception {
        byte[] keybytes=Array.copyOf(masterPassWord.getBytes(),16);

        secretKey=new SecretKeySpec(keyBytes,ALGORITHM);
    }



    public static void main(String args[]) throws Exception {
        Scanner sc=new Scanner(System.in);
        System.out.print("Set/Enter Master Password:");

        String master=sc.nextLine();
        setMasterPassWord(master);
    }
    
}
