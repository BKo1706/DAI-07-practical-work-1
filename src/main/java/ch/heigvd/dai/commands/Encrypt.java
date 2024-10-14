package ch.heigvd.dai.commands;
import ch.heigvd.dai.Utils;

import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "encrypt", description = "Encrypt a file.")
public class Encrypt implements Callable<Integer> {
    @CommandLine.ParentCommand
    protected Root parent;

    @CommandLine.Option(
            names = {"-k", "--key"},
            description = "The name of the file in which the secret key used for encryption is stored",
            required = true)
    private String keyfilename;


    @Override
    public Integer call() throws Exception {
        Utils.encrypteFile(keyfilename, parent.getAlgorithm().toString(), parent.getFilename(), parent.getFilename() + ".enc");
        return 0;
    }
}