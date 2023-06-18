data "aws_ami" "example" {
  id = var.aws_ami_id

  lifecycle {
    # The AMI ID must refer to an existing AMI that has the tag "nomad-server".
    postcondition {
      condition     = self.tags["Component"] == "nomad-server"
      error_message = "tags[\"Component\"] must be \"nomad-server\"."
    }
  }
  owners = []
}

data "aws_ami" "example2" {
  id = var.aws_ami_id

  lifecycle {
    # The AMI ID must refer to an existing AMI that has the tag "nomad-server".
    postcondition {
      condition = contains(["STAGE", "PROD"], var.environment)
      error_message = "tags[\"Component\"] must be \"nomad-server\"."
    }
  }
  owners = []
}

data "aws_ami" "example3" {
  id = var.aws_ami_id

  lifecycle {
    # The AMI ID must refer to an existing AMI that has the tag "nomad-server".
    postcondition {
      condition = length(var.items) != 0
      error_message = "tags[\"Component\"] must be \"nomad-server\"."
    }
  }
  owners = []
}

data "aws_ami" "example4" {
  id = var.aws_ami_id

  lifecycle {
    # The AMI ID must refer to an existing AMI that has the tag "nomad-server".
    postcondition {
      condition = alltrue([
        for v in var.instances : contains(["t2.micro", "m3.medium"], v.type)
      ])
      error_message = "tags[\"Component\"] must be \"nomad-server\"."
    }
  }
  owners = []
}



data "aws_ami" "example5" {
  id = var.aws_ami_id

  lifecycle {
    # The AMI ID must refer to an existing AMI that has the tag "nomad-server".
    postcondition {
      condition = can(regex("^[a-z]+$", var.name))
      error_message = "tags[\"Component\"] must be \"nomad-server\"."
    }
  }
  owners = []
}

data "aws_ami" "example6" {
  id = var.aws_ami_id

  lifecycle {
    # The AMI ID must refer to an existing AMI that has the tag "nomad-server".
    postcondition {
      # This remote output value must have a value that can
      # be used as a string, which includes strings themselves
      # but also allows numbers and boolean values.
      condition = can(tostring(data.terraform_remote_state.example.outputs["name"]))
      error_message = "tags[\"Component\"] must be \"nomad-server\"."
    }
  }
  owners = []
}

data "aws_ami" "example6" {
  id = var.aws_ami_id

  lifecycle {
    # The AMI ID must refer to an existing AMI that has the tag "nomad-server".
    postcondition {
      # This remote output value must be convertible to a list
      # type of with element type.
      condition = can(tolist(data.terraform_remote_state.example.outputs["items"]))
      error_message = "tags[\"Component\"] must be \"nomad-server\"."
    }
  }
  owners = []
}

data "aws_ami" "example7" {
  id = var.aws_ami_id

  lifecycle {
    # The AMI ID must refer to an existing AMI that has the tag "nomad-server".
    postcondition {
      # var.example must have an attribute named "foo"
      condition = can(var.example.foo)
      error_message = "tags[\"Component\"] must be \"nomad-server\"."
    }
  }
  owners = []
}

data "aws_ami" "example7" {
  id = var.aws_ami_id

  lifecycle {
    # The AMI ID must refer to an existing AMI that has the tag "nomad-server".
    postcondition {
      # var.example must be a sequence with at least one element
      condition = can(var.example[0])
      # (although it would typically be clearer to write this as a
      # test like length(var.example) > 0 to better represent the
      # intent of the condition.)
      error_message = "tags[\"Component\"] must be \"nomad-server\"."
    }
  }
  owners = []
}

resource "aws_instance" "example" {
  instance_type = "t2.micro"
  ami           = "ami-abc123"

  lifecycle {
    postcondition {
      condition     = self.instance_state == "running"
      error_message = "EC2 instance must be running."
    }
  }
}

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
