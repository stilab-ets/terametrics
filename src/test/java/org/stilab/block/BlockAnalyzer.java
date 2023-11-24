package org.stilab.block;

import junit.framework.TestCase;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.parser.grammar.HclGrammar;
import org.sonar.iac.terraform.tree.impl.TupleTreeImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BlockAnalyzer extends TestCase {


  public static void main(String[] args) {

    String hclFilePath = "src/test/java/org/stilab/block/data/example.tf";

    HclParser hclParser = new HclParser();

    String fileContent = parseFileContent(hclFilePath);

    Tree tree = hclParser.parse(fileContent);

    // View the class type of each element and its children
    viewClassTypes(tree, 0);

    System.out.println(tree);
  }

  public static String parseFileContent(String filePath) {
    try {
      // Read all bytes from the file and convert them to a string
      byte[] contentBytes = Files.readAllBytes(Paths.get(filePath));
      String content = new String(contentBytes);

      return content;
    } catch (IOException e) {
      // Handle any errors that may occur during file reading
      e.printStackTrace();
      return null; // Return null to indicate an error occurred
    }
  }

  private static void viewClassTypes(Tree tree, int depth) {
    // Print the class type of the current tree node
    System.out.println(getIndentation(depth) + tree.getClass().getSimpleName());

    // Recursively view class types of children
    for (Tree child : tree.children()) {
      viewClassTypes(child, depth + 1);
    }
  }

  private static String getIndentation(int depth) {
    StringBuilder indentation = new StringBuilder();
    for (int i = 0; i < depth; i++) {
      indentation.append("  "); // You can adjust the number of spaces for indentation
    }
    return indentation.toString();
  }



}
