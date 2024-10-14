package ch.heigvd.dai;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.Cipher;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Utils {

    /*
     * Fonction pour charger une clé à partir d'un fichier
     * @param fileName : le nom du fichier contenant la clé
     * @param algorithm : l'algorithme de chiffrement utilisé
     */
    public static SecretKey loadKeyFromFile(String fileName, String algorithm) {
        byte[] decodedKey = null;
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(fileName)); // Lire les bytes de la clé
            decodedKey = Base64.getDecoder().decode(keyBytes); // Décoder la clé en base64
        } catch (IOException e) {
            System.out.println("Error reading key : " + e.getMessage());
            System.exit(1);
        }
        return new SecretKeySpec(decodedKey, algorithm); // Reconstruire la clé
    }

    /*
        * Fonction pour chiffrer ou déchiffrer un fichier
        * @param secretKey :
        * @param algorithm : l'algorithme de chiffrement utilisé
        * @param inputFile : le fichier à chiffrer ou déchiffrer
        * @param outputFile : le fichier de sortie
        * @param opMode : le mode de chiffrement (chiffrement ou déchiffrement)
    */
    public static void encrypteDecryptFile(SecretKey secretKey, String algorithm, String inputFile, String outputFile, int opMode) {
        try {
            // Créer un objet Cipher pour l'algorithme choisi
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(opMode, secretKey); // Initialiser en mode déchiffrement

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
            System.out.println("Error cryption: " + e.getMessage());
        }
    }

    /*
     * Fonction pour chiffrer ou déchiffrer un fichier
     * @param secretKeyFile : le fichier contenant la clé
     * @param algorithm : l'algorithme de chiffrement utilisé
     * @param inputFile : le fichier à chiffrer ou déchiffrer
     * @param outputFile : le fichier de sortie
     * @param opMode : le mode de chiffrement (chiffrement ou déchiffrement)
     */
    public static void encryptedecryptFile(String secretKeyFile, String algorithm, String inputFile, String outputFile, int opMode){
        SecretKey key = loadKeyFromFile(secretKeyFile, algorithm);
        encrypteDecryptFile(key, algorithm, inputFile, outputFile, opMode);
    }
}
