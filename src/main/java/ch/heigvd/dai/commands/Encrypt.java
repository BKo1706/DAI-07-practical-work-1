package ch.heigvd.dai.commands;
import ch.heigvd.dai.Utils;

import java.util.concurrent.Callable;
import picocli.CommandLine;

import javax.crypto.Cipher;

@CommandLine.Command(name = "encrypt", description = "Encrypt a file.")
public class Encrypt implements Callable<Integer> {
    @CommandLine.ParentCommand
    protected Root parent;

    @CommandLine.Option(
            names = {"-k", "--key"},
            description = "The name of the file in which the secret key used for encryption/decryption is stored",
            required = true)
    protected String keyfilename;


    @Override
    public Integer call() throws Exception {
        Utils.encryptedecryptFile(keyfilename, parent.getAlgorithm().toString(), parent.getFilename(), parent.getFilename() + ".enc", Cipher.ENCRYPT_MODE);
        return 0;
    }
}