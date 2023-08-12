locals {
  services = toset(concat(var.activate_apis, [for i in var.activate_api_identities : i.api]))
  service_identities = flatten([
    for i in var.activate_api_identities : [
      for r in i.roles :
      { api = i.api, role = r }
    ]
  ])
}

locals {
  mahi = {
    mahi = {
      name = "mahi"
      surname = "begoug"
    }
  }
}
