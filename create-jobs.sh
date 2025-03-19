if [-z "${vault_token}"]; then
    echo vault_token is missing
    exit 1

fi 
git pull; ansible-playbook  create-jobs-playbook.yml -e vault_token=${vault_token} 