
# frontend 툴 설치 

https://nodejs.org/en


# aa 프젝트 만들기 

npx create-turbo@latest aa


# admin-portal 생성 

cd /Users/mz03-jmryu/Desktop/Project/aicc/aa/apps

 cp -r web admin-portal
 cp -r web user-portal


# name 수정 
/Users/mz03-jmryu/Desktop/Project/aicc/aa/apps/user-portal/package.json


# pnpm install 

cd /Users/mz03-jmryu/Desktop/Project/aicc/aa
pnpm install 

# pnpm dev 

pnpm dev 

# 참조 

https://github.com/vercel/turbo/tree/main/examples/basic



# Front-end 작업 환경 초기 설정

## ROOT 초기 설치

- [nodejs](https://nodejs.org/en/download) : v18
- [pnpm](https://pnpm.io/ko/installation) : v8
- [Turborepo](https://turbo.build/repo/docs/installing) : 설치 안해도 개발은 가능
- [VSCode](https://code.visualstudio.com/download) : 추천하는 extensions은 설치할 것
- [git]() : 내부망 링크 막혀있음

## 개발 환경 초기 설정

```bash
$ git config --global user.name "Your Name"
$ git config --global user.email you@example.com
```

### Seegene wisdom 프로젝트 클론

- ex VSCode : 커멘드 팔렛트 오픈(command + shift + p)
- 'Git: clone' 입력 후 clone 주소 입력 
- 연결 후 추천하는 extensions은 설치할 것

이후

```bash
pnpm install

pnpm dev
```

### directory

- apps : Front pages
  - admin-portal
  - service-portal
  - user-portal
  - storybook
- packages : 공통 모듈
  - ui : 공통 컴포넌트 모음
  - editor : 공통 에디터
  - library : 공통 라이브러리

---

## 배포 참조

[GitHub Action](https://turbo.build/repo/docs/ci/github-actions)

```sh
pnpm i
pnpm build
```

- admin-portal s3 : ./apps/admin-portal/out/
- service-portal s3 : ./apps/service-portal/out/
- user-portal s3 : ./apps/user-portal/out/

- 
