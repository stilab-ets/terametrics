package org.stilab.visitors;

import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MccabeCC {

    AttrFinderImpl attributeFinder = new AttrFinderImpl();

    List<AttributeTreeImpl> attributes;

    public int measureMccabeCCForAnAttributes(AttributeTreeImpl attributeTree) {

      List<TerraformTreeImpl> complexity = new ArrayList<>();

      // Get Number of Conditions
      ConditionalExpressionVisitor conditionalExpressionVisitor = new ConditionalExpressionVisitor();
      List<TerraformTreeImpl> conditions =  conditionalExpressionVisitor.visit(attributeTree);
      complexity.addAll(conditions);

      // Get Number of Loops
      LoopsExpressionVisitor loopsExpressionVisitor = new LoopsExpressionVisitor();
      List<TerraformTreeImpl> loops = loopsExpressionVisitor.visit(attributeTree);
      complexity.addAll(loops);

      // check if the attribute is 'foreach', 'count'


      return complexity.size() + 1;
    }

    public List<AttributeTreeImpl> setAttributes(BlockTreeImpl blockTree) {
      attributes = this.attributeFinder.getAllAttributes(blockTree);
      return attributes;
    }

    public List<AttributeTreeImpl> getAttributes() {
      return attributes;
    }




}
