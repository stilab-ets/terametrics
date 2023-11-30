package org.stilab.calculators;

import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.MetaArgumentVisitor;

public class MetaArgumentCalculator {


    MetaArgumentVisitor metaArgumentVisitor;

    public MetaArgumentCalculator(BlockTreeImpl identifiedBlock){
      metaArgumentVisitor = new MetaArgumentVisitor();
      metaArgumentVisitor.filterMetaArguments(identifiedBlock);
    }

    public int countMetaArgs(){
      return metaArgumentVisitor.metaArgsCount();
    }

}
