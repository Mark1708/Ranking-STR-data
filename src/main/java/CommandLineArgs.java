import com.beust.jcommander.Parameter;

public class CommandLineArgs {
    @Parameter(names = {"-p", "--path"},
            description = "Path to csv file",
            required = true)
    private String dataPath;

    @Parameter(names = {"-i", "--index"},
            description = "Index of haplotype",
            required = true)
    private String haplotypeIndex;

    @Parameter(names = {"-a", "--age"},
            description = "Average age",
            required = true)
    private int averageAge;

    @Parameter(names = {"-h", "--help"},
            description = "Help/Usage",
            help = true)
    private boolean help;

    public String getDataPath() {
        return dataPath;
    }

    public String getHaplotypeIndex() {
        return haplotypeIndex;
    }

    public int getAverageAge() {
        return averageAge;
    }

    public boolean isHelp() {
        return help;
    }
}
