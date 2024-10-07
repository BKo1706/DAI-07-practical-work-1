package ch.heigvd.dai.commands;

import java.util.concurrent.Callable;
import picocli.CommandLine;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@CommandLine.Command(name = "encrypt", description = "Encrypt a file.")
public class Encrypt implements Callable<Integer> {
    @CommandLine.ParentCommand
    protected Root parent;

    public static void encryptFile(String key, String algorithm, String inputFile, String outputFile) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), algorithm);
        Cipher cipher = Cipher.getInstance(algorithm);
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

    @Override
    public Integer call() throws Exception {
        encryptFile(parent.getKey(), parent.getAlgorithm().toString(), parent.getFilename(), parent.getFilename() + ".enc");
        return 0;
    }
}