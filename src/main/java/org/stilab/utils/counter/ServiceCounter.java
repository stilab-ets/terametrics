package org.stilab.utils.counter;

import org.sonar.iac.common.parser.grammar.LexicalConstant;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceCounter {

  private static ServiceCounter instance;

  private static final String SPLITTER = "\\r?\\n";

  private ServiceCounter() {
    // Initialization code here
  }

  // Public static method to access the singleton instance
  public static synchronized ServiceCounter getInstance() {
        if (instance == null) {
          instance = new ServiceCounter();
    }
    return instance;
  }

  public String extractDesiredContent(String fileContent, int startLine, int endLine) {

    StringBuilder content = new StringBuilder();
    String[] lines = fileContent.split(SPLITTER);
    int lineNumber = 0;
    for (String line : lines) {
      lineNumber++;

      if (lineNumber >= startLine && lineNumber <= endLine) {
        content.append(line).append("\n");
      }
    }
    return content.toString();

  }

  public String parseFileContentByPart(String filePath, int startLine, int endLine) {

    StringBuilder content = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

      String line;
      int lineNumber = 0;

      while ((line = reader.readLine()) != null && lineNumber <= endLine) {

        lineNumber++;
        if (lineNumber >= startLine && lineNumber <= endLine ) {
          content.append(line).append("\n");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return content.toString();
  }

  public Matcher matchMultiLineComment(String parsedContent){
    // Remove multi-line comments from the content
    Pattern pattern = Pattern.compile(LexicalConstant.MULTI_LINE_COMMENT);
    return pattern.matcher(parsedContent);
  }

  public Matcher matchSingleLineCommentHash(String parsedContent) {
    // Remove single-line comments from the content
    Pattern pattern1 = Pattern.compile(LexicalConstant.SINGLE_LINE_COMMENT_HASH);
    return pattern1.matcher(parsedContent);
  }

  public Matcher matchSingleLineCommentDoubleSlash(String parsedContent) {
    // Remove single-line-comment-double-slash from the content
    Pattern pattern2 = Pattern.compile(LexicalConstant.SINGLE_LINE_COMMENT_DOUBLE_SLASH);
    return pattern2.matcher(parsedContent);
  }

  public int countLineOfCode(String parsedContent) {
    parsedContent = matchMultiLineComment(parsedContent).replaceAll("");
    parsedContent = matchSingleLineCommentHash(parsedContent).replaceAll("");
    parsedContent = matchSingleLineCommentDoubleSlash(parsedContent).replaceAll("");
    int lineOfCode = 0;
    String[] lines = parsedContent.split(SPLITTER);
    for (String line : lines) {
      String trimmedLine = line.trim();
      if ( !trimmedLine.isEmpty() ) {
        lineOfCode++;
      }
    }
    return lineOfCode;
  }

  public int countCommentsLines(String parsedContent) {
    return countMultilineComments(parsedContent) + countSingleLineHashComments(parsedContent) ;
  }

  public int countBlankLinesInsideBlock(String parsedContent) {
    String[] lines = parsedContent.split(SPLITTER);
    int blankLinesCount = 0;
    for (String line : lines) {
      String trimmedLine = line.trim();
      if ( trimmedLine.isEmpty() ) {
        blankLinesCount++;
      }
    }
    return blankLinesCount;
  }

  public int countMultilineComments(String content) {
    Matcher matcher = matchMultiLineComment(content);
    int commentCount = 0;
    while (matcher.find()) {
      int nonBlankLineCount = countNonBlankLines(content.substring(matcher.start(), matcher.end()));
      commentCount += nonBlankLineCount;
    }
    return commentCount;
  }

  public int countNonBlankLines(String comment) {
    String[] lines = comment.split("\r\n|\r|\n");
    int nonBlankLineCount = 0;
    for (String line : lines) {
      if (!line.trim().isEmpty()) {
        nonBlankLineCount++;
      }
    }
    return nonBlankLineCount;
  }

  public int countSingleLineHashComments(String content) {
    Matcher matcher = matchSingleLineCommentHash(content);
    int commentCount = 0;
    while (matcher.find()) {
      commentCount++;
    }
    return commentCount;
  }

}
