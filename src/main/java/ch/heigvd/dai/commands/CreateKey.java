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
    private int keyLenght = 0;

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

    private boolean isValidKeyLength(int keyLength, int[] validKeySizes) {
        for (int size : validKeySizes) {
            if (size == keyLength) {
                return true;
            }
        }
        return false;
    }

    private int getKeyLength() {
        int[] sizes = parent.getAlgorithm().getKeySizes();
        if (keyLenght == 0){
            return sizes[0];
        } else if(!isValidKeyLength(keyLenght, sizes)) {
            System.out.print("La taille de la clé n'est pas valide pour cet algorithme. Les tailles de clé valides sont: {");
            for (int i = 0; i < parent.getAlgorithm().getKeySizes().length; i++) {
                if (i != 0) 
                    System.out.print(", ");       
                System.out.print(sizes[i]);
            }
            System.out.println("}");
            System.exit(1);
        }
        return keyLenght;
    }

    public Integer call() throws Exception {
        if (parent.getAlgorithm().isDepreciated())
            System.out.println("Warning: L'algorithme " + parent.getAlgorithm() + " is not recommanded for use.");

        generatePrivKey(parent.getFilename(), parent.getAlgorithm().toString(), getKeyLength());
        System.out.println("La clé privée a été générée et sauvegardée avec succès.");
        return 0;
    }
}