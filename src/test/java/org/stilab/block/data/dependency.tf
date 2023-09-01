locals {
  iam_role_name = 'iam-web-server'
}

variable "iam_role_use_name_prefix" {
  default = "server"
}
variable "prefix_separator" {
  default = "prefix-server"
}
variable "iam_role_permissions_boundary" {
  default = ""
}

data "aws_iam_policy_document" "aws_iam_policy_document" {
  assume_role_policy = []
}

variable "create_cloudwatch_log_group" {
  default = ""
}


resource "aws_iam_role" "this" {
  count = local.create_iam_role ? 1 : 0
  name        = var.iam_role_use_name_prefix ? null : local.iam_role_name
  name_prefix = var.iam_role_use_name_prefix ? "${local.iam_role_name}${var.prefix_separator}" : null
  path        = var.iam_role_path
  description = var.iam_role_description
  assume_role_policy    = data.aws_iam_policy_document.assume_role_policy[0].json
  permissions_boundary  = var.iam_role_permissions_boundary
  force_detach_policies = true
  # https://github.com/terraform-aws-modules/terraform-aws-eks/issues/920
  # Resources running on the cluster are still generaring logs when destroying the module resources
  # which results in the log group being re-created even after Terraform destroys it. Removing the
  # ability for the cluster role to create the log group prevents this log group from being re-created
  # outside of Terraform due to services still generating logs during destroy process
  dynamic "inline_policy" {
    for_each = var.create_cloudwatch_log_group ? [1] : []
    content {
      name = local.iam_role_name
      policy = jsonencode({
        Version = "2012-10-17"
        Statement = [
          {
            Action   = ["logs:CreateLogGroup"]
            Effect   = "Deny"
            Resource = aws_cloudwatch_log_group.this[0].arn
          },
        ]
      })

    }
  }
  tags = merge(var.tags, var.iam_role_tags)
}
