package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.AttrFinderImpl;
import org.stilab.visitors.TokenVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TokenCalculator {

  private TokenVisitor tokenVisitor;

  public TokenCalculator() {
    tokenVisitor = new TokenVisitor();
  }

  public TokenVisitor getTokenVisitor() {
    return tokenVisitor;
  }

  public List<Double> textEntropyPerAttrs(BlockTreeImpl identifiedBlock) {
    // Get all the attributes of the identified block
    List<AttributeTreeImpl> attributes = (new AttrFinderImpl()).getAllAttributes(identifiedBlock);
    List<Double> textEntropyAttrs = new ArrayList<>();

    for (AttributeTreeImpl attribute: attributes){
      List<Character> charsAttr = tokenVisitor.textualize(attribute);
      Map<Character, Integer> charsFreqAttr = tokenVisitor.getTextEntropy().countCharacterFrequency(charsAttr);
      double attrTextEntropyMeasure = tokenVisitor.getTextEntropy().textEntropy(charsFreqAttr);
      textEntropyAttrs.add(attrTextEntropyMeasure);
    }
    return textEntropyAttrs;
  }

  public List<Integer> tokensPerAttrs(BlockTreeImpl identifiedBlock){
    List<AttributeTreeImpl> attributes = (new AttrFinderImpl()).getAllAttributes(identifiedBlock);
    List<Integer> numTokenPerAttrs = new ArrayList<>();

    for (AttributeTreeImpl attribute: attributes) {
      int numTokenPerAttr = tokenVisitor.tokenizer(attribute).size();
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

    double avgAttrsTokens = sum / tokensPerAttrs.size();
    BigDecimal roundedAverage = BigDecimal.valueOf(avgAttrsTokens).setScale(2, RoundingMode.HALF_UP);
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
    BigDecimal roundedAverage = BigDecimal.valueOf(minAttrsTextEntropy).setScale(2, RoundingMode.HALF_UP);

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
    BigDecimal roundedAverage = BigDecimal.valueOf(maxAttrsTextEntropy).setScale(2, RoundingMode.HALF_UP);
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

    double avgAttrsTextEntropy = sum / textEntropyPerAttrs.size();
    BigDecimal roundedAverage = BigDecimal.valueOf(avgAttrsTextEntropy).setScale(2, RoundingMode.HALF_UP);
    return roundedAverage.doubleValue();
  }

  public int numberTokens(BlockTreeImpl identifiedBlock){
    return this.tokenVisitor.tokenizer(identifiedBlock).size();
  }



}
