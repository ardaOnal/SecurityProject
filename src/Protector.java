import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.apache.commons.lang3.SerializationUtils;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/* PasswordProtector class for encrypting/decrypting
   storing/loading operations
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
    public boolean encrypt(String password, Table passwordTable){
        try{
            //generating initialization vector randomly
            SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT", bouncyCastleProvider);
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);

            //salting to prevent rainbow table based attacks
            byte[] salt = new byte[32];
            secureRandom.nextBytes(salt);

            //password based key generation
            SecretKey key = passwordBasedKeyGeneration(password, salt, 10000, 256);
            if( key == null)
                return false;

            //taking the cipher object to the encryption mode to call doFinal
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

            //in order to encrypt, the data string first needs to be written into txt

            Files.write(Paths.get("encryptedFiles/data.txt"), SerializationUtils.serialize(passwordTable.getRecords()));
            byte[] input = Files.readAllBytes(Paths.get("encryptedFiles/data.txt"));
            byte[] output = cipher.doFinal(input);

            String outFile = "encryptedFiles/" + Hex.toHexString(iv) + ".aes";

            // save the iv to a txt file to use in decryption
            Files.write( Paths.get("encryptedFiles/iv.txt"), Hex.toHexString(iv).getBytes());

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
