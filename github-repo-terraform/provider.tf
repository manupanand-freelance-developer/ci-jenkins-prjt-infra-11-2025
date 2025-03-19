provider "github" {
 owner="manupanand-freelance-developer"

}
terraform {
  required_providers {
    github={
        source = "integartions/github"
        version="~> 6.0"
    }
  }
}