# EKS Load Balancer Controller 구성 방법

!!!! note AWS EKS (Elastic Kubernetes Service)에서 Amazon Load Balancer Controller를 활용한 Ingress를 구성하는 방법
 

AWS EKS Document : https://docs.aws.amazon.com/eks/latest/userguide/alb-ingress.html

 !!!! note [ 사전 준비 ] Bastion 작업 서버에 프로그램 설치 : aws-cli, eksctl, kubectl, helm, git

#### 1. EKS Cluster 접속

```

aws eks --region ap-northeast-2 update-kubeconfig --name test-eks --profile dev-ryu 

``` 

#### 2. OIDC 공급자 확인

```
aws eks describe-cluster --name test-eks --query "cluster.identity.oidc.issuer" --output text
```


#### 2.1 OIDC 공급자가 없을 경우 생성

```s

eksctl utils associate-iam-oidc-provider --cluster test-eks --approve

```


#### 3. kubernetes iamserviceaccount를 위한 policy 생성

```
aws iam create-policy \
    --policy-name AWSLoadBalancerControllerIAMPolicy \ 
    --policy-document \
    https://raw.githubusercontent.com/kubernetes-sigs/aws-load-balancer-controller/main/docs/install/iam_policy.json
```

#### 4. kubernetes iamserviceaccount 생성

```s

eksctl create iamserviceaccount \
  --cluster=test-eks \
  --namespace=kube-system \
  --name=aws-load-balancer-controller \
  --attach-policy-arn=arn:aws:iam::123456789000:policy/AWSLoadBalancerControllerIAMPolicy \
  --override-existing-serviceaccounts \
  --approve

```

### 5. 생성된 iamserviceaccount 확인

```
eksctl get iamserviceaccount \
    --cluster test-eks \
    --name aws-load-balancer-controller \
    --namespace kube-system
```


#### 6. Helm에 EKS Repository 추가

```
kubectl apply -k "github.com/aws/eks-charts/stable/aws-load-balancer-controller//crds?ref=master"

helm repo add eks https://aws.github.io/eks-charts
```


#### 7. AWS Load Balancer Controller 배포

```s

helm install aws-load-balancer-controller eks/aws-load-balancer-controller \
  -n kube-system \
  --set clusterName=test-eks \
  --set serviceAccount.create=false \
  --set serviceAccount.name=aws-load-balancer-controller 

``` 

```s
helm list -n kube-system                        
NAME                            NAMESPACE       REVISION        UPDATED                                 STATUS          CHART                                   APP VERSION
aws-load-balancer-controller    kube-system     1               2023-02-20 07:50:17.264857 +0900 KST    deployed        aws-load-balancer-controller-1.4.7      v2.4.6  
```

```s

helm uninstall aws-load-balancer-controller -n kube-system
```

#### 8. AWS Load Balancer Controller 배포 확인

kubectl get deployment -n kube-system aws-load-balancer-controller
 

#### 9. 이후 deployment, service, ingress를 EKS Cluster에 적용 후 AWS Console > EC2 > Load Balancers에서 ALB가 생성되었는지 확인

kubectl rollout restart deployment -n kube-system coredns

kubectl get pod -n kube-system

#### 10. argocd 설치해 검증 해 보자 


helm repo add argo https://argoproj.github.io/argo-helm

kubectl create namespace argo

helm install -f argo-cd-value.yaml argo -n argo argo/argo-cd

!!!! note helm upgrade -f argo-cd-value.yaml argo -n argo argo/argo-cd 

```yaml 
nodeSelector:
  role: devops
server:
  extraArgs:
    - --insecure #http 사용시
```    

!!! note kubectl apply -f argocd-ingress.yaml 

```yaml 
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: argo-cd
  namespace: argo
  annotations:
    kubernetes.io/ingress.class: alb
    alb.ingress.kubernetes.io/load-balancer-name: argo-cd
    alb.ingress.kubernetes.io/target-type: ip
    alb.ingress.kubernetes.io/listen-ports: '[{"HTTPS":443}]'
    alb.ingress.kubernetes.io/scheme: internal
    alb.ingress.kubernetes.io/subnets: subnet-a, subnet-b
    alb.ingress.kubernetes.io/certificate-arn: arn:aws:acm:ap-northeast-2:123456789000:certificate/123456789000
spec:
  rules:
  - http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: argo-argocd-server
            port:
              number: 443
```              



!!! note kubectl -n argo get service

```s
NAME                                    TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)             AGE
argo-argocd-applicationset-controller   ClusterIP   172.20.81.87     <none>        7000/TCP            23m
argo-argocd-dex-server                  ClusterIP   172.20.77.222    <none>        5556/TCP,5557/TCP   23m
argo-argocd-redis                       ClusterIP   172.20.69.58     <none>        6379/TCP            23m
argo-argocd-repo-server                 ClusterIP   172.20.58.239    <none>        8081/TCP            23m
argo-argocd-server                      ClusterIP   172.20.43.104    <none>        80/TCP,443/TCP      23m
argo-server                             ClusterIP   172.20.159.232   <none>        2746/TCP            39m
```


