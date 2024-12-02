
```shell
awslocal ssm put-parameter \
    --type String \
    --name "/demo/param/DEMO_PROPERTIES_ID" \
    --value "aws_id"
    
awslocal ssm put-parameter \
    --type String \
    --name "/demo/param/DEMO_PROPERTIES_KEY" \
    --value "aws_key"

awslocal ssm put-parameter \
    --type String \
    --name "/demo/param/DEMO_PROPERTIES_URL" \
    --value "aws_url"
```