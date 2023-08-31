package org.stilab.metrics.counter.block_level.block_dependency;

import org.json.simple.JSONObject;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.VariableExprTreeImpl;
import org.stilab.metrics.counter.block_level.VariablesIdentifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ImplicitResourceDependency {

    private List<VariableExprTreeImpl> vars;

    private List<String> providerPrefixes = new ArrayList<>(
      Arrays.asList("aws", "google", "azurerm", "kubernetes", "oci", "alicloud", "google-beta", "archive", "helm", "null",
        "nomad", "random", "template", "tls", "http", "time", "waypoint", "vsphere", "vault", "tfe", "terraform", "salesforce",
        "oraclepaas", "opc", "hcs", "hcp", "googleworkspace", "external", "dns", "consul", "cloudinit", "boundary", "azurestack",
        "azuread", "awscc", "ad", "kubectl")
    );

    public ImplicitResourceDependency() {}

    public int numOfInvokedVars(){
        List<VariableExprTreeImpl> variablesConfigDependency = new ArrayList<>();
        for (VariableExprTreeImpl var: vars) {
            if (var.name().equals("var")) {
              variablesConfigDependency.add(var);
            }
        }
        return variablesConfigDependency.size();
    }

    public int numOfInvokedLocals(){
      List<VariableExprTreeImpl> localsConfigDependency = new ArrayList<>();
      for (VariableExprTreeImpl local: vars) {
        if (local.name().equals("local")) {
          localsConfigDependency.add(local);
        }
      }
      return localsConfigDependency.size();
    }

    public int numOfInvokedProviders(){
      List<VariableExprTreeImpl> providerConfigDependency = new ArrayList<>();
      for (VariableExprTreeImpl provider: vars) {
        if (provider.name().equals("provider") || providerPrefixes.contains(provider.name())) {
          System.out.println(provider.name());
          providerConfigDependency.add(provider);
        }
      }
      return providerConfigDependency.size();
    }

    public int numOfInvokedModules(){
      List<VariableExprTreeImpl> moduleConfigDependency = new ArrayList<>();
      for (VariableExprTreeImpl local: vars) {
        if (local.name().equals("module")) {
          moduleConfigDependency.add(local);
        }
      }
      return moduleConfigDependency.size();
    }

    public int numOfInvokedData() {
      List<VariableExprTreeImpl> dataConfigDependency = new ArrayList<>();
      for (VariableExprTreeImpl local: vars) {
        if (local.name().equals("data")) {
          dataConfigDependency.add(local);
        }
      }
      return dataConfigDependency.size();
    }

    public int numOfInvokedResources(){
      return vars.stream()
        .filter(resource -> providerPrefixes.stream()
          .anyMatch(prefix -> resource.name().startsWith(prefix + "_")))
        .collect(Collectors.toList())
        .size();
    }

    public JSONObject updateMetric(JSONObject metrics, BlockTreeImpl identifiedBlock) {

      VariablesIdentifier variablesIdentifier = new VariablesIdentifier();
      this.vars = variablesIdentifier.filterVarsFromBlock(identifiedBlock);

      int numDependentResources = this.numOfInvokedResources();
      int numDependentData = this.numOfInvokedData();
      int numDependentModules = this.numOfInvokedModules();
      int numDependentProviders = this.numOfInvokedProviders();
      int numDependentLocals = this.numOfInvokedLocals();
      int numDependentVars = this.numOfInvokedVars();

      metrics.put("numImplicitDependentResources", numDependentResources);
      metrics.put("numImplicitDependentData", numDependentData);
      metrics.put("numImplicitDependentModules", numDependentModules);
      metrics.put("numImplicitDependentProviders", numDependentProviders);
      metrics.put("numImplicitDependentLocals", numDependentLocals);
      metrics.put("numImplicitDependentVars", numDependentVars);

      return metrics;
    }


}
