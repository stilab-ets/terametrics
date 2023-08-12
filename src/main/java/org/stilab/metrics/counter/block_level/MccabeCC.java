package org.stilab.metrics.counter.block_level;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.tree.impl.SyntaxTokenImpl;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.stilab.utils.ExpressionAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MccabeCC {

  public int measureMccabeCCForAnAttributes(AttributeTreeImpl attributeTree) {

    List<TerraformTreeImpl> complexity = new ArrayList<>();

    // We Count the number of conditions
    List<TerraformTreeImpl> binaryConditions =
      (new ConditionalExpressionIdentifier())
      .filterConditions(attributeTree);

    // Add the conditions
    complexity.addAll(binaryConditions);

    // We count the number of loops
    List<TerraformTreeImpl> loops = (new LoopsExpressionIdentifier()).filterLoops(attributeTree);

    // Add the loops
    complexity.addAll(loops);

    // Ad-hoc method to identify all the methods
    List<SyntaxTokenImpl> syntaxTokens = this.identifyTokens(attributeTree);

    return this.measureComplexityDiff(syntaxTokens, complexity);
  }

  public int measureComplexityDiff(List<SyntaxTokenImpl> syntaxTokens, List<TerraformTreeImpl> complexity){
    int d = Math.abs(syntaxTokens.size() - complexity.size());
    if (d == 0) {
      return complexity.size() + 1;
    } else {
      return complexity.size() + d + 1;
    }
  }

  public List<SyntaxTokenImpl> identifyTokens(AttributeTreeImpl attributeTree) {
    // Particular Case: value = [
    //    for i in range(1) : {
    //      worker_role_arn = local.pod_execution_role_arn
    //      platform        = "fargate"
    //    } if local.create_eks
    //  ]
    List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(attributeTree.value());

    List<SyntaxTokenImpl> syntaxTokens = trees.stream()
      .filter(tree -> tree instanceof SyntaxTokenImpl)
      .map(tree -> (SyntaxTokenImpl) tree)
      .filter(token -> token.value().equals("if") || token.value().equals("for") )
      .collect(Collectors.toList());

    return syntaxTokens;
  }

  public double avgMccabeCC(BlockTreeImpl blockTree) {

    List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl())
      .getAllAttributes(blockTree);
    List<Integer> localMcc = new ArrayList<>();
    int right = 0;
    for (AttributeTreeImpl attribute: attributeTrees) {
      if (measureMccabeCCForAnAttributes(attribute) != 0) {
        right = right + 1;
        localMcc.add( measureMccabeCCForAnAttributes(attribute) );
      }
    }
    if (right == 0) {
      return 0;
    }
    return (double) localMcc.stream().mapToInt(Integer::intValue).sum() / (double) right;
  }

  public double sumMccabeCC(BlockTreeImpl blockTree) {

    List<AttributeTreeImpl> attributeTrees = (new AttrFinderImpl())
      .getAllAttributes(blockTree);
    List<Integer> localMcc = new ArrayList<>();
    for (AttributeTreeImpl attribute: attributeTrees) {
      if (measureMccabeCCForAnAttributes(attribute) != 0) {
        localMcc.add( measureMccabeCCForAnAttributes(attribute) );
      }
    }

    return (double) localMcc.stream().mapToInt(Integer::intValue).sum();

  }

}
