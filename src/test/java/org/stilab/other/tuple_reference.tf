
# Try to get identify the reference under the tuple depends_on []
resource "google_compute_shared_vpc_service_project" "shared_vpc_attachment" {
  count = "${var.shared_vpc != "" ? 1 : 0}"

  host_project    = "${var.shared_vpc}"
  service_project = "${google_project.main.project_id}"

  depends_on = [
    google_project_service.project_services,
    google_project_services.project_services_authority,
  ]
}
