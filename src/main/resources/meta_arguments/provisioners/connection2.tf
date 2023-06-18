resource "aws_instance" "cluster" {
  count = 3

  # ...
}

resource "terraform_data" "cluster" {
  # Replacement of any instance of the cluster requires re-provisioning
  triggers_replace = aws_instance.cluster.[*].id

  # Bootstrap script can run on any instance of the cluster
  # So we just choose the first in this case
  connection {
    host = aws_instance.cluster.[0].public_ip
  }

  provisioner "remote-exec" {
    # Bootstrap script called with private_ip of each node in the cluster
    inline = [
      "bootstrap-cluster.sh ${join(" ", aws_instance.cluster.*.private_ip)}",
    ]
  }
}
