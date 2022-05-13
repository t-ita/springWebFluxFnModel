# Spring WebFlux で関数プログラミングモデル

## 環境構築

### Spring Initializr

下記設定で、Spring プロジェクトを作成

- Project
  - Grade Project
- Language
  - Java
- Spring Boot
  - 2.6.7
- Project Metadata
  - Group: com.example
  - Artifact: springWebFlux
  - Name: springWebFlux
  - Package name: com.example.springwebflux
  - Packaging: jar
  - Java: 17
- Dependencies
  - Spring Reactive Web
  - Lombok

生成された `build.gradle` は以下

```groovy
plugins {
  id 'org.springframework.boot' version '2.6.7'
  id 'io.spring.dependency-management' version '1.0.11.RELEASE'
  id 'java'
}

group = 'com.example'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '17'

configurations {
  compileOnly {
    extendsFrom annotationProcessor
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-webflux'
  compileOnly 'org.projectlombok:lombok'
  annotationProcessor 'org.projectlombok:lombok'
  testImplementation 'org.springframework.boot:spring-boot-starter-test'
  testImplementation 'io.projectreactor:reactor-test'
}

tasks.named('test') {
  useJUnitPlatform()
}
```

### jEnv

Java 環境を Jenv で指定

```
jenv local 17.0.3
```

### プロジェクトの初期状態を Github に登録

```
git init
git commit -m "first commit"
git remote add origin git@github.com:xxx.git
git push -u origin master
```
