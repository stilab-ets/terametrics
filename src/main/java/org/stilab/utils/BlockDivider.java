package org.stilab.utils;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.symbols.ListSymbol;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.interfaces.IBlockComplexity;
import org.stilab.metrics.BlockLabelIdentifier;
import org.stilab.metrics.counter.block.finder.TopBlockFinder;
import org.stilab.metrics.counter.block.size.BlockComplexity;

import java.io.File;
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

      public List<BlockPosition> divideFilePerBlock() {

        String fileContent = parseFileContent(filePath);
        List<BlockPosition> blockPositions = new ArrayList<>();
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
            int start_index = blockTree.key().textRange().start().line();
            // identify the end index
            int end_index   = blockTree.value().textRange().end().line();
            String blockContent = ServiceCounter.getInstance().extractDesiredContent(fileContent,
              start_index, end_index);

            // Construct the block identifier
            BlockLabelIdentifier blockLabelIdentifier = new BlockLabelIdentifier();
            List<String> labels = blockLabelIdentifier.identifyLabelsOfBlock(blockTree);
            labels.add(0, blockTree.key().value());
            String block_identifiers = this.concatElementsOfList(labels);

            // compute the size of the block
            int depthOfBlock = end_index - start_index + 1;

            // Block Position
            BlockPosition blockPosition = new BlockPosition<>(start_index, end_index, blockContent, blockTree, block_identifiers, depthOfBlock);
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
          String content = new String(contentBytes);

          return content;
        } catch (IOException e) {
          // Handle any errors that may occur during file reading
          e.printStackTrace();
          return null; // Return null to indicate an error occurred
        }
      }

}
