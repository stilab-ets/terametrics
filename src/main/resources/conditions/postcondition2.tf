variable "vpc_cidrs" {
  type = set(string)
}

data "aws_vpc" "example" {
  for_each = var.vpc_cidrs

  filter {
    name   = "cidr"
    values = [each.key]
  }
}

resource "aws_internet_gateway" "example" {
  for_each = data.aws_vpc.example
  vpc_id = each.value.id

  lifecycle {
    precondition {
      condition     = data.aws_vpc.example[each.key].state == "available"
      error_message = "VPC ${each.key} must be available."
    }
  }
}
