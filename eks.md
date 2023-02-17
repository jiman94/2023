# EKS 1.24 구성하기

!!!! note Install 

1. aws-cli 설치
```shell 
 aws --version
```

2. AWS CLI Configure 구성

```shell 
aws configure

AWS Access Key ID [None]: 
AWS Secret Access Key [None]: 
Default region name [None]: ap-northeast-2
Default output format [None]: json

```


3. kubectl 설치


4. EKS 클러스터용 VPC 및 서브넷 생성

5. 클러스터 IAM 역할 생성 및 연결


```shell 
vi eks-cluster-role-trust-policy.json

{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "eks.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
```

```shell 
aws iam create-role \
  --role-name EKSClusterRole \
  --assume-role-policy-document file://"eks-cluster-role-trust-policy.json"
```

```shell 
aws iam attach-role-policy \
  --policy-arn arn:aws:iam::aws:policy/AmazonEKSClusterPolicy \
  --role-name EKSClusterRole
```

6. 클러스터 생성

!!! note aws EKS 콘솔로 이동한다.

```shell 
a) 생성할 클러스터 이름 입력 
b) kubernetes 버전 선택 
c) 클러스터 서비스 역할에서 이전에 만든 IAM 역할 선택 (EKSClusterRole) 
d) VPC 선택
e) 서브넷 추가
e) 생성한 보안그룹 추가
```

7. 클러스터와 통신 연결

```shell 
aws eks update-kubeconfig --region ap-northeast-2 --name test-eks 
```

```shell 
kubectl config get-contexts

kubectl get svc
```

8. 관리형 노드 IAM 생성 및 연결

```shell 
vi node-role-trust-policy.json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
```

!!!! node 노드 IAM 역할을 생성한다.

```shell 
aws iam create-role \
  --role-name EKSNodeRole \
  --assume-role-policy-document file://"node-role-trust-policy.json"
```

!!! node IAM 역할과 정책을 연결한다.
```shell 
aws iam attach-role-policy \
  --policy-arn arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy \
  --role-name EKSNodeRole
```

```shell 
aws iam attach-role-policy \
  --policy-arn arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly \
  --role-name EKSNodeRole
```

```sell
aws iam attach-role-policy \
  --policy-arn arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy \
  --role-name EKSNodeRole
```
  
