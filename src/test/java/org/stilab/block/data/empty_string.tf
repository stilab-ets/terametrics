resource "aws_cloudwatch_event_target" "example" {
  # ...
  ecs_target {
    task_count          = 1
    task_definition_arn = aws_ecs_task_definition.task.arn
    launch_type         = ""
    # ...
  }
}
