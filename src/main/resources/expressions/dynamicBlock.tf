
# https://developer.hashicorp.com/terraform/language/expressions/dynamic-blocks

#  Overuse of dynamic blocks can make configuration hard to read and maintain, so we recommend using them only when you need
#  to hide details in order to build a clean user interface for a re-usable module. Always write nested blocks out literally where possible.
#
#  If you find yourself defining most or all of a resource block's arguments and nested blocks using directly-corresponding
#  attributes from an input variable then that might suggest that your module is not creating a useful abstraction.
#  It may be better for the calling module to define the resource itself then pass information about it into your module.
#  For more information on this design tradeoff, see When to Write a Module and Module Composition.

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

  dynamic "origin_group" {
    for_each = var.load_balancer_origin_groups
    content {
      name = origin_group.key

      dynamic "origin" {
        for_each = origin_group.value.origins
        content {
          hostname = origin.value.hostname
        }
      }
    }
  }

}

variable "load_balancer_origin_groups" {
  type = map(object({
    origins = set(object({
      hostname = string
    }))
  }))
}
