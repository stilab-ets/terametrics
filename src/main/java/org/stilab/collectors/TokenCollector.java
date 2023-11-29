package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.calculators.TokenCalculator;
import org.stilab.visitors.TokenVisitor;

import java.util.List;
import java.util.Map;

public class TokenCollector implements Decorator {

  @Override
  public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

    TokenCalculator tokenCalculator = new TokenCalculator();

    List<Double> textEntropyPerAttrs = tokenCalculator.textEntropyPerAttrs(identifiedBlock);
    double minAttrsTextEntropy = tokenCalculator.minAttrsTextEntropy(textEntropyPerAttrs);
    double maxAttrsTextEntropy = tokenCalculator.maxAttrsTextEntropy(textEntropyPerAttrs);
    double avgAttrsTextEntropy = tokenCalculator.avgAttrsTextEntropy(textEntropyPerAttrs);

    List<Integer> tokensPerAttrs = tokenCalculator.tokensPerAttrs(identifiedBlock);
    int numTokens = tokenCalculator.numberTokens(identifiedBlock);
    int minTokensPerAttr = tokenCalculator.minAttrsTokens(tokensPerAttrs);
    int maxTokensPerAttr = tokenCalculator.maxAttrsTokens(tokensPerAttrs);
    double avgTokensPerAttr = tokenCalculator.avgAttrsTokens(tokensPerAttrs);

    List<Character> characters = tokenCalculator.getTokenVisitor().textualize(identifiedBlock);
    // For each attribute, measure its entropy
    // Take the max_, min_, avg_
    Map<Character, Integer> characterFrequency = tokenCalculator.getTokenVisitor().getTextEntropy().countCharacterFrequency(characters);
    double textEntropyMeasure = tokenCalculator.getTokenVisitor().getTextEntropy().textEntropy(characterFrequency);

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
