package ch.heigvd.dai.commands;

import java.util.concurrent.Callable;
import picocli.CommandLine;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.util.Base64;

@CommandLine.Command(name = "encrypt", description = "encrypt a file.")
public class Encrypt {
    @CommandLine.ParentCommand protected Root parent;
    public static void encryptFile(String key, String inputFile, String outputFile) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), parent.getAlgorithm());
        Cipher cipher = Cipher.getInstance(parent.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        FileInputStream inputStream = new FileInputStream(inputFile);
        byte[] inputBytes = new byte[(int) inputStream.available()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();
    }
}
