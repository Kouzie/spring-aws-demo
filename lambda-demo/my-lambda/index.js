const AWS = require('aws-sdk');
const sqs = new AWS.SQS({
    endpoint: 'http://sqs.us-east-1.localhost.localstack.cloud:4566', // LocalStack 엔드포인트
    region: 'us-east-1',               // LocalStack 기본 리전
});

exports.sendSqsHandler = async (event) => {
    console.log('Process started');

    // 10초 대기
    await new Promise((resolve) => setTimeout(resolve, 10000));
    console.log('10 seconds passed');

    // SQS 메시지 전송
    const sqsParams = {
        QueueUrl: 'http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/demo-queue', // LocalStack의 SQS URL
        MessageBody: JSON.stringify({ message: 'This is a test message from Lambda in LocalStack' }),
    };

    try {
        const result = await sqs.sendMessage(sqsParams).promise();
        console.log('Message sent to SQS', result.MessageId);
    } catch (error) {
        console.error('Error sending message to SQS', error);
        return {
            statusCode: 500,
            body: 'Error sending message to SQS' + error,
        };
    }

    console.log('Process completed');
    return {
        statusCode: 200,
        body: 'Process completed successfully',
    };
};

exports.multiplyHandler = async (event) => {
    let body = JSON.parse(event.body);
    const product = body.num1 * body.num2;
    return {
        statusCode: 200,
        body: `The product of ${body.num1} and ${body.num2} is ${product}`,
    };
};

exports.addHandler = async (event) => {
    let body = JSON.parse(event.body);
    const sum = body.num1 + body.num2;
    return {
        statusCode: 200,
        body: `The sum of ${body.num1} and ${body.num2} is ${sum}`,
    };
};