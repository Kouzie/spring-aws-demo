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
#    },
#    {
#      "Expiration": { "Days": 10 },
#      "ID": "TagDeleteRule_10",
#      "Filter": { "Tag": { "Key": "DELETE_TAG", "Value": "DELETE_10" } },
#      "Status": "Enabled"
#    },
#    {
#      "Expiration": { "Days": 15 },
#      "ID": "TagDeleteRule_15",
#      "Filter": { "Tag": { "Key": "DELETE_TAG", "Value": "DELETE_15" } },
#      "Status": "Enabled"
#    },
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