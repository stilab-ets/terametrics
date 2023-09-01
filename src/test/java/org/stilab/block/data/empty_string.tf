resource "aws_cloudwatch_event_target" "example" {
  # ...
  ecs_target {
    task_count          = 1
    task_definition_arn = aws_ecs_task_definition.task.arn
    launch_type         = ""
    # ...
  }
}

resource "aws_cloudwatch_event_target" "example" {
  # ...
  ecs_target {
    task_count          = 1
    task_definition_arn = aws_ecs_task_definition.task.arn
    launch_type         = ""
    # ...
  }
}

resource "aws_instance" "example" {
  instance_type = "t2.micro"
  private_ip    = ""
}

resource "aws_elasticsearch_domain" "example" {
  # ...
  ebs_options {
    ebs_enabled = true
    volume_size = var.volume_size
    volume_type = var.volume_size > 0 ? local.volume_type : ""
  }
}

resource "aws_elasticsearch_domain" "example" {
  # ...
  ebs_options {
    ebs_enabled = true
    volume_size = var.volume_size
    volume_type = var.volume_size > 0 ? local.volume_type : ""
  }
}

resource "aws_route" "example" {
  route_table_id = aws_route_table.example.id
  gateway_id     = aws_internet_gateway.example.id

  destination_cidr_block      = local.ipv6 ? "" : local.destination
  destination_ipv6_cidr_block = local.ipv6 ? local.destination_ipv6 : ""
}

resource "aws_route_table" "example" {
  # ...
  route {
    cidr_block      = local.ipv6 ? "" : local.destination
    ipv6_cidr_block = local.ipv6 ? local.destination_ipv6 : ""
  }
}

resource "aws_cloudwatch_event_target" "test" {
  arn      = aws_ecs_cluster.test.id
  rule     = aws_cloudwatch_event_rule.test.id
  role_arn = aws_iam_role.test.arn
  ecs_target {
    launch_type         = ""
    task_count          = 1
    task_definition_arn = aws_ecs_task_definition.task.arn
    network_configuration {
      subnets = [aws_subnet.subnet.id]
    }
  }
}

resource "aws_vpc" "example" {
  cidr_block      = "10.1.0.0/16"
  ipv6_cidr_block = ""
}


