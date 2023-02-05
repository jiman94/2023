# Fast api 는 비동기 방식의 web server framework
# uvicorn은 비동기 방식의 http server -> ASCI

#### 1) fastapi 및 uvicorn 설치
- 프레임워크 및 웹서버가 생성

```shell
pip install fastapi 'uvicorn[standard]'
```

#### 2) main.py 파일 생성 후 아래의 간단한 코드를 작성하여 저장

```python
from fastapi import FastAPI

app = FastAPI()

@app.get("/") 
def root():
    return {'Hello':'World!'} 

```

#### 3) 서버 생성

```shell 

uvicorn main:app --reload

uvicorn main:app --port 8080 --workers 3

```

- main : 모듈명
- app : FastAPI 인스턴스 생성
- reload : 새로 고침
