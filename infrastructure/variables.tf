variable "aws_region" {
  description = "AWS region"
  default     = "eu-west-2"
}

variable "environment" {
  description = "Environment name"
  default     = "prod"
}

variable "domain_name" {
  description = "Domain name for the service"
  default     = "docusign-ingest.robmacdonald.com"
}

variable "vpc_id" {
  description = "VPC ID"
  default     = "vpc-04131bd1fcc74b6e4"
}
