//package org.stilab.utils.locators;
//
//import org.sonar.iac.common.api.tree.Tree;
//import org.sonar.iac.terraform.parser.HclParser;
//import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
//import org.stilab.metrics.counter.block.finder.BlockFinderByIndex;
//import org.stilab.metrics.counter.block.finder.TopBlockFinder;
//import org.stilab.utils.ServiceCounter;
//import org.stilab.utils.mapper.Pair;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//
//public class BlockServiceLocator {
//
//   public BlockServiceLocator() {}
//
//   public Pair<BlockTreeImpl, String> identifyRightBlock(String filePath, int impactedLine) {
//       // Hcl Parser
//       HclParser hclParser = new HclParser();
//       File blob = new File(filePath);
//       // Parse the content of the hcl file
//       Tree tree = hclParser.parse(blob);
//       // To look for the Parsed One
//       TopBlockFinder topBlockFinder = new TopBlockFinder();
//       List<BlockTreeImpl> blocks = topBlockFinder.findTopBlock(tree);
//       // Search By Index
//       BlockFinderByIndex blockFinderByIndex = new BlockFinderByIndex();
//       // Search identify the right block
//       BlockTreeImpl identifiedBlock = blockFinderByIndex.findBlockByIndex(blocks, impactedLine);
//      // When the change doesn't concern a block of tf
//       if (identifiedBlock != null) {
//         String fileContent = parseFileContent(filePath);
//         String blockContent = ServiceCounter.getInstance().extractDesiredContent(fileContent,
//           identifiedBlock.key().textRange().start().line(),
//           identifiedBlock.value().textRange().end().line()
//         );
//         return new Pair<>(identifiedBlock, blockContent);
//       }
//       return new Pair<>(null, "");
//   }
//
//  public String parseFileContent(String filePath) {
//    try {
//      // Read all bytes from the file and convert them to a string
//      byte[] contentBytes = Files.readAllBytes(Paths.get(filePath));
//      String content = new String(contentBytes);
//
//      return content;
//    } catch (IOException e) {
//      // Handle any errors that may occur during file reading
//      e.printStackTrace();
//      return null; // Return null to indicate an error occurred
//    }
//  }
//
//
//}
