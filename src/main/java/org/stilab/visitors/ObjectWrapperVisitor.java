package org.stilab.visitors;

import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.api.tree.ExpressionTree;
import org.sonar.iac.terraform.tree.impl.*;
import org.stilab.parser.spliters.ExpressionAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ObjectWrapperVisitor {

    List<TerraformTreeImpl> objects = new ArrayList<>();
    List<AttributeTreeImpl> attributes = new ArrayList<>();

    public List<AttributeTreeImpl> getAttributes() {
      return attributes;
    }

    public List<TerraformTreeImpl> getObjects() {
      return objects;
    }

    public List<TerraformTreeImpl> visit(AttributeTreeImpl attributeTree) {

        ExpressionTree expressionTree = attributeTree.value();

        List<Tree> trees = ExpressionAnalyzer.getInstance().getAllNestedExpressions(expressionTree);

        Stream<TerraformTreeImpl> localObjects = trees
          .stream()
          .filter(ObjectTreeImpl.class::isInstance)
          .map(TerraformTreeImpl.class::cast);

        Stream<TerraformTreeImpl> forObjects = trees
          .stream()
          .filter(ForObjectTreeImpl.class::isInstance)
          .map(TerraformTreeImpl.class::cast);

        Stream<TerraformTreeImpl> combinedFilters = Stream.concat(localObjects, forObjects);

        return combinedFilters.collect(Collectors.toList());
    }

      public List<TerraformTreeImpl> filterObjectsFromAttributesList(List<AttributeTreeImpl> attributeTrees) {
        List<TerraformTreeImpl> attributeAccessTrees = new ArrayList<>();
        for(AttributeTreeImpl attributeAccess: attributeTrees) {
          attributeAccessTrees.addAll( this.visit(attributeAccess) );
        }
        return attributeAccessTrees;
      }

      public List<TerraformTreeImpl> filterObjectsFromBlock(BlockTreeImpl blockTree) {
        attributes = (new AttrFinderImpl()).getAllAttributes(blockTree);
        objects = this.filterObjectsFromAttributesList(attributes);
        return objects;
      }



}
