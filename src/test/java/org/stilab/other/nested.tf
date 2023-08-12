locals {
  ebs_optimized = {
    "c1.medium"    = false
    "c1.xlarge"    = true
    "c3.large"     = false
    "c3.xlarge"    = true
    "c3.2xlarge"   = true
    "c3.4xlarge"   = true
  }

  default = {
    name                 = "count.index" # Name of the worker group. Literal count.index will never be used but if name is not set, the count.index interpolation will be used.
    ami_id               = ""            # AMI ID for the eks workers. If none is provided, Terraform will search for the latest version of their EKS optimized worker AMI.
    asg_desired_capacity = "1"           # Desired worker capacity in the autoscaling group.
    asg_max_size         = "3"           # Maximum worker capacity in the autoscaling group.
    asg_min_size         = "1"           # Minimum worker capacity in the autoscaling group.
    instance_type        = "m4.large"    # Size of the workers instances.
    additional_userdata  = ""            # userdata to append to the default userdata.
    ebs_optimized        = true          # sets whether to use ebs optimization on supported types.
  }

  iam_role_tags                 = try(each.value.iam_role_tags, var.fargate_profile_defaults.iam_role_tags, {})
  iam_role_additional_policies  = try(each.value.iam_role_additional_policies, var.fargate_profile_defaults.iam_role_additional_policies, [])

  from_port   = 443
  to_port     = 443
}
