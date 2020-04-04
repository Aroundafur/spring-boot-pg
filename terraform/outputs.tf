# Database output | postgresql-instance-output.tf
output db_instance_address {
  description = "IP address of the master database instance"
  value = google_sql_database_instance.master.ip_address[0].ip_address
}
output db_instance_name {
  description = "Name of the database instance"
  value = google_sql_database_instance.master.name
}
output db_instance_username {
  description = "Name of the database user"
  value = var.db_user_name
}
output db_instance_generated_user_password {
  description = "The auto generated default user password if no input password was provided"
  value = var.db_user_password
}