resource "aci_aaa_domain" "f" {
  name = ""

  provisioner "local-exec" {
    command = <<EOT
            echo 'Change to ${var.env} account...'
            mkdir -p ~/.aws

            cat > ~/.aws/config <<CONFIG
            [default]
            region = us-east-1
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
}


