package org.stilab.metrics.counter.block.metrics;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RegexIdentifier {
    public List<String> regexFunctions = new ArrayList<>(Arrays.asList("replace", "regexall", "regex"));
    public List<String> regexOperations = new ArrayList<>();
    public List<String> identifyRegexOperators(BlockTreeImpl blockTree) {
      List<String> functions = (new FunctionCallExpressionIdentifier())
        .filterFCfromBlock(blockTree).stream()
        .map(function -> function.name().value())
        .collect(Collectors.toList());

      regexOperations = functions.stream()
        .filter(regexFunctions::contains)
        .collect(Collectors.toList());

      return regexOperations;
    }

    public int countRegex() { return regexOperations.size(); }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock){

      this.identifyRegexOperators(identifiedBlock);
      int numRegex = this.countRegex();
      metrics.put("numRegex", numRegex);
      return metrics;
    }

}