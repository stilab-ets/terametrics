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
}

variable "users1" {
  type = map(object({
    role = string
  }))
}

locals {
  users_by_role = {
    for name, user in var.users1 : user.role => name
  }
}
