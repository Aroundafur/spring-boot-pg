//resource "google_container_cluster" "default" {
//  name = var.app_project
//  project = var.app_project
//  description = "Demo GKE Cluster"
//  location = var.gcp_location
//
//  remove_default_node_pool = true
//  initial_node_count = var.initial_node_count
//
//  master_auth {
//    username = ""
//    password = ""
//
//    client_certificate_config {
//      issue_client_certificate = false
//    }
//  }
//}
//
//resource "google_container_node_pool" "default" {
//  name = "${var.app_project}-node-pool"
//  project = var.app_project
//  location = var.gcp_location
//  cluster = google_container_cluster.default.name
//  node_count = 1
//
//  node_config {
//    preemptible = true
//    machine_type = var.machine_type
//
//    metadata = {
//      disable-legacy-endpoints = "true"
//    }
//
//    oauth_scopes = [
//      "https://www.googleapis.com/auth/logging.write",
//      "https://www.googleapis.com/auth/monitoring",
//    ]
//  }
//}