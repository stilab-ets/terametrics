package org.stilab.metrics.counter.expression;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DecisionsIdentifier {


  //    Decisions Numbers
  //    PrefixExpressionTreeImpl -----> !
  //    BinaryExpressionTreeImpl -----> && ||, == != < > <= >=

  public List<String> operators = new ArrayList<>(
    Arrays.asList("!", "&&", "||", "==", "!=", "<", ">", "<=", ">=")
  );

  public List<String> decisionsOperators = new ArrayList<>();


  public List<String> identifyDecisionsOperators(BlockTreeImpl blockTree) {

    List<String> unaryOperators = (new PrefixExpressionIdentifier())
      .filterPrefixFromBlock(blockTree).stream()
      .map(prefix -> prefix.prefix().value())
      .collect(Collectors.toList());

    List<String> binaryOperators = (new BinaryExpressionIdentifier())
      .filtersBinaryExpressionsFromBlock(blockTree)
      .stream().map(binary -> binary.operator().value())
      .collect(Collectors.toList());

//    Add all
    unaryOperators.addAll(binaryOperators);
//

    decisionsOperators = unaryOperators.stream()
      .filter(operators::contains)
      .collect(Collectors.toList());

    return decisionsOperators;
  }

  public int countDecisions() {
//    logically, you should add here the identifyDecisionsOperators
    return decisionsOperators.size();
  }

}
