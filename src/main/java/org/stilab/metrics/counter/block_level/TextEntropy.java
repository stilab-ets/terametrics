package org.stilab.metrics.counter.block_level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextEntropy {



//  1. Calculate the frequency of each character in the text.
  public Map<Character, Integer> countCharacterFrequency(List<Character> characters) {
    Map<Character, Integer> frequencyMap = new HashMap<>();

    for (Character c : characters) {
      frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
    }

    return frequencyMap;
  }

//  2. Calculate the entropy
  public double textEntropy(Map<Character, Integer> characterFrequency) {

    double entropy = 0.0;

    int totalCharacters = characterFrequency.values().stream().mapToInt(Integer::intValue).sum();

    for (char c : characterFrequency.keySet()) {
      double probability = (double) characterFrequency.get(c) / totalCharacters;
      entropy -= probability * log2(probability);
    }

    return entropy;
  }

  private double log2(double num) {
    return Math.log(num) / Math.log(2);
  }



}
