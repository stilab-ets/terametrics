variable "legacy_filenames" {
  type     = bool
  default  = false
  nullable = false
}

module "buckets" {
  source = "./modules/buckets"

  buckets = [
    {
      name = "production"
      website = {
        routing_rules = <<-EOT
      [
        {
          "Condition" = { "KeyPrefixEquals": "img/" },
          "Redirect"  = { "ReplaceKeyPrefixWith": "images/" }
        }
      ]
      EOT
      }
    },
    {
      name = "archived"
      enabled = false
    },
    {
      name = "docs"
      website = {
        index_document = "index.txt"
        error_document = "error.txt"
      }
    },
  ]
}
