import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.apache.commons.lang3.SerializationUtils;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Scanner;

/*
   PasswordProtector class responsible encrypting/decrypting the data,
   as well as storing them and loading them from the files.
*/
public class Protector {
    private Cipher cipher;
    private BouncyCastleProvider bouncyCastleProvider;
    public Protector(BouncyCastleProvider bouncyCastleProvider) {
        this.bouncyCastleProvider = bouncyCastleProvider;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", bouncyCastleProvider);
        }
        catch(Exception e){
            cipher = null;
        }
        // initialize a Cipher-object
    }
    public boolean encrypt(String password, Table passwordTable) {
        try{
            // generating initialization vector randomly
            SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT", bouncyCastleProvider);
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);

            // salting to prevent rainbow table based attacks
            byte[] salt = new byte[32];
            secureRandom.nextBytes(salt);

            // password based key generation
            SecretKey key = passwordBasedKeyGeneration(password, salt, 10000, 256);
            if( key == null)
                return false;

            // taking the cipher object to the encryption mode to call doFinal
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

            // in order to encrypt, the data string first needs to be written into txt

            // we serialize using SerializationUtils of apache commons lang3 module
            Files.write(Paths.get("encryptedFiles/data.txt"), SerializationUtils.serialize(passwordTable.getRecords()));
            byte[] input = Files.readAllBytes(Paths.get("encryptedFiles/data.txt"));
            byte[] output = cipher.doFinal(input);

            String outFile = "encryptedFiles/" + Hex.toHexString(iv) + ".aes";

            // save the iv to a txt file to use in decryption
            Files.write( Paths.get("encryptedFiles/iv.txt"), Hex.toHexString(iv).getBytes());
            // save the salt to a txt file to use in decryption
            Files.write(Paths.get("encryptedFiles/salt.txt"), Hex.toHexString(salt).getBytes());

            Files.write(Paths.get(outFile), output);
            File text = new File("encryptedFiles/data.txt");
            text.delete();
            return true;
        }
        catch(Exception e)
        {
            System.out.println( e);
            return false;
        }
    }

    public boolean decrypt(String password, ArrayList<Record> decryptedData)
    {
        // Reading the iv from iv.txt file
        String iv = "";
        String salt = "";
        try {
            File ivFile = new File("encryptedFiles/iv.txt");
            Scanner scanner = new Scanner(ivFile);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                iv = data;
            }
            scanner.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        // Reading the salt from salt.txt file
        try {
            File ivFile = new File("encryptedFiles/salt.txt");
            Scanner scanner = new Scanner(ivFile);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                salt = data;
            }
            scanner.close();
    } catch (Exception e) {
            System.out.println(e);
        }


        if ( !iv.equals("") && !salt.equals(""))
        {
            try {

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", bouncyCastleProvider);
                byte[] input = Files.readAllBytes(Paths.get("encryptedFiles/" + iv + ".aes"));

                // decoding iv and salt to byte arrays
                byte[] ivByte = Hex.decode(iv);
                byte[] saltByte = Hex.decode(salt);

                //password based key generation
                SecretKey key = passwordBasedKeyGeneration(password, saltByte, 10000, 256);

                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivByte));
                byte[] output = cipher.doFinal(input);

                // deserialize the byte code to an arraylist, the opposite operation of how we
                // saved the information in the first place
                ArrayList<Record> answer = (ArrayList) SerializationUtils.deserialize(output);

                // retrieving the data into the passed ArrayList
                for(int i = 0; i < answer.size(); i++){
                    decryptedData.add( new Record(answer.get(i).getSite(), answer.get(i).getUrl(), answer.get(i).getUsername(), answer.get(i).getPassword()));
                }
                return true;
            }
            catch (Exception e) {
                System.out.println(e);
                return false;
            }

        }

        return true;
    }
    public SecretKey passwordBasedKeyGeneration( String password, byte[] salt, int iterationCount, int keyLength){
        try{
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WITHHMACSHA256", bouncyCastleProvider);
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);
            return factory.generateSecret(keySpec);
        }
        catch(Exception e){
            return null;
        }
    }
}
