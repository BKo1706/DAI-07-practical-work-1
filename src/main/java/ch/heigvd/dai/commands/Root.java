package ch.heigvd.dai.commands;
import picocli.CommandLine;

@CommandLine.Command(
        description = "A program use for encryption and decryption of files",
        version = "1.0.0",
        subcommands = {
                Encrypt.class,
                Decrypt.class,
        },
        scope = CommandLine.ScopeType.INHERIT,
        mixinStandardHelpOptions = true)
public class Root {

    public enum AvailableAlgorithms {
        AES
    }
    @CommandLine.Parameters(index = "0", description = "The name of the file.")
    protected String filename;

    @CommandLine.Option(
            names = {"-a", "--algorithm"},
            description = "The algorithm to use (possible values: ${AES}).",
            required = true)
    protected AvailableAlgorithms algorithm;

    @CommandLine.Option(
            names = {"-k", "--key"},
            description = "The name of the file in which the secret key used for encryption/decryption is stored",
            required = true)
    protected String keyfilename;

    public String getFilename() {return filename;}

    public AvailableAlgorithms getAlgorithm() {
        return algorithm;
    }

    public String getKey() {return keyfilename;}
}
