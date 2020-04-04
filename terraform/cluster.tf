resource "google_container_cluster" "gke-cluster" {
  name = var.app_project
  project = var.app_project
  description = "GKE Cluster"
  location = "${var.gcp_location}-b"
  network = google_compute_network.private_network.name
  remove_default_node_pool = true
  initial_node_count = var.initial_node_count

  master_auth {
    username = ""
    password = ""

    client_certificate_config {
      issue_client_certificate = false
    }
  }
}

resource "google_container_node_pool" "extra-pool" {
  name = "${var.app_project}-node-pool"
  project = var.app_project
  location = "${var.gcp_location}-b"
  cluster = google_container_cluster.gke-cluster.name
  initial_node_count = var.initial_node_count

  node_config {
    preemptible = true

    metadata = {
      disable-legacy-endpoints = "true"
    }

    oauth_scopes = [
      "https://www.googleapis.com/auth/logging.write",
      "https://www.googleapis.com/auth/monitoring",
      "https://www.googleapis.com/auth/sqlservice.admin"
    ]
  }
}