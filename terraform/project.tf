resource "google_project" "my_project" {
  name       = "My Project"
  project_id = "your-project-id"
  org_id     = "1234567"
}

resource "random_id" "project_name_suffix" {
  byte_length = 4
}