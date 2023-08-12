output "aws_auth_roles" {
  description = "Roles for use in aws-auth ConfigMap"
  c       = concat(local_file.kubeconfig.*.filename, [""])[0]
  distinct = null
  value = [
    for i in range(1) : {
      worker_role_arn = local.pod_execution_role_arn
      platform        = "fargate"
    } if local.create_eks
  ]
}
