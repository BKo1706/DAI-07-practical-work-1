package ch.heigvd.dai.commands;
import ch.heigvd.dai.Utils;

import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "decrypt", description = "Decrypt a file.")
public class Decrypt implements Callable<Integer> {
    @CommandLine.ParentCommand
    protected Root parent;

    @CommandLine.Option(
            names = {"-k", "--key"},
            description = "The name of the file in which the secret key used for decryption is stored",
            required = true)
    private String keyfilename;

    @Override
    public Integer call() throws Exception {
        Utils.decrypteFile(keyfilename, parent.getAlgorithm().toString(), parent.getFilename(), parent.getFilename() + ".dec");
        return 0;
    }
}