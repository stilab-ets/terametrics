package org.stilab.metrics.visitors;

import org.sonar.iac.terraform.api.tree.SyntaxToken;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.TerraformTreeImpl;
import org.stilab.metrics.iterators.AttrFinderImpl;

import java.util.List;

public class ExplicitResourceDependencyVisitor {

    public int numberOfDependentResources(List<AttributeTreeImpl> attributes) {

      for (AttributeTreeImpl attribute: attributes) {
        SyntaxToken token = attribute.key();
        if (token.value().equals("depends_on")) {
          TupleVisitor tupleVisitor = new TupleVisitor();
          List<TerraformTreeImpl> tuples = tupleVisitor.visit(attribute);
          TupleElementsVisitor tupleIdentifiers = new TupleElementsVisitor(tuples);
          return tupleIdentifiers.getTotalNumberOfElementsOfDifferentTuples();
        }
      }
      return 0;
    }

    public int getNumberOfResourceDependency(BlockTreeImpl blockTree){
      return numberOfDependentResources((new AttrFinderImpl()).getAllAttributes(blockTree));
    }

}
