///  setting github repo webhook - for jenkins


resource "github_repository_webhook" "main" {
    for_each = var.repos
    repository= each.key
    configuration{
        url="http://reoutejenkins.manupanand.online:8080/multibranch-webhook-trigger/invoke?token=${ each.key}"
        content_type="form"
        # insecure_ssl=false
    }
    active=true

    events=["push"]//github webhook events and payload->push

}

# module "ENV" {
    
#     source = "./ADD-ENV-GITHUB"
#     repos= local.repos_with_env_list
# }
# we need to add  repo autherization for differernt environment
resource "github_repository_environment" "env" {

    count =  length(local.repos_with_env_list)

    repository= local.repos_with_env_list[count.index]["app"]
    environment =local.repos_with_env_list[count.index]["env"]
    prevent_self_review=false
    reviewers{
        users = [data.github_user.user-number.id] # number
    }
  
}

data "github_user" "user-number" {
  username="manupanand"
}