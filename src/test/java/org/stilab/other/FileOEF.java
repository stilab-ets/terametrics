package org.stilab.other;

import junit.framework.TestCase;
import org.sonar.iac.common.api.tree.Tree;
import org.sonar.iac.terraform.parser.HclParser;
import org.sonar.iac.terraform.tree.impl.AttributeTreeImpl;
import org.sonar.iac.terraform.tree.impl.BlockTreeImpl;
import org.sonar.iac.terraform.tree.impl.LiteralExprTreeImpl;
import org.stilab.metrics.counter.attr.finder.AttrFinderImpl;
import org.stilab.metrics.counter.block_level.HereDocIdentifier;
import org.stilab.metrics.counter.block_level.LiteralExpressionHereDocIdentifier;
import org.stilab.metrics.counter.block_level.LiteralExpressionIdentifier;
import org.stilab.utils.ExpressionAnalyzer;

import java.io.File;
import java.util.List;

public class FileOEF extends TestCase {

  public void testFileOEF() {
    HclParser hclParser = new HclParser();
    File file = new File("C:\\Users\\Admin\\dev\\sonar-iac\\iac-extensions" +
      "\\terraform_miner\\src\\test\\java\\org\\stilab\\test.tf");
    Tree tree = hclParser.parse(file);
    BlockTreeImpl blockTree = (BlockTreeImpl) tree.children().get(0);

    AttrFinderImpl attrFinder = new AttrFinderImpl();


    for (AttributeTreeImpl attributeTree : attrFinder.getAllAttributes(blockTree)) {
      for (Tree expressionTree :ExpressionAnalyzer.getInstance().getAllNestedExpressions(attributeTree)) {
        System.out.println(expressionTree.getClass());
      }
    }
  }

  public void testHeredocAnalyzer() {
    // Example file content containing heredoc delimiters
    String fileContent = "locals {\n" +
      "  asg_tags = [\"${null_resource.tags_as_list_of_maps.*.triggers}\"]\n" +
      "\n" +
      "  # More information: https://amazon-eks.s3-us-west-2.amazonaws.com/1.10.3/2018-06-05/amazon-eks-nodegroup.yaml\n" +
      "  workers_userdata = <<USERDATA\n" +
      "#!/bin/bash -xe\n" +
      "\n" +
      "CA_CERTIFICATE_DIRECTORY=/etc/kubernetes/pki\n" +
      "CA_CERTIFICATE_FILE_PATH=$CA_CERTIFICATE_DIRECTORY/ca.crt\n" +
      "mkdir -p $CA_CERTIFICATE_DIRECTORY\n" +
      "echo \"${aws_eks_cluster.this.certificate_authority.0.data}\" | base64 -d >  $CA_CERTIFICATE_FILE_PATH\n" +
      "INTERNAL_IP=$(curl -s http://169.254.169.254/latest/meta-data/local-ipv4)\n" +
      "sed -i s,MASTER_ENDPOINT,${aws_eks_cluster.this.endpoint},g /var/lib/kubelet/kubeconfig\n" +
      "sed -i s,CLUSTER_NAME,${var.cluster_name},g /var/lib/kubelet/kubeconfig\n" +
      "sed -i s,REGION,${data.aws_region.current.name},g /etc/systemd/system/kubelet.service\n" +
      "sed -i s,MAX_PODS,20,g /etc/systemd/system/kubelet.service\n" +
      "sed -i s,MASTER_ENDPOINT,${aws_eks_cluster.this.endpoint},g /etc/systemd/system/kubelet.service\n" +
      "sed -i s,INTERNAL_IP,$INTERNAL_IP,g /etc/systemd/system/kubelet.service\n" +
      "DNS_CLUSTER_IP=10.100.0.10\n" +
      "if [[ $INTERNAL_IP == 10.* ]] ; then DNS_CLUSTER_IP=172.20.0.10; fi\n" +
      "sed -i s,DNS_CLUSTER_IP,$DNS_CLUSTER_IP,g /etc/systemd/system/kubelet.service\n" +
      "sed -i s,CERTIFICATE_AUTHORITY_FILE,$CA_CERTIFICATE_FILE_PATH,g /var/lib/kubelet/kubeconfig\n" +
      "sed -i s,CLIENT_CA_FILE,$CA_CERTIFICATE_FILE_PATH,g mahi/etc/systemd/system/kubelet.service\n" +
      "systemctl daemon-reload\n" +
      "systemctl restart kubelet kube-proxy\n" +
      "USERDATA\n" +
      "\n" +
      "  config_map_aws_auth = <<CONFIGMAPAWSAUTH\n" +
      "apiVersion: v1\n" +
      "kind: ConfigMap\n" +
      "metadata:\n" +
      "  name: aws-auth\n" +
      "  namespace: kube-system\n" +
      "data:\n" +
      "  mapRoles: |\n" +
      "    - rolearn: ${aws_iam_role.workers.arn}\n" +
      "      username: system:node:{{EC2PrivateDNSName}}\n" +
      "      groups:\n" +
      "        - system:bootstrappers\n" +
      "        - system:nodes\n" +
      "CONFIGMAPAWSAUTH\n" +
      "\n" +
      "  kubeconfig = <<KUBECONFIG\n" +
      "\n" +
      "apiVersion: v1\n" +
      "clusters:\n" +
      "- cluster:\n" +
      "    server: ${aws_eks_cluster.this.endpoint}\n" +
      "    certificate-authority-data: ${aws_eks_cluster.this.certificate_authority.0.data}\n" +
      "  name: kubernetes\n" +
      "contexts:\n" +
      "- context:\n" +
      "    cluster: kubernetes\n" +
      "    user: aws\n" +
      "  name: aws\n" +
      "current-context: aws\n" +
      "kind: Config\n" +
      "preferences: {}\n" +
      "users:\n" +
      "- name: aws\n" +
      "  user:\n" +
      "    exec:\n" +
      "      apiVersion: client.authentication.k8s.io/v1alpha1\n" +
      "      command: heptio-authenticator-aws\n" +
      "      args:\n" +
      "        - \"token\"\n" +
      "        - \"-i\"\n" +
      "        - \"${var.cluster_name}\"\n" +
      "KUBECONFIG\n" +
      "}";

    // Call Hcl Parser
    HclParser hclParser = new HclParser();

    // Parse the Content Of The HCL FILE
    Tree tree = hclParser.parse(fileContent);

    // Identify the first Block
    BlockTreeImpl blockTree = (BlockTreeImpl) tree.children().get(0);

    // Identify All the literal Expressions within the block
    LiteralExpressionIdentifier literalExpressionIdentifier = new LiteralExpressionIdentifier();

    // For each Literal Expression, check if there is a
    List<LiteralExprTreeImpl> literalExprTrees = literalExpressionIdentifier.filterLiteralExprFromBlock(blockTree);

    LiteralExpressionHereDocIdentifier literalExpressionHereDocIdentifier = new LiteralExpressionHereDocIdentifier();

    // Count the number of HereDoc-Per-Block
    assertEquals(literalExpressionHereDocIdentifier.totalNumberOfHereDoc(literalExprTrees), 3);

    // Count the Total number of lines of HereDoc
    assertEquals(literalExpressionHereDocIdentifier.totalLinesOfHereDoc(literalExprTrees), 63);

    // Count the Avg Size
    assertEquals(literalExpressionHereDocIdentifier.avgNumberLinesPerHereDoc(literalExprTrees), 21.0);

    // Count the Max Size
    assertEquals(literalExpressionHereDocIdentifier.maxNumberLinesPerHereDoc(literalExprTrees), 27);

  }

  public void testHeredocIdentifier() {
    // Example file content containing heredoc delimiters
    String fileContent = "locals {\n" +
      "  asg_tags = [\"${null_resource.tags_as_list_of_maps.*.triggers}\"]\n" +
      "\n" +
      "  # More information: https://amazon-eks.s3-us-west-2.amazonaws.com/1.10.3/2018-06-05/amazon-eks-nodegroup.yaml\n" +
      "  workers_userdata = <<USERDATA\n" +
      "#!/bin/bash -xe\n" +
      "\n" +
      "CA_CERTIFICATE_DIRECTORY=/etc/kubernetes/pki\n" +
      "CA_CERTIFICATE_FILE_PATH=$CA_CERTIFICATE_DIRECTORY/ca.crt\n" +
      "mkdir -p $CA_CERTIFICATE_DIRECTORY\n" +
      "echo \"${aws_eks_cluster.this.certificate_authority.0.data}\" | base64 -d >  $CA_CERTIFICATE_FILE_PATH\n" +
      "INTERNAL_IP=$(curl -s http://169.254.169.254/latest/meta-data/local-ipv4)\n" +
      "sed -i s,MASTER_ENDPOINT,${aws_eks_cluster.this.endpoint},g /var/lib/kubelet/kubeconfig\n" +
      "sed -i s,CLUSTER_NAME,${var.cluster_name},g /var/lib/kubelet/kubeconfig\n" +
      "sed -i s,REGION,${data.aws_region.current.name},g /etc/systemd/system/kubelet.service\n" +
      "sed -i s,MAX_PODS,20,g /etc/systemd/system/kubelet.service\n" +
      "sed -i s,MASTER_ENDPOINT,${aws_eks_cluster.this.endpoint},g /etc/systemd/system/kubelet.service\n" +
      "sed -i s,INTERNAL_IP,$INTERNAL_IP,g /etc/systemd/system/kubelet.service\n" +
      "DNS_CLUSTER_IP=10.100.0.10\n" +
      "if [[ $INTERNAL_IP == 10.* ]] ; then DNS_CLUSTER_IP=172.20.0.10; fi\n" +
      "sed -i s,DNS_CLUSTER_IP,$DNS_CLUSTER_IP,g /etc/systemd/system/kubelet.service\n" +
      "sed -i s,CERTIFICATE_AUTHORITY_FILE,$CA_CERTIFICATE_FILE_PATH,g /var/lib/kubelet/kubeconfig\n" +
      "sed -i s,CLIENT_CA_FILE,$CA_CERTIFICATE_FILE_PATH,g mahi/etc/systemd/system/kubelet.service\n" +
      "systemctl daemon-reload\n" +
      "systemctl restart kubelet kube-proxy\n" +
      "USERDATA\n" +
      "\n" +
      "  config_map_aws_auth = <<CONFIGMAPAWSAUTH\n" +
      "apiVersion: v1\n" +
      "kind: ConfigMap\n" +
      "metadata:\n" +
      "  name: aws-auth\n" +
      "  namespace: kube-system\n" +
      "data:\n" +
      "  mapRoles: |\n" +
      "    - rolearn: ${aws_iam_role.workers.arn}\n" +
      "      username: system:node:{{EC2PrivateDNSName}}\n" +
      "      groups:\n" +
      "        - system:bootstrappers\n" +
      "        - system:nodes\n" +
      "CONFIGMAPAWSAUTH\n" +
      "\n" +
      "  kubeconfig = <<KUBECONFIG\n" +
      "\n" +
      "apiVersion: v1\n" +
      "clusters:\n" +
      "- cluster:\n" +
      "    server: ${aws_eks_cluster.this.endpoint}\n" +
      "    certificate-authority-data: ${aws_eks_cluster.this.certificate_authority.0.data}\n" +
      "  name: kubernetes\n" +
      "contexts:\n" +
      "- context:\n" +
      "    cluster: kubernetes\n" +
      "    user: aws\n" +
      "  name: aws\n" +
      "current-context: aws\n" +
      "kind: Config\n" +
      "preferences: {}\n" +
      "users:\n" +
      "- name: aws\n" +
      "  user:\n" +
      "    exec:\n" +
      "      apiVersion: client.authentication.k8s.io/v1alpha1\n" +
      "      command: heptio-authenticator-aws\n" +
      "      args:\n" +
      "        - \"token\"\n" +
      "        - \"-i\"\n" +
      "        - \"${var.cluster_name}\"\n" +
      "KUBECONFIG\n" +
      "}";

    // Call Hcl Parser
    HclParser hclParser = new HclParser();

    // Parse the Content Of The HCL FILE
    Tree tree = hclParser.parse(fileContent);

    // Identify the first Block
    BlockTreeImpl blockTree = (BlockTreeImpl) tree.children().get(0);

    HereDocIdentifier hereDocIdentifier = new HereDocIdentifier();
    hereDocIdentifier.filterHereDocsFromBlock(blockTree);

    assertEquals(hereDocIdentifier.totalNumberOfHereDoc(), 3);
    assertEquals(hereDocIdentifier.avgNumberLinesPerHereDoc(), 21.0);
    assertEquals(hereDocIdentifier.maxNumberLinesPerHereDoc(), 27);
  }

}
