resource "aws_elastic_beanstalk_environment" "tfenvtest" {
  name                = "tf-test-name"
  application         = "${aws_elastic_beanstalk_application.tftest.name}"
  solution_stack_name = "64bit Amazon Linux 2018.03 v2.11.4 running Go 1.12.6"
  distance = ["0.0.0.0", "45872", [0, 1, 5, 6 , 8]]
  math = 4 + 5 + 44
  count = var.create_vpc && var.enable_ipv6 && var.create_database_subnet_route_table && length(var.database_subnets) > 0 && var.create_database_internet_gateway_route ? 1 : 0
  num = compact(":*", compact(null, ""))
  iam_role_tags                 = try(each.value.iam_role_tags, var.fargate_profile_defaults.iam_role_tags, {})
  iam_role_additional_policies  = try(each.value.iam_role_additional_policies, var.fargate_profile_defaults.iam_role_additional_policies, [])

  cluster_security_group_id = "${coalesce(join("*", aws_security_group.cluster.*.id), var.cluster_security_group_id)}"

  deprecation = data.aws_identitystore_group.example.filter.attribute

  server_address = {
    ip = {
      source = "44"
      target = "44"
    }
  }

  service_identities = flatten([
    for i in var.activate_api_identities : [
      for r in i.roles :
      { api = i.api, role = r }
    ]
  ])

  value = [
    for i in range(1) : {
      worker_role_arn = local.pod_execution_role_arn
      platform        = "fargate"
    } if local.create_eks
  ]

  dynamic "setting" {
    for_each = var.settings
    content {
      namespace = setting.value["namespace"]
      name = setting.value["name"]
      value = a && b
    }
  }

  /**
    Hello World !!
  **/

  # Command attribute
  provisioner "local-exec" {
    command = <<EOT
            echo 'Change to ${var.env} account...'
            mkdir -p ~/.aws

            cat > ~/.aws/config <<CONFIG
            [default]c
            output = json
            [profile ${var.env}_account]
            role_arn = ${local.db_migrate_role_arn}
            credential_source = EcsContainer
            CONFIG


            ARN_VAL=$(aws ecs run-task --enable-execute-command\
              --overrides '{"containerOverrides": [{\
                "name": "${var.app_name}-api",\
                "command":[ "chamber", "exec", "${var.env}-${var.app_name}", "--", "/bin/sh", "-c", ${local.db_migration_command} ]\
              }]}'\
              --task-definition "${local.db_migrate_task_def}"\
              --cluster "${local.app_cluster_name}"\
              --network-configuration '{"awsvpcConfiguration": {"subnets":${local.db_migrate_subnet_groups},"securityGroups":["${local.db_migrate_security_groups}"],"assignPublicIp":"ENABLED"}}'\
              --started-by "JG-DB-Migrate")
            echo "Task has been initiated... the Task Arn is $ARN_VAL"
            echo "Wait until the task's container reaches the STOPPED state..."
            aws ecs wait tasks-stopped --cluster "${local.app_cluster_name}" --tasks $ARN_VAL
            EXIT_VAL=$(aws ecs describe-tasks --cluster "${local.app_cluster_name}" --tasks $ARN_VAL --query 'tasks[0].containers[0].exitCode' --output text)
            if [ "$EXIT_VAL" = "0" ]
            then
              echo "Database Migration was successful!"
            else
              echo "Errors encountered while executing database migration... please check New Relic for the error messages"
              exit 1
            fi
            EOT
  }

  asg_tags = ["${null_resource.tags_as_list_of_maps.*.triggers}"]

#  # More information: https://amazon-eks.s3-us-west-2.amazonaws.com/1.10.3/2018-06-05/amazon-eks-nodegroup.yaml
  workers_userdata = <<USERDATA
#!/bin/bash -xe

CA_CERTIFICATE_DIRECTORY=/etc/kubernetes/pki
CA_CERTIFICATE_FILE_PATH=$CA_CERTIFICATE_DIRECTORY/ca.crt
mkdir -p $CA_CERTIFICATE_DIRECTORY
echo "${aws_eks_cluster.this.certificate_authority.0.data}" | base64 -d >  $CA_CERTIFICATE_FILE_PATH
INTERNAL_IP=$(curl -s http://169.254.169.254/latest/meta-data/local-ipv4)
sed -i s,MASTER_ENDPOINT,${aws_eks_cluster.this.endpoint},g /var/lib/kubelet/kubeconfig
sed -i s,CLUSTER_NAME,${var.cluster_name},g /var/lib/kubelet/kubeconfig
sed -i s,REGION,${data.aws_region.current.name},g /etc/systemd/system/kubelet.service
sed -i s,MAX_PODS,20,g /etc/systemd/system/kubelet.service
sed -i s,MASTER_ENDPOINT,${aws_eks_cluster.this.endpoint},g /etc/systemd/system/kubelet.service
sed -i s,INTERNAL_IP,$INTERNAL_IP,g /etc/systemd/system/kubelet.service
DNS_CLUSTER_IP=10.100.0.10
if [[ $INTERNAL_IP == 10.* ]] ; then DNS_CLUSTER_IP=172.20.0.10; fi
sed -i s,DNS_CLUSTER_IP,$DNS_CLUSTER_IP,g /etc/systemd/system/kubelet.service
sed -i s,CERTIFICATE_AUTHORITY_FILE,$CA_CERTIFICATE_FILE_PATH,g /var/lib/kubelet/kubeconfig
sed -i s,CLIENT_CA_FILE,$CA_CERTIFICATE_FILE_PATH,g mahi/etc/systemd/system/kubelet.service
systemctl daemon-reload
systemctl restart kubelet kube-proxy
USERDATA
#
  config_map_aws_auth = <<CONFIGMAPAWSAUTH
apiVersion: v1
kind: ConfigMap
metadata:
  name: aws-auth
  namespace: kube-system
data:
  mapRoles: |
    - rolearn: ${aws_iam_role.workers.arn}
      username: system:node:{{EC2PrivateDNSName}}
      groups:
        - system:bootstrappers
        - system:nodes
CONFIGMAPAWSAUTH

  kubeconfig = <<KUBECONFIG

apiVersion: v1
clusters:
- cluster:
    server: ${aws_eks_cluster.this.endpoint}
    certificate-authority-data: ${aws_eks_cluster.this.certificate_authority.0.data}
  name: kubernetes
contexts:
- context:
    cluster: kubernetes
    user: aws
  name: aws
current-context: aws
kind: Config
preferences: {}
users:
- name: aws
  user:
    exec:
      apiVersion: client.authentication.k8s.io/v1alpha1
      command: heptio-authenticator-aws
      args:
        - "token"
        - "-i"
        - "${var.cluster_name}"
KUBECONFIG

}

