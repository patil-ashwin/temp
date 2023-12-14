import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionService {

    private final String secretKey = "YourSecretKey"; // Replace with your own secret key

    public String encrypt(String plainText) throws Exception {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] ivBytes = new byte[cipher.getBlockSize()];
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedText) throws Exception {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] ivBytes = new byte[cipher.getBlockSize()];
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}



import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import java.util.Properties;

public class CustomPropertyConfigurer extends PropertyPlaceholderConfigurer {

    @Override
    protected void convertProperties(Properties props) {
        super.convertProperties(props);

        // Iterate through properties and modify specific ones as needed
        for (String propertyName : props.stringPropertyNames()) {
            String propertyValue = props.getProperty(propertyName);
            if (propertyName.startsWith("xyz")) {
                // Modify property value as needed
                props.setProperty(propertyName, "text2");
            }
            // Add other conditions or modifications for different properties here
        }
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) {
        super.processProperties(beanFactoryToProcess, props);
    }
}


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringExtractor {

    public static void main(String[] args) {
        String inputString = "NE(mytext)";

        // Define the pattern to match text inside parentheses
        Pattern pattern = Pattern.compile("NE\\((.*?)\\)");
        Matcher matcher = pattern.matcher(inputString);

        // Check if the pattern is found in the input string
        if (matcher.find()) {
            String extractedText = matcher.group(1); // Extract the text captured by the group
            System.out.println("Extracted Text: " + extractedText);
        } else {
            System.out.println("Pattern not found.");
        }
    }
}


