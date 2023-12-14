import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class EncryptionDecryptionExample {

    private static SecretKey secretKey;

    public static void main(String[] args) throws Exception {
        // Generate a secret key (only once)
        generateSecretKey();

        // Perform encryption and decryption
        String encryptedText = encrypt("This is a secret message.");
        System.out.println("Encrypted Text: " + encryptedText);

        String decryptedText = decrypt(encryptedText);
        System.out.println("Decrypted Text: " + decryptedText);
    }

    // Method to generate a SecretKey
    private static void generateSecretKey() throws Exception {
        secretKey = KeyGenerator.getInstance("AES").generateKey();
    }

    // Method to encrypt plaintext using the shared SecretKey
    private static String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Method to decrypt encrypted text using the shared SecretKey
    private static String decrypt(String encryptedText) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
