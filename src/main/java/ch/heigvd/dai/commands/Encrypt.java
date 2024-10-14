package ch.heigvd.dai.commands;

import java.util.concurrent.Callable;
import picocli.CommandLine;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.Cipher;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@CommandLine.Command(name = "encrypt", description = "Encrypt a file.")
public class Encrypt implements Callable<Integer> {
    @CommandLine.ParentCommand
    protected Root parent;

    @CommandLine.Option(
            names = {"-k", "--key"},
            description = "The name of the file in which the secret key used for encryption/decryption is stored",
            required = true)
    protected String keyfilename;

    // Fonction pour lire la clé depuis un fichier
    private static SecretKey loadKeyFromFile(String fileName, String algorithm) {
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(fileName)); // Lire les bytes de la clé
            byte[] decodedKey = Base64.getDecoder().decode(keyBytes); // Décoder la clé en base64
            return new SecretKeySpec(decodedKey, algorithm); // Reconstruire la clé
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }

    private static void encryptFile(SecretKey secretKey, String algorithm, String inputFile, String outputFile) {
        try {
            // Créer un objet Cipher pour l'algorithme choisi
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey); // Initialiser en mode chiffrement

            // Lire le fichier d'entrée
            FileInputStream fis = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[fis.available()];
            fis.read(inputBytes);
            fis.close();

            // Chiffrer les données du fichier
            byte[] outputBytes = cipher.doFinal(inputBytes);

            // Écrire le fichier chiffré dans le fichier de sortie
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(outputBytes);
            fos.close();
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public Integer call() throws Exception {
        encryptFile(loadKeyFromFile(keyfilename, parent.getAlgorithm().toString()), parent.getAlgorithm().toString(), parent.getFilename(), parent.getFilename() + ".enc");
        return 0;
    }
}