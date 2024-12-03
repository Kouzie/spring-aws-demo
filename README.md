## LocalStack

LocalStack 실행 및 전용 aws cli 설치

```shell
docker-compose up -d
```

```shell
pip install awscli-local
awslocal configure
AWS Access Key ID [None]: test-access-key
AWS Secret Access Key [None]: test-secret-key
Default region name [None]: us-east-1
Default output format [None]: json

awslocal s3 mb s3://my-local-bucket
awslocal s3 ls
```

## S3


```shell
curl -X GET "http://localhost:8080?prefix=my-folder"
```

```shell
curl -X POST "http://localhost:8080/upload" \
  -F "file=@Lenna.png" \
  -F "prefix=my-folder" \
  -H "Content-Type: multipart/form-data"
```

```shell
curl -X DELETE "http://localhost:8080?key=my-folder/d7438429-f2de-4f90-af61-373a352d3d09"
```

```shell
awslocal s3api get-bucket-lifecycle-configuration \
  --bucket demo-bucket
#{
#  "Rules": [
#    {
#      "Expiration": { "Days": 1 },
#      "ID": "TagDeleteRule_1",
#      "Filter": { "Tag": { "Key": "DELETE_TAG", "Value": "DELETE_1" } },
#      "Status": "Enabled"
#    }, ...
#    {
#      "Expiration": { "Days": 30 },
#      "ID": "TagDeleteRule_30",
#      "Filter": { "Tag": { "Key": "DELETE_TAG", "Value": "DELETE_30" } },
#      "Status": "Enabled"
#    },
#    {
#      "Expiration": { "Days": 30 },
#      "ID": "PrefixDeleteRule",
#      "Filter": { "Prefix": "temp/" },
#      "Status": "Enabled"
#    },
#    {
#      "ID": "TransitionRule",
#      "Filter": { "Prefix": "logs/" },
#      "Status": "Enabled",
#      "Transitions": [ { "Days": 90, "StorageClass": "GLACIER" } ]
#    }
#  ]
#}


awslocal s3api get-object-tagging \
  --bucket demo-bucket \
  --key my-folder/258a6920-5527-46b9-bccd-c3c84d7e93c8
#{
#    "TagSet": [
#        {
#            "Key": "DELETE_TAG",
#            "Value": "DELETE_1"
#        }
#    ]
#}
```

## SQS

```shell
curl -X POST http://localhost:8080/message \
     -H "Content-Type: application/json" \
     -d '{"key1": "value1", "key2": "value2"}'

# 시퀀셜하게 100번 요청 
for i in {1..100}; \
do echo "Sending request #$i"; \
curl -X POST http://localhost:8080/message \
-H "Content-Type: application/json" \
-d "{\"key1\": \"value$i\", \"key2\": \"value2\"}"; \
done

# 10개 스레드로 1000번 요청
seq 1 1000 | xargs -P 10 -n 1 -I {} sh -c '
  echo "Sending request #{}"
  curl -X POST http://localhost:8080/message \
       -H "Content-Type: application/json" \
       -d "{\"key1\": \"value{}\", \"key2\": \"value2\"}"
'
```

## Parameter Store

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

## DynamoDB

```shell
curl -X POST http://localhost:8080/customer \
     -H "Content-Type: application/json" \
     -d '{
           "username": "john_doe",
           "password": "password123",
           "nickname": "Johnny",
           "intro": "Hello, I am John!",
           "age": 30,
           "email": "john.doe@example.com"
         }'

curl -X GET http://localhost:8080/customer/40b7472a-cd4f-47c5-80ef-214cbebc8997

# 한국시간 기준 Tue Dec 03 2024 00:00:00 - Tue Dec 04 2024 00:00:00
curl -X GET "http://localhost:8080/customer?beginDate=1733151600&endingDate=1733238000"
```

## Lambda


```shell
cd lambda-demo/my-lambda
# package.json 생성 및 aws-sdk 설치
npm init -y
npm install aws-sdk

zip -r function.zip index.js package.json node_modules/

awslocal lambda create-function \
    --function-name MultiplyNumbers \
    --runtime nodejs18.x \
    --role arn:aws:iam::123456789012:role/lambda-role \
    --handler index.multiplyHandler \
    --zip-file fileb://function.zip

awslocal lambda create-function \
    --function-name AddNumbers \
    --runtime nodejs18.x \
    --role arn:aws:iam::123456789012:role/lambda-role \
    --handler index.addHandler \
    --zip-file fileb://function.zip

awslocal lambda create-function \
    --function-name SendSqsMessage \
    --runtime nodejs18.x \
    --role arn:aws:iam::123456789012:role/lambda-role \
    --handler index.sendSqsHandler \
    --zip-file fileb://function.zip
# 제한시간 1분으로 연장(default 3초) create-function 에선 timeout 설정 불가능
awslocal lambda update-function-configuration \
    --function-name SendSqsMessage \
    --timeout 60

awslocal lambda delete-function --function-name MultiplyNumbers
awslocal lambda delete-function --function-name AddNumbers
awslocal lambda delete-function --function-name SendSqsMessage
```

```shell
echo '{"body": "{\"num1\": 4, \"num2\": 5}"}' > event.json

awslocal lambda invoke \
    --invocation-type RequestResponse \
    --function-name MultiplyNumbers \
    --payload file://event.json \
    response.json
    
awslocal lambda invoke \
    --invocation-type RequestResponse \
    --function-name AddNumbers \
    --payload file://event.json \
    response.json

awslocal lambda invoke \
    --invocation-type RequestResponse \
    --function-name SendSqsMessage \
    --payload file://event.json \
    response.json
    
awslocal lambda invoke \
    --invocation-type Event \
    --function-name SendSqsMessage \
    --payload file://event.json \
    response.json
```

```shell
curl -X POST http://localhost:8081/lambda/invoke \
    -H "Content-Type: application/json" \
    -d '{
        "functionName": "MultiplyNumbers",
        "type": "RequestResponse",
        "payload": "{\"num1\": 4, \"num2\": 5}"
    }'
    
curl -X POST http://localhost:8081/lambda/invoke \
    -H "Content-Type: application/json" \
    -d '{
        "functionName": "AddNumbers",
        "type": "RequestResponse",
        "payload": "{\"num1\": 4, \"num2\": 5}"
    }'

curl -X POST http://localhost:8081/lambda/invoke \
    -H "Content-Type: application/json" \
    -d '{
        "functionName": "SendSqsMessage",
        "type": "Event",
        "payload": ""
    }'
```