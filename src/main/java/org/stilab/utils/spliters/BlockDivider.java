package org.stilab.utils.spliters;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.BlockLabelVisitor;
import org.stilab.metrics.iterators.TopBlockFinder;
import org.stilab.utils.counter.ServiceCounter;
import org.stilab.utils.mapper.BlockPosition;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BlockDivider {

      private String filePath;

      public BlockDivider(String filePath) {
        this.filePath = filePath;
      }

      public List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> divideFilePerBlock() {

        String fileContent = parseFileContent(filePath);

        List<BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer>> blockPositions = new ArrayList<>();

        // Call Hcl Parser
        HclParser hclParser = new HclParser();

        try{
          // Parse the Content Of The HCL FILE
          Tree tree = hclParser.parse(fileContent);
          // GET THE TOP BLOCK
          TopBlockFinder topBlockFinder = new TopBlockFinder();
          List<BlockTreeImpl> blocks = topBlockFinder.findTopBlock(tree);

          for (BlockTreeImpl blockTree: blocks) {

            // identify the start index
            int startIndex = blockTree.key().textRange().start().line();
            // identify the end index
            int endIndex   = blockTree.value().textRange().end().line();
            String blockContent = ServiceCounter.getInstance().extractDesiredContent(fileContent, startIndex, endIndex);
            // Construct the block identifier
            BlockLabelVisitor blockLabelVisitor = new BlockLabelVisitor();
            List<String> labels = blockLabelVisitor.identifyLabelsOfBlock(blockTree);
            labels.add(0, blockTree.key().value());
            String blockIdentifiers = this.concatElementsOfList(labels);
            // compute the size of the block
            int depthOfBlock = endIndex - startIndex + 1;
            // Block Position
            BlockPosition<Integer, Integer, String, BlockTreeImpl, Object, String, Integer> blockPosition = new BlockPosition<>(startIndex, endIndex, blockContent, blockTree, blockIdentifiers, depthOfBlock);
            blockPositions.add(blockPosition);
          }
        } catch ( Exception e) {
          // Handle the exception appropriately (e.g., logging, error handling, etc.)
          e.printStackTrace();
          // Return an empty list of BlockPosition in case of exception
          return blockPositions;
        }
        return blockPositions;
      }

      public String concatElementsOfList(List<String> stringList) {
        return String.join(" ", stringList);
      }

      public String parseFileContent(String filePath) {
        try {
          // Read all bytes from the file and convert them to a string
          byte[] contentBytes = Files.readAllBytes(Paths.get(filePath));
          return new String(contentBytes);
        } catch (IOException e) {
          // Handle any errors that may occur during file reading
          e.printStackTrace();
          return null; // Return null to indicate an error occurred
        }
      }

}
