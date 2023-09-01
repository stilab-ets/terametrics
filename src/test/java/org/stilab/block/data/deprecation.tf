#data "aws_kms_secret" "example" {
#  secret {
#    # ... potentially other configuration ...
#    name    = "master_password"
#    payload = "AQEC..."
#  }
#
#  secret {
#    # ... potentially other configuration ...
#    name    = "master_username"
#    payload = "AQEC..."
#  }
#}
#

#resource "aws_rds_cluster" "example" {
#  # ... other configuration ...
#  master_password = "${data.aws_kms_secret.example.master_password}"
#  master_username = "${data.aws_kms_secret.example.master_username}"
#}

#
#resource "aws_api_gateway_rest_api" "example" {
#  name = "example"
#}

#resource "aws_api_gateway_deployment" "example" {
#  rest_api_id = "${aws_api_gateway_rest_api.example.id}"
#  stage_name  = "example"
#}
#
#resource "aws_api_gateway_api_key" "example" {
#  name = "example"
#
#  stage_key {
#    rest_api_id = "${aws_api_gateway_rest_api.example.id}"
#    stage_name  = "${aws_api_gateway_deployment.example.stage_name}"
#  }
#}
#
#resource "aws_api_gateway_integration" "example" {
#  # ... other configuration ...
#
#  request_parameters_in_json = <<PARAMS
#{
#    "integration.request.header.X-Authorization": "'static'"
#}
#PARAMS
#}
#
#resource "aws_api_gateway_integration_response" "example" {
#  # ... other configuration ...
#
#  response_parameters_in_json = <<PARAMS
#{
#    "method.response.header.Content-Type": "integration.response.body.type"
#}
#PARAMS
#}
#
#resource "aws_api_gateway_method" "example" {
#  # ... other configuration ...
#
#  request_parameters_in_json = <<PARAMS
#{
#    "method.request.header.Content-Type": false,
#    "method.request.querystring.page": true
#}
#PARAMS
#}
#
#resource "aws_api_gateway_method_response" "example" {
#  # ... other configuration ...
#
#  response_parameters_in_json = <<PARAMS
#{
#    "method.response.header.Content-Type": true
#}
#PARAMS
#}
#
#resource "aws_appautoscaling_policy" "example" {
#  # ... other configuration ...
#
#  adjustment_type         = "ChangeInCapacity"
#  cooldown                = 60
#  metric_aggregation_type = "Maximum"
#
#  step_adjustment {
#    metric_interval_upper_bound = 0
#    scaling_adjustment          = -1
#  }
#}
#

#resource "aws_autoscaling_policy" "example" {
#  # ... other configuration ...
#
#  min_adjustment_step = 2
#}

#
#resource "aws_cloudfront_distribution" "example" {
#  # ... other configuration ...
#
#  cache_behavior {
#    # ... other configuration ...
#  }
#
#  cache_behavior {
#    # ... other configuration ...
#  }
#}
#
#resource "aws_dx_lag" "example" {
#  name                  = "example"
#  connections_bandwidth = "1Gbps"
#  location              = "EqSe2-EQ"
#  number_of_connections = 1
#}
#
#resource "aws_ecs_service" "example" {
#  # ... other configuration ...
#
#  placement_strategy {
#    # ... other configuration ...
#  }
#
#  placement_strategy {
#    # ... other configuration ...
#  }
#}
#
#resource "aws_efs_file_system" "example" {
#  # ... other configuration ...
#
#  reference_name = "example"
#}
#
#resource "aws_elasticache_cluster" "example" {
#  # ... other configuration ...
#
#  availability_zones = ["us-west-2a", "us-west-2b"]
#}
#
#resource "aws_network_acl" "example" {
#  # ... other configuration ...
#
#  subnet_id = "subnet-12345678"
#}
#
#resource "aws_redshift_cluster" "example" {
#  # ... other configuration ...
#
#  bucket_name    = "example"
#  enable_logging = true
#  s3_key_prefix  = "example"
#}
#
#resource "aws_redshift_cluster" "example" {
#  # ... other configuration ...
#
#  bucket_name    = "example"
#  enable_logging = true
#  s3_key_prefix  = "example"
#}
#
#resource "aws_wafregional_byte_match_set" "example" {
#  # ... other configuration ...
#
#  byte_match_tuple {
#    # ... other configuration ...
#  }
#
#  byte_match_tuple {
#    # ... other configuration ...
#  }
#}
#
#output "lambda_result" {
#  value = "${data.aws_lambda_invocation.example.result_map["key1"]}"
#}
#
#resource "aws_cognito_user_pool" "example" {
#  # ... other configuration ...
#
#  admin_create_user_config {
#    # ... potentially other configuration ...
#
#    unused_account_validity_days = 7
#  }
#}
#
#resource "aws_dx_gateway_association" "example" {
#  # ... other configuration ...
#  vpn_gateway_id = aws_vpn_gateway.example.id
#}
#
#resource "aws_dx_gateway_association_proposal" "example" {
#  # ... other configuration ...
#  vpn_gateway_id = aws_vpn_gateway.example.id
#}
#
#resource "aws_emr_cluster" "example" {
#  # ... other configuration ...
#
#  core_instance_count = 2
#}
#
#resource "aws_emr_cluster" "example" {
#  # ... other configuration ...
#
#  core_instance_type = "m4.large"
#}
#
#resource "aws_emr_cluster" "example" {
#  # ... other configuration ...
#
#  instance_group {
#    instance_role = "MASTER"
#    instance_type = "m4.large"
#  }
#
#  instance_group {
#    instance_count = 1
#    instance_role  = "CORE"
#    instance_type  = "c4.large"
#  }
#
#  instance_group {
#    instance_count = 2
#    instance_role  = "TASK"
#    instance_type  = "c4.xlarge"
#  }
#}
#
#resource "aws_emr_cluster" "example" {
#  # ... other configuration ...
#
#  master_instance_type = "m4.large"
#}
#
#resource "aws_glue_job" "example" {
#  # ... other configuration ...
#
#  allocated_capacity = 2
#}
#
#resource "aws_iam_instance_profile" "example" {
#  # ... other configuration ...
#
#  roles = [aws_iam_role.example.id]
#}
#
#resource "aws_launch_template" "example" {
#  # ... other configuration ...
#
#  network_interfaces {
#    # ... other configuration ...
#
#    delete_on_termination = null
#  }
#}
#
#resource "aws_lb_listener_rule" "example" {
#  # ... other configuration ...
#
#  condition {
#    field  = "path-pattern"
#    values = ["/static/*"]
#  }
#}
#
#resource "aws_msk_cluster" "example" {
#  # ... other configuration ...
#
#  encryption_info {
#    # ... potentially other configuration ...
#
#    encryption_in_transit {
#      # ... potentially other configuration ...
#
#      client_broker = "TLS_PLAINTEXT"
#    }
#  }
#}
#
#resource "aws_s3_bucket" "example" {
#  # ... other configuration ...
#
#  region = "us-west-2"
#}
#
#resource "aws_s3_bucket_metric" "example" {
#  # ... other configuration ...
#
#  filter {}
#}
#
#resource "aws_ssm_maintenance_window_task" "example" {
#  # ... other configuration ...
#
#  logging_info {
#    s3_bucket_name       = aws_s3_bucket.example.id
#    s3_bucket_key_prefix = "example"
#  }
#}
#
#resource "aws_ssm_maintenance_window_task" "example" {
#  # ... other configuration ...
#
#  task_parameters {
#    name   = "commands"
#    values = ["date"]
#  }
#}
#
#resource "aws_s3_bucket" "example" {
#  bucket = "yournamehere"
#  acl    = "private"
#
#  # ... other configuration ...
#}
#
#resource "aws_s3_bucket" "example" {
#  bucket = "yournamehere"
#
#  # ... other configuration ...
#  grant {
#    id          = data.aws_canonical_user_id.current_user.id
#    type        = "CanonicalUser"
#    permissions = ["FULL_CONTROL"]
#  }
#
#  grant {
#    type        = "Group"
#    permissions = ["READ_ACP", "WRITE"]
#    uri         = "http://acs.amazonaws.com/groups/s3/LogDelivery"
#  }
#}
#
#resource "aws_s3_bucket" "example" {
#  bucket = "yournamehere"
#
#  # ... other configuration ...
#  cors_rule {
#    allowed_headers = ["*"]
#    allowed_methods = ["PUT", "POST"]
#    allowed_origins = ["https://s3-website-test.hashicorp.com"]
#    expose_headers  = ["ETag"]
#    max_age_seconds = 3000
#  }
#}
#
#resource "aws_s3_bucket" "example" {
#  bucket = "yournamehere"
#
#  lifecycle_rule {
#    id      = "Keep previous version 30 days, then in Glacier another 60"
#    enabled = true
#
#    noncurrent_version_transition {
#      days          = 30
#      storage_class = "GLACIER"
#    }
#
#    noncurrent_version_expiration {
#      days = 90
#    }
#  }
#
#  lifecycle_rule {
#    id                                     = "Delete old incomplete multi-part uploads"
#    enabled                                = true
#    abort_incomplete_multipart_upload_days = 7
#  }
#}
#
#resource "aws_s3_bucket" "example" {
#  bucket = "yournamehere"
#
#  lifecycle_rule {
#    id      = "log-expiration"
#    enabled = true
#    prefix  = ""
#
#    transition {
#      days          = 30
#      storage_class = "STANDARD_IA"
#    }
#
#    transition {
#      days          = 180
#      storage_class = "GLACIER"
#    }
#  }
#}
#
#resource "aws_s3_bucket" "example" {
#  bucket = "yournamehere"
#
#  lifecycle_rule {
#    id      = "log-expiration"
#    enabled = true
#    prefix  = "foobar"
#
#    transition {
#      days          = 30
#      storage_class = "STANDARD_IA"
#    }
#
#    transition {
#      days          = 180
#      storage_class = "GLACIER"
#    }
#  }
#}
#
#resource "aws_s3_bucket" "example" {
#  bucket = "yournamehere"
#
#  # ... other configuration ...
#  lifecycle_rule {
#    id      = "log"
#    enabled = true
#    prefix = "log/"
#
#    tags = {
#      rule      = "log"
#      autoclean = "true"
#    }
#
#    transition {
#      days          = 30
#      storage_class = "STANDARD_IA"
#    }
#
#    transition {
#      days          = 60
#      storage_class = "GLACIER"
#    }
#
#    expiration {
#      days = 90
#    }
#  }
#
#  lifecycle_rule {
#    id      = "tmp"
#    prefix  = "tmp/"
#    enabled = true
#
#    expiration {
#      date = "2022-12-31"
#    }
#  }
#}
#
#resource "aws_s3_bucket" "log_bucket" {
#  # ... other configuration ...
#  bucket = "example-log-bucket"
#}
#
#resource "aws_s3_bucket" "example" {
#  bucket = "yournamehere"
#
#  # ... other configuration ...
#  logging {
#    target_bucket = aws_s3_bucket.log_bucket.id
#    target_prefix = "log/"
#  }
#}
#
#resource "aws_s3_bucket" "example" {
#  bucket = "yournamehere"
#
#  # ... other configuration ...
#  policy = <<EOF
#{
#  "Id": "Policy1446577137248",
#  "Statement": [
#    {
#      "Action": "s3:PutObject",
#      "Effect": "Allow",
#      "Principal": {
#        "AWS": "${data.aws_elb_service_account.current.arn}"
#      },
#      "Resource": "arn:${data.aws_partition.current.partition}:s3:::yournamehere/*",
#      "Sid": "Stmt1446575236270"
#    }
#  ],
#  "Version": "2012-10-17"
#}
#EOF
#}
#
#resource "aws_s3_bucket" "example" {
#  bucket = "yournamehere"
#
#  # ... other configuration ...
#  request_payer = "Requester"
#}
#
resource "aws_s3_bucket" "example" {
  bucket = "yournamehere"

  # ... other configuration ...
  server_side_encryption_configuration {
    rule {
      apply_server_side_encryption_by_default {
        kms_master_key_id = aws_s3_bucket.example.region
        sse_algorithm     = "aws:kms"
      }
    }
  }
}
#
#resource "aws_s3_bucket" "example" {
#  bucket = "yournamehere"
#
#  # ... other configuration ...
#  versioning {
#    enabled = true
#  }
#}
#
#data "aws_subnet_ids" "example" {
##  vpc_id = var.vpc_id
#  vpc_id = data.aws_subnet_ids.example.vpc_id
#}
#
#data "aws_connect_hours_of_operation" "example" {
#  hours_of_operation_arn = "10h"
#}
#
#data "aws_subnet" "example" {
#  for_each = data.aws_subnet_ids.example.ids
#  id       = each.value
#}
#
#output "subnet_cidr_blocks" {
#  value = [for s in data.aws_subnet.example : s.cidr_block]
#}
#
#output "elasticache_global_replication_group_version_result" {
#  value = aws_elasticache_global_replication_group.example.actual_engine_version
#}
#
#resource "aws_db_instance" "test" {
#  replicate_source_db = aws_db_instance.source.id
#  # ...other configuration...
#}
#
#resource "aws_route" "example" {
#  instance_id = aws_instance.example.id
#  # ...other configuration...
#}
#
#resource "aws_network_interface" "example" {
#  # ...other configuration...
#}
#
#resource "aws_instance" "example" {
#  network_interface {
#    network_interface_id = aws_network_interface.example.id
#    # ...other configuration...
#  }
#
#  # ...other configuration...
#}
#
#resource "aws_route" "example" {
#  network_interface_id = aws_network_interface.example.id
#  # ...other configuration...
#
#  # Wait for the ENI attachment
#  depends_on = [aws_instance.example]
#}
#
#resource "aws_route_table" "example" {
#  route {
#    instance_id = aws_instance.example.id
#    # ...other configuration...
#  }
#  # ...other configuration...
#}
#
