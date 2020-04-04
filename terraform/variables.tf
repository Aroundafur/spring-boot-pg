# define the GCP authentication file
variable "gcp_auth_file" {
  type = string
  description = "GCP authentication file"
}

# define GCP project name
variable "app_project" {
  type = string
  description = "GCP project name"
}

# define GCP region
variable "gcp_location" {
  type = string
  description = "GCP location"
}
# define GCP zone
variable "gcp_zone" {
  type = string
  description = "GCP zone"
}

# define GCP initial node count
variable "initial_node_count" {
  type = number
  description = "GCP cluster node count"
}

# define GCP machine type
variable "machine_type" {
  type = string
  description = "GCP machine type"
}