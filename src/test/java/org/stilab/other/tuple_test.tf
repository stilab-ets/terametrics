resource "aws_customer_gateway" "this" {
  for_each = var.customer_gateways

  bgp_asn    = each.value["bgp_asn"] ### This one
  ip_address = each.value["ip_address"] ### This second
  type       = "ipsec.1"

  tags = merge(
    {
      Name = format("%s-%s", var.name, each.key)
    },
    var.tags,
    var.customer_gateway_tags,
  )
}
