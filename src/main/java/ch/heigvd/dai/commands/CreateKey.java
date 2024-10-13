package ch.heigvd.dai.commands;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

@Command(name = "createKey", description = "Generate key and save to files.")
public class CreateKey implements Callable<Integer> {
    @CommandLine.ParentCommand
    protected Root parent;    

    public static void generatePrivKey(String privateKeyFile, String algorithm) throws NoSuchAlgorithmException, IOException {
        // Générer une paire de clés de l'alogrithme demandé
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        
        PrivateKey privateKey = pair.getPrivate();

        // Sauvegarder la clé privée dans un fichier
        try (FileOutputStream fos = new FileOutputStream(privateKeyFile)) {
            fos.write(privateKey.getEncoded());
        }
    }

    public Integer call() throws Exception {
        generatePrivKey(parent.getKey(), parent.getAlgorithm().toString());
        System.out.println("La clé a été générée et sauvegardée avec succès.");
        return 0;
    }
}