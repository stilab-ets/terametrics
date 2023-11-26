package org.stilab.metrics.data.repository;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.metrics.visitors.TokenVisitor;

import java.util.List;
import java.util.Map;

public class TokenRepository implements Repository {

  @Override
  public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

    TokenVisitor tokenVisitor= new TokenVisitor();

    List<Double> textEntropyPerAttrs = tokenVisitor.textEntropyPerAttrs(identifiedBlock);
    List<Character> characters = tokenVisitor.textualize(identifiedBlock);

    double minAttrsTextEntropy = tokenVisitor.minAttrsTextEntropy(textEntropyPerAttrs);
    double maxAttrsTextEntropy = tokenVisitor.maxAttrsTextEntropy(textEntropyPerAttrs);
    double avgAttrsTextEntropy = tokenVisitor.avgAttrsTextEntropy(textEntropyPerAttrs);

    List<Integer> tokensPerAttrs = tokenVisitor.tokensPerAttrs(identifiedBlock);
    int numTokens = tokenVisitor.numberTokens(identifiedBlock);
    int minTokensPerAttr = tokenVisitor.minAttrsTokens(tokensPerAttrs);
    int maxTokensPerAttr = tokenVisitor.maxAttrsTokens(tokensPerAttrs);
    double avgTokensPerAttr = tokenVisitor.avgAttrsTokens(tokensPerAttrs);

    // For each attribute, measure its entropy
    // Take the max_, min_, avg_
    Map<Character, Integer> characterFrequency = tokenVisitor.getTextEntropy().countCharacterFrequency(characters);
    double textEntropyMeasure = tokenVisitor.getTextEntropy().textEntropy(characterFrequency);

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
