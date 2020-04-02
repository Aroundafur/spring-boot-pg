#!/bin/bash


#provision:
terraform init
terraform apply -auto-approve

kubectl apply -f deployment.yaml

#destroy:
terraform destroy -auto-approve
