## LocalStack

> <https://docs.localstack.cloud/overview/>


LocalStack 은 AWS Lambda , S3 , DynamoDB , Kinesis , SQS , SNS 등 
점점 늘어나는 AWS 서비스를 지원합니다! 
LocalStack Pro는 추가 API와 고급 기능을 지원하여 클라우드 개발 경험을 쉽게 만들어줍니다!

## GUI 툴

LocalStack Desktop 혹은 아래 브라우저 기반 사이트에서 localhost 에서 동작중인 LocalStack 서비스에 접근 가능 

> <https://app.localstack.cloud/inst/default/resources>
> 회원가입 필수

## CLI 툴

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