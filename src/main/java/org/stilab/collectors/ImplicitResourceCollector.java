package org.stilab.collectors;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.stilab.visitors.ImplicitResourceDependencyVisitor;
import org.stilab.visitors.VariablesVisitor;

public class ImplicitResourceCollector implements Decorator {


    @Override
    public JSONObject decorateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {
      ImplicitResourceDependencyVisitor implicitResourceRepository = new ImplicitResourceDependencyVisitor();

      VariablesVisitor variablesVisitor = new VariablesVisitor();


      implicitResourceRepository.setVariableExprTrees(
        variablesVisitor.filterVarsFromBlock(identifiedBlock)
      );

      int numDependentResources = implicitResourceRepository.numOfInvokedResources();
      int numDependentData = implicitResourceRepository.numOfInvokedData();
      int numDependentModules = implicitResourceRepository.numOfInvokedModules();
      int numDependentProviders = implicitResourceRepository.numOfInvokedProviders();
      int numDependentLocals = implicitResourceRepository.numOfInvokedLocals();
      int numDependentVars = implicitResourceRepository.numOfInvokedVars();
      int numDependentEach = implicitResourceRepository.numOfInvokedEach();

      metrics.put("numImplicitDependentResources", numDependentResources);
      metrics.put("numImplicitDependentData", numDependentData);
      metrics.put("numImplicitDependentModules", numDependentModules);
      metrics.put("numImplicitDependentProviders", numDependentProviders);
      metrics.put("numImplicitDependentLocals", numDependentLocals);
      metrics.put("numImplicitDependentVars", numDependentVars);
      metrics.put("numImplicitDependentEach", numDependentEach);

      return metrics;
    }




}
