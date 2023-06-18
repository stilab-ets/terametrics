locals {
  name   = "ex-${basename(path.cwd)}"
  region = "eu-west-1"
  mahi   = "eu-west-1"
  vpc_cidr = "10.0.0.0/16"
  for_each = var.settings
  azs      = slice(data.aws_availability_zones.available.names, 0, 3)
  tags = {
    Example    = {
      obj = local.name
      k   = var.settings
    }
    GithubRepo = "terraform-aws-vpc"
    GithubOrg  = "terraform-aws-modules"
    Githubtst  = "terraform-aws-modules"
  }
}

resource "aws_elastic_beanstalk_environment" "tfenvtest" {
  name                = "tf-test-name"
  application         = "${aws_elastic_beanstalk_application.tftest.name}"
  solution_stack_name = "64bit Amazon Linux 2018.03 v2.11.4 running Go 1.12.6"

  dynamic "setting" {
    for_each = var.settings
    content {
      namespace = setting.value["namespace"]
      name = setting.value["name"]
      value = setting.value["value"]
    }
  }
}

locals {}

module "vpc" {
#  source = "../../"

  count = local.create_vpc && var.enable_dhcp_options ? 1 : 0
  test = "Hello, %{ if var.name != "" }${var.name}%{ else }unnamed%{ endif }!"
  mahi = 58
  name = local.name
  cidr = local.vpc_cidr



  azs             = local.azs
  private_subnets = [for k, v in local.azs : cidrsubnet(local.vpc_cidr, 4, k)]

  tags = local.tags

  tags = merge(
    {
      Name = try(
        var.public_subnet_names[count.index],
        format("${var.name}-${var.public_subnet_suffix}-%s", element(var.azs, count.index))
      )
    },
    var.tags,
    var.public_subnet_tags,
    lookup(var.public_subnet_tags_per_az, element(var.azs, count.index), {})
  )

  route_table_id = element(aws_route_table.database[*].id, count.index)

}

variable "users" {
  type = map(object({
    is_admin = bool
  }))
}

locals {
  admin_users = {
    for name, user in var.users : name => user
    if user.is_admin
  }
  regular_users = {
    for name, user in var.users : name => user
    if !user.is_admin
  }

  data = "%{ for a in b}foo%{ endfor }"
}
