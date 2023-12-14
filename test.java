import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionDecryptionExample {

    private static final String secretKeyString = "MySecretKey12345"; // Hardcoded secret key

    public static void main(String[] args) throws Exception {
        // Perform encryption and decryption
        String encryptedText = encrypt("This is a secret message.");
        System.out.println("Encrypted Text: " + encryptedText);

        String decryptedText = decrypt(encryptedText);
        System.out.println("Decrypted Text: " + decryptedText);
    }

    // Method to encrypt plaintext using the hardcoded SecretKey
    private static String encrypt(String plainText) throws Exception {
        byte[] keyBytes = secretKeyString.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Method to decrypt encrypted text using the hardcoded SecretKey
    private static String decrypt(String encryptedText) throws Exception {
        byte[] keyBytes = secretKeyString.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
