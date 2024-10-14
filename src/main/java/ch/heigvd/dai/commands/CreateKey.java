package ch.heigvd.dai.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.concurrent.Callable;

@Command(name = "createKey", description = "Generate key pair and save to files.")
public class CreateKey implements Callable<Integer> {
    @CommandLine.ParentCommand
    protected Root parent;    
    
    @CommandLine.Option(
            names = {"-l", "--lenght"},
            description = "The length of the key in bits",
            required = false)
    private int keyLenght = 256;

    public static void generatePrivKey(String privateKeyFile, String algorithm, int keyLength) throws NoSuchAlgorithmException, IOException {
        // Générer une privée clé
        KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
        keyGen.init(keyLength); // Taille de clé
        SecretKey secretKey = keyGen.generateKey();

        // Encoder la clé privée en base64
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        // Sauvegarder la clé privée encodée en base64 dans un fichier texte
        try (FileOutputStream fos = new FileOutputStream(privateKeyFile)) {
            fos.write(encodedKey.getBytes());
        }
    }

    public Integer call() throws Exception {
        generatePrivKey(parent.getFilename(), parent.getAlgorithm().toString(), keyLenght);
        System.out.println("La clé privée a été générée et sauvegardée avec succès.");
        return 0;
    }
}