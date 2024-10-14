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

@CommandLine.Command(name = "decrypt", description = "Decrypt a file.")
public class Decrypt implements Callable<Integer> {
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

    private static void decryptFile(SecretKey secretKey, String algorithm, String inputFile, String outputFile) {
        try {
            // Créer un objet Cipher pour l'algorithme choisi
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey); // Initialiser en mode déchiffrement

            // Lire le fichier chiffré
            FileInputStream fis = new FileInputStream(inputFile);
            byte[] inputBytes = new byte[fis.available()];
            fis.read(inputBytes);
            fis.close();

            // Déchiffrer les données du fichier
            byte[] outputBytes = cipher.doFinal(inputBytes);

            // Écrire le fichier déchiffré dans le fichier de sortie
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(outputBytes);
            fos.close();
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public Integer call() throws Exception {
        decryptFile(loadKeyFromFile(keyfilename, parent.getAlgorithm().toString()), parent.getAlgorithm().toString(), parent.getFilename(), parent.getFilename() + ".dec");
        return 0;
    }
}