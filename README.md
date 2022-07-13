# ARTXEW
## Spring Boot Skeleton Project

[http://132.226.23.193:8080/swagger-ui/index.html[profile=dev]](http://132.226.23.193:8080/swagger-ui/index.html)

## Environment
> VsCode 1.68.0
>> Extension Pack for Java
>
>> Spring Boot Extension Pack
>
>> Lombok Annotations Support for VS Code

> OpenJDK Temurin-17.0.1+12
>> Spring Framework 2.7.0
>
>> Gradle 7.4

> ORACLE Cloud
>> Oracle Linux Server 8.6
>
>> Linux 5.4.17-2136.307.3.1.el8uek.x86_64
>
>> PostgreSQL v10.21
>
>> Redis server v5.0.3
>
>> elasticsearch v8.3.1
>
>> java-17-openjdk-devel.x86_64

## DB Tables
```sql
CREATE SEQUENCE sq_example__eid;

CREATE TABLE EXAMPLE (
    EID INTEGER PRIMARY KEY DEFAULT nextval('sq_example__eid')
    , TXT VARCHAR(40) NOT NULL
    , CRT_TM TIMESTAMP DEFAULT transaction_timestamp()
    , UDT_TM TIMESTAMP DEFAULT transaction_timestamp()
);

INSERT INTO EXAMPLE (TXT)
SELECT uuid_in(md5(random()::TEXT || now()::TEXT)::CSTRING)
FROM generate_series(1, 123456);
```

### launch.json
```json
{
    "version": "0.3.7",
    "configurations": [
        {
            "type": "java",
            "name": "Launch BootApplication",
            "request": "launch",
            "mainClass": "artxew.boot.BootApplication",
            "projectName": "artxew",
            "vmArgs": [
                "--add-opens",
                "java.base/java.lang=ALL-UNNAMED",
                "--add-opens",
                "java.base/java.util=ALL-UNNAMED",

                "-Dspring.profiles.active=prod",
                "-Denvironment.password=artxew-enc-key"
            ]
        },
        {
            "type": "java",
            "name": "Launch Current File",
            "request": "launch",
            "mainClass": "${file}",
            "projectName": "artxew",
            "vmArgs": [
                "--add-opens",
                "java.base/java.lang=ALL-UNNAMED",
                "--add-opens",
                "java.base/java.util=ALL-UNNAMED",
                
                "-Dspring.profiles.active=local",
                "-Denvironment.password=artxew-enc-key"
            ]
        }
    ]
}
```
### settings.json
```json
{
    "java.configuration.updateBuildConfiguration": "automatic"
}
```

## Features
> TO DO
>> google login
>
>> naver login
>
>> kakao login

> Spring Boot Base

> Config( Resources )
>> src/main/resources/config/application.yml: 스프링 공통 설정
>
>> src/main/resources/config/application-xxx.yml: 액티브 프로파일에 따른 스프링 설정
>
>> src/main/resources/arguments/environment-arguments.yml: 스프링 설정 이외의 환경변수 설정
>
>> src/main/resources/exception/defined-exception.yml: 익셉션에 따른 스테이터스 코드와 메시지 설정
>
>> src/main/resources/config/ehcache/ehcache-config.xml: EH캐시 설정
>
>> src/main/resources/config/logback/logback-config.xml: 로그백 설정
>
>> src/main/resources/config/mybatis/mybatis-config.xml: 마이바티스 설정
>
>> src/main/resources/sql/\**/**.xml: SQL 파일 위치
>
>> src/main/resources/excel/\**/**.*: 엑셀 템플릿 관리
>
>> src/main/resources/public/**: 정적 자원 위치

> Config( Java )
>> AsyncConfig.java: 비동기 설정
>
>> EhcacheConfig.java: EH캐시 설정
>
>> ElasticsearchConfig.java: 일래스틱서치 설정
>
>> JasyptConfig.java: application.yml 암호화 설정
>
>> MybatisConfig.java: 마이바티스 및 트랜잭션 관련 설정
>
>> RedisConfig.java: 레디스 세션 설정
>
>> SwaggerConfig.java: 스웨거 사용을 위한 설정
>
>> WebMvcConfig.java: 404에러, 인터셉터, 리소스 핸들러 등 Web Mvc 관련 설정

> Framework-Decedent
>> BaseAsyncProcessor: 비동기 작업시 actionWithFinally 기능 제공
>
>> BaseController: 컨트롤러 작업시 공통 DTO 포맷과 에러 처리 기능 제공
>
>> BaseService: 서비스 작업시 에러 처리 기능 제공
>
>> PageReqDto: 목록조회 공통 요청 객체
>
>> PageResDto: 목록조회 공통 응답 객체
>
>> ServerResponseDto: REST API 공통 응답 객체, 상태 코드 설정 기능 제공

> Framework-Environment
>
>> AuthCheck: 권한 체크 어노테이션
>
>> CommonDao: 공통 DAO 컴포넌트
>
>> DefinedException: defined-exception.yml 파일을 통한 익셉션 처리 기능
>
>> FlowLog**: AOP 기반 시그니쳐 로그 기능
>
>> SessionGroupHolder: 웹소켓 세션 관리 기능
>
>> WebSocketUi: [ /websocket-ui/index.html ] 웹소켓 테스트 페이지
>
>> WsApi: WebSocketUi 태그명 정의 어노테이션

> Framework-Layers
>> OAuthCtrl: 세션 로그인, JWT 로그인, 소셜 로그인 등의 기능

> Framework-Util
>> ContextUtil: Spring Context 외의 영역에서 Spring Bean을 사용하기 위한 유틸
>
>> MailSender: 자바 메일 발송 유틸 (to, cc, bcc, File, MultipartFile)
>
>> SessionMap: 로그인 기능 및 세션 값을 전역에서 가져오기 위한 유틸
>
>> StreamResponseWriter: 파일 다운로드, 압축 파일 다운로드, 엑셀 다운로드 유틸
>
>> StringUtil: SHA3-256 해싱, Json 등 문자열 관련 기능
>
>> UtilsFieldsProvider: Util 클래스 필드변수 주입을 위한 클래스

> Project-Layers
>> Example: 기능 구현 예제