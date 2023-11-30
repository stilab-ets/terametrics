package org.stilab.visitors;

import org.stilab.parser.spliters.ExpressionAnalyzer;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.FunctionCallTreeImpl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionCallExpressionVisitor {

    private List<FunctionCallTreeImpl> functionsCallPerBlock = new ArrayList<>();
    private List<AttributeTreeImpl> attributes = new ArrayList<>();

    public List<AttributeTreeImpl> getAttributes() {
      return attributes;
    }

    public List<FunctionCallTreeImpl> getFunctionsCallPerBlock() {
      return functionsCallPerBlock;
    }

    public List<FunctionCallTreeImpl> visit(AttributeTreeImpl attributeTree) {
        ExpressionTree expressionTree = attributeTree.value();
        List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);
        return trees.stream()
                    .filter(FunctionCallTreeImpl.class::isInstance)
                    .map(FunctionCallTreeImpl.class::cast)
                    .collect(Collectors.toList());
    }

    public List<FunctionCallTreeImpl> filterFCfromAttributesList(List<AttributeTreeImpl> attributeTrees) {
        List<FunctionCallTreeImpl> functionCallTrees = new ArrayList<>();
        for (AttributeTreeImpl attributeTree: attributeTrees) {
          functionCallTrees.addAll( this.visit(attributeTree) );
        }
        return functionCallTrees;
    }

  //    root blocks
    public List<FunctionCallTreeImpl> filterFCfromBlock(BlockTreeImpl blockTree) {
         attributes =  (new AttrFinderImpl())
          .getAllAttributes(blockTree);
        functionsCallPerBlock = this.filterFCfromAttributesList(attributes);
        return functionsCallPerBlock;
    }

}
