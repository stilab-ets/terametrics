package org.stilab;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.stilab.parser.facade.Command;
import org.stilab.parser.facade.DirCommand;
import org.stilab.parser.facade.FileCommand;
import org.stilab.parser.facade.UriCommand;

public class Bootstrap {


    @Parameter(names = {"-f", "--file"}, description = "Absolute Path and name of file to use")
    private String file;
    @Parameter(names = {"-t", "--target"}, description = "Absolute Path Target to save the generated measures", required = true)
    private String target;
    @Parameter(names = {"-h", "--help"}, description = "Help/Usage", help = true)
    private boolean help;
    @Parameter(names = {"-b", "--bloc"}, description = "Measure Quality Metrics for a given '.tf' file")
    private boolean bloc;
    @Parameter(names = {"-l"}, description = "Measure Quality Metrics for a local folder that contains '.tf' files")
    private boolean l;
    @Parameter(names = {"--local"}, description = "Local Absolute Path to a local folder that contains '.tf' files")
    private String local;
    @Parameter(names = {"-g", "--git"}, description = "Measure Quality Metrics for a git repository (GitHub/GitLab) that contains '.tf' files")
    private boolean git;
    @Parameter(names = {"-r", "--repo"}, description = "Repo full Name of the git repository (GitHub/GitLab) that contains '.tf' files")
    private String repo;

    @Parameter(names = {"-p", "--project"}, description = "Project Name in the case you have local folder/repo")
    private String projectName;


    private void processCommandLineArguments(final String[] arguments) {

        final JCommander commander = JCommander.newBuilder().programName("Terraform Metrics Based-AST").addObject(this).build();
        commander.parse(arguments);

        Command command = null;

        if (help) { commander.usage();}

        else {
          try {

            // Measure the metrics for the given script per block
              if (bloc) {
                command = new FileCommand(file, target);
              }

              if (l) {
                command = new DirCommand(projectName, repo, target);
              }

              if (git) {
                command = new UriCommand(projectName, repo, target);

              }
            assert command != null;
            command.execute();
          } catch (Exception e) {
//            logger.error("Error while identifying the right block: {}", e.getMessage(), e);
          }
        }
    }

    public static void main(final String[] arguments) {
        new Bootstrap().processCommandLineArguments(arguments);
    }
}


