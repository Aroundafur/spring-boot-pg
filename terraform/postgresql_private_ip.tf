resource "google_compute_network" "private_network" {
  provider = google-beta
  project = var.app_project
  name = "private-network"
}

resource "google_compute_global_address" "private_ip_address" {
  provider = google-beta

  name = "private-ip-address"
  purpose = "VPC_PEERING"
  address_type = "INTERNAL"
  prefix_length = 16
  network = google_compute_network.private_network.self_link
}

resource "google_service_networking_connection" "private_vpc_connection" {
  provider = google-beta

  network = google_compute_network.private_network.self_link
  service = "servicenetworking.googleapis.com"
  reserved_peering_ranges = [
    google_compute_global_address.private_ip_address.name]
}

resource "random_id" "db_name_suffix" {
  byte_length = 4
}

resource "google_sql_database_instance" "master" {
  provider = google-beta
  name = "private-instance-${random_id.db_name_suffix.hex}"
  region = var.gcp_location
  depends_on = [
    google_service_networking_connection.private_vpc_connection]
  database_version = var.db_version

  settings {
    tier = var.db_tier
    availability_type = "REGIONAL"
    activation_policy = var.db_activation_policy
    disk_autoresize = var.db_disk_autoresize
    disk_size = var.db_disk_size
    disk_type = var.db_disk_type
    pricing_plan = var.db_pricing_plan

    location_preference {
      zone = var.gcp_zone
    }

    backup_configuration {
      enabled = true
    }

    maintenance_window {
      day = "7"
      # sunday
      hour = "3"
      # 3am
    }
    ip_configuration {
      ipv4_enabled = true
      private_network = google_compute_network.private_network.self_link
    }
  }
}

# create database
resource "google_sql_database" "main" {
  depends_on = [
    google_sql_user.main
  ]
  name = var.db_name
  project = var.app_project
  instance = google_sql_database_instance.master.name
  charset = var.db_charset
  collation = var.db_collation
}

# create user
resource "random_id" "user_password" {
  byte_length = 8
}

resource "google_sql_user" "main" {
  depends_on = [
    google_sql_database_instance.master
  ]
  name = var.db_user_name
  project = var.app_project
  instance = google_sql_database_instance.master.name
  password = var.db_user_password == "" ? random_id.user_password.hex : var.db_user_password
}

resource "google_sql_database_instance" "read_replica" {
  name = "replica-${random_id.db_name_suffix.hex}"
  master_instance_name = "${var.app_project}:${google_sql_database_instance.master.name}"
  region = var.gcp_location
  database_version = var.db_version

  replica_configuration {
    failover_target = false
  }

  settings {
    tier = var.db_tier
    availability_type = "ZONAL"
    disk_size = var.db_disk_size

    backup_configuration {
      enabled = false
    }

    ip_configuration {
      ipv4_enabled = true
      private_network = google_compute_network.private_network.self_link
    }

    location_preference {
      zone = var.gcp_zone
    }
  }
}

resource "google_sql_user" "sqlproxy" {
  depends_on = [
    google_sql_database_instance.master
  ]
  name = var.sqlproxy_user_name
  project = var.app_project
  instance = google_sql_database_instance.master.name
  password = var.db_user_password
}