data "aws_ami" "example" {
  owners = ["amazon"]

  filter {
    name   = "image-id"
    values = ["ami-abc123"]
  }
}

resource "aws_instance" "example" {
  instance_type = "t3.micro"
  ami           = data.aws_ami.example.id

  lifecycle {
    # The AMI ID must refer to an AMI that contains an operating system
    # for the `x86_64` architecture.
    precondition {
      condition     = data.aws_ami.example.architecture == "x86_64"
      error_message = "The selected AMI must be for the x86_64 architecture."
    }

    # The EC2 instance must be allocated a public DNS hostname.
    postcondition {
      condition     = self.public_dns != ""
      error_message = "EC2 instance must be in a VPC that has public DNS hostnames enabled."
    }
  }
}

data "aws_ebs_volume" "example" {
  # Use data resources that refer to other resources to
  # load extra data that isn't directly exported by a resource.
  #
  # Read the details about the root storage volume for the EC2 instance
  # declared by aws_instance.example, using the exported ID.

  filter {
    name = "volume-id"
    values = [aws_instance.example.root_block_device.volume_id]
  }

  # Whenever a data resource is verifying the result of a managed resource
  # declared in the same configuration, you MUST write the checks as
  # postconditions of the data resource. This ensures Terraform will wait
  # to read the data resource until after any changes to the managed resource
  # have completed.
  lifecycle {
    # The EC2 instance will have an encrypted root volume.
    postcondition {
      condition     = self.encrypted
      error_message = "The server's root volume is not encrypted."
    }
  }
}

output "api_base_url" {
  value = "https://${aws_instance.example.private_dns}:8433/"
}
