package org.stilab.metrics.counter.block_level;

import org.json.simple.JSONObject;
import org.sonar.iac.common.api.tree.Comment;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.SyntaxTokenImpl;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TokenIdentifier {

  TextEntropy textEntropy ;

  public TokenIdentifier() {
    textEntropy = new TextEntropy();
  }


  public List<SyntaxTokenImpl> identifyTokens (Tree root) {
    List<SyntaxTokenImpl> elements = new ArrayList<>();
    if (root != null){
      if (root instanceof SyntaxTokenImpl) {
        elements.add((SyntaxTokenImpl) root);
      }
      for (Tree child: root.children()) {
        elements.addAll( identifyTokens(child) );
      }
    }
    return elements;
  }


  public List<String> tokenizer(Tree root) {
    List<SyntaxTokenImpl>  tokens  = this.identifyTokens(root);
    List<String> values = new ArrayList<>();

    int counter = 0;

    for (SyntaxTokenImpl token: tokens) {
//      !Objects.equals(token.value(), "")
      if (!token.value().trim().isEmpty()) {
        values.add(token.value());
        counter+=1;
      }
    }
//    System.out.println(values);
    return values;
  }

  public List<Character> textualize(Tree root) {
    List<SyntaxTokenImpl> tokens = this.identifyTokens(root);
    List<Character> values = new ArrayList<>();

    for (SyntaxTokenImpl token : tokens) {
      if (!Objects.equals(token.value(), "")) {
        char[] chars = token.value().toCharArray();
        for (char c : chars) {
          if ((!Character.isWhitespace(c)) ) {
            if ((c != ' ')) {
              values.add(c);
            }
          }
        }

        if (token.comments().size() > 0) {
          for (Comment comment : token.comments()) {
            char[] commentChars = comment.value().toCharArray();
            for (char c : commentChars) {
              if (!Character.isWhitespace(c)) {
                values.add(c);
              }
            }
          }
        }
      }
    }
    return values;
  }

  public List<Double> textEntropyPerAttrs(BlockTreeImpl identifiedBlock) {
      // Get all the attributes of the identified block
      List<AttributeTreeImpl> attributes = (new AttrFinderImpl()).getAllAttributes(identifiedBlock);
      List<Double> textEntropyAttrs = new ArrayList<>();

      for (AttributeTreeImpl attribute: attributes){
        List<Character> charsAttr = this.textualize(attribute);
        Map<Character, Integer> charsFreqAttr = textEntropy.countCharacterFrequency(charsAttr);
        double attrTextEntropyMeasure = textEntropy.textEntropy(charsFreqAttr);
        textEntropyAttrs.add(attrTextEntropyMeasure);
      }
      return textEntropyAttrs;
  }

  public List<Integer> tokensPerAttrs(BlockTreeImpl identifiedBlock){
    List<AttributeTreeImpl> attributes = (new AttrFinderImpl()).getAllAttributes(identifiedBlock);
    List<Integer> numTokenPerAttrs = new ArrayList<>();

    for (AttributeTreeImpl attribute: attributes) {
      int numTokenPerAttr = tokenizer(attribute).size();
      numTokenPerAttrs.add(numTokenPerAttr);
    }
    return numTokenPerAttrs;
  }

  public int minAttrsTokens(List<Integer> tokensPerAttrs){
    if (tokensPerAttrs.isEmpty()) {
        return 0;
    }
    int min = tokensPerAttrs.get(0);
    for (Integer value: tokensPerAttrs) {
        if (value < min) {
          min = value;
        }
    }
    return min;
  }

  public int maxAttrsTokens(List<Integer> tokensPerAttrs) {
    if (tokensPerAttrs.isEmpty()) { return 0; }

    int max = tokensPerAttrs.get(0);
    for (Integer value: tokensPerAttrs) {
      if (value > max) {
         max = value;
      }
    }
    return max;
  }

  public double avgAttrsTokens(List<Integer> tokensPerAttrs) {
    if (tokensPerAttrs.isEmpty()) {
      // Return some appropriate value when the list is empty
      return 0.0; // You can change this to another default value if needed
    }

    double sum = 0.0;
    for (Integer value : tokensPerAttrs) {
      sum += value; // Add up all values in the list
    }

    double avgAttrsTokens = (double) sum / tokensPerAttrs.size();
    BigDecimal roundedAverage = new BigDecimal(avgAttrsTokens).setScale(2,
      RoundingMode.HALF_UP);
    return roundedAverage.doubleValue();
  }


  public double minAttrsTextEntropy(List<Double> textEntropyPerAttrs){

    if (textEntropyPerAttrs.isEmpty()) {
      // Return some appropriate value when the list is empty
      return 0.0; // You can change this to another default value if needed
    }

    double min = textEntropyPerAttrs.get(0); // Initialize min with the first value
      for (Double value: textEntropyPerAttrs) {
        if (value < min) {
          min = value; // Update min if a smaller value is found
        }
      }

    double minAttrsTextEntropy = min;
    BigDecimal roundedAverage = new BigDecimal(minAttrsTextEntropy).setScale(2,
      RoundingMode.HALF_UP);
    return roundedAverage.doubleValue();
  }

  public double maxAttrsTextEntropy(List<Double> textEntropyPerAttrs){
    if (textEntropyPerAttrs.isEmpty()) {
      // Return some appropriate value when the list is empty
      return 0.0; // You can change this to another default value if needed
    }

    double max = textEntropyPerAttrs.get(0); // Initialize max with the first value
    for (Double value : textEntropyPerAttrs) {
      if (value > max) {
        max = value; // Update max if a larger value is found
      }
    }

    double maxAttrsTextEntropy = max;
    BigDecimal roundedAverage = new BigDecimal(maxAttrsTextEntropy).setScale(2,
      RoundingMode.HALF_UP);
    return roundedAverage.doubleValue();
  }

  public double avgAttrsTextEntropy(List<Double> textEntropyPerAttrs) {
    if (textEntropyPerAttrs.isEmpty()) {
      // Return some appropriate value when the list is empty
      return 0.0; // You can change this to another default value if needed
    }

    double sum = 0.0;
    for (Double value : textEntropyPerAttrs) {
      sum += value; // Add up all values in the list
    }

    double avgAttrsTextEntropy = (double) sum / textEntropyPerAttrs.size();
    BigDecimal roundedAverage = new BigDecimal(avgAttrsTextEntropy).setScale(2,
      RoundingMode.HALF_UP);
    return roundedAverage.doubleValue();
  }

  public int numberTokens(BlockTreeImpl identifiedBlock){
    return this.tokenizer(identifiedBlock).size();
  }

  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

    List<Double> textEntropyPerAttrs = textEntropyPerAttrs(identifiedBlock);
    List<Character> characters = this.textualize(identifiedBlock);

    double minAttrsTextEntropy = minAttrsTextEntropy(textEntropyPerAttrs);
    double maxAttrsTextEntropy = maxAttrsTextEntropy(textEntropyPerAttrs);
    double avgAttrsTextEntropy = avgAttrsTextEntropy(textEntropyPerAttrs);

    List<Integer> tokensPerAttrs = tokensPerAttrs(identifiedBlock);
    int numTokens = numberTokens(identifiedBlock);
    int minTokensPerAttr = minAttrsTokens(tokensPerAttrs);
    int maxTokensPerAttr = maxAttrsTokens(tokensPerAttrs);
    double avgTokensPerAttr = avgAttrsTokens(tokensPerAttrs);

    // For each attribute, measure its entropy
    // Take the max_, min_, avg_
    Map<Character, Integer> characterFrequency = textEntropy.countCharacterFrequency(characters);
    double textEntropyMeasure = textEntropy.textEntropy(characterFrequency);

    metrics.put("textEntropyMeasure",  textEntropyMeasure);
    metrics.put("minAttrsTextEntropy", minAttrsTextEntropy);
    metrics.put("maxAttrsTextEntropy", maxAttrsTextEntropy);
    metrics.put("avgAttrsTextEntropy", avgAttrsTextEntropy);
//    ----------
    metrics.put("numTokens", numTokens);
    metrics.put("minTokensPerAttr", minTokensPerAttr);
    metrics.put("maxTokensPerAttr", maxTokensPerAttr);
    metrics.put("avgTokensPerAttr", avgTokensPerAttr);
    return metrics;
  }


}
