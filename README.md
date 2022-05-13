# Spring WebFlux で Functional Endpoints を使う

Spring WebFlux に含まれている、`Functional Endpoints` を試す。

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

## Publisher / Subscriber を作る

[前回作成した Spring WebFlux の例](https://github.com/t-ita/springWebFlux) の Publisher / Subscriber から、RequestMapping
のアノテーションを削除

Publisher.java

```java

@Component
public class Publisher {
  Mono<String> greeting() {
    return Mono.just("Hello, WebFlux!");
  }

  Flux<Long> numberStream() {
    return Flux.interval(Duration.ofMillis(500)); // 0.5秒毎に数値を0からカウントアップして返す
  }
}
```

Subscriber.java

```java

@Component
public class Subscriber {
  private final WebClient client;

  public Subscriber(WebClient.Builder builder) {
    this.client = builder.baseUrl("http://localhost:8080").build();
  }

  public Mono<String> subscribeGreeting() {
    return client.get().uri("publish/greeting").accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String.class)
            .map(s -> "Subscribe: " + s);
  }

  public Flux<String> fizzBuzz() {
    return client.get().uri("publish/numberstream").accept(MediaType.APPLICATION_NDJSON)
            .retrieve()
            .bodyToFlux(Long.class)
            .map(aLong -> (aLong % 3 == 0 ? "Fizz" : "") + (aLong % 5 == 0 ? "Buzz" : aLong % 3 == 0 ? "" : aLong));
  }
}
```

## HandlerFunction を作る

リクエストを受け取ってレスポンスを返す関数を作成する。

PublisherHandler

```java

@Component
public class PublisherHandler {
  private final Publisher publisher;

  public PublisherHandler(Publisher publisher) {
    this.publisher = publisher;
  }

  public Mono<ServerResponse> greeting(ServerRequest request) {
    return ServerResponse
            .ok()
            .body(publisher.greeting(), String.class);
  }

  public Mono<ServerResponse> numberstream(ServerRequest request) {
    return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_NDJSON)
            .body(publisher.numberStream(), Long.class);
  }
}
```

SubscriberHandler

```java

@Component
public class SubscriberHandler {
  private final Subscriber subscriber;

  public SubscriberHandler(Subscriber subscriber) {
    this.subscriber = subscriber;
  }

  public Mono<ServerResponse> greeting(ServerRequest request) {
    return ServerResponse
            .ok()
            .body(subscriber.subscribeGreeting(), String.class);
  }

  public Mono<ServerResponse> fizzbuzz(ServerRequest request) {
    return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_NDJSON)
            .body(subscriber.fizzBuzz(), Long.class);
  }
}
```

## RouterFunction を作る

エンドポイントをマッピングする、Router関数を作成する

PublisherRouter

```java

@Component
public class PublisherRouter {
  @Bean
  public RouterFunction<ServerResponse> publisherRoute(PublisherHandler publisherHandler) {
    return RouterFunctions.route()
            .path("/publish", builder -> builder
                    .GET("/greeting", publisherHandler::greeting)
                    .GET("/numberstream", publisherHandler::numberstream)
            )
            .build();
  }
}
```

SubscriberRouter

```java

@Component
public class SubscriberRouter {
  @Bean
  public RouterFunction<ServerResponse> subscriberRoute(SubscriberHandler subscriberHandler) {
    return RouterFunctions.route()
            .path("/subscribe", builder -> builder
                    .GET("/greeting", subscriberHandler::greeting)
                    .GET("/fizzbuzz", subscriberHandler::fizzbuzz)
            )
            .build();
  }
}
```

これで完成。

## 他の書き方

HandlerFunction は、関数インターフェースとして以下の様に定義されている。

```java

@FunctionalInterface
public interface HandlerFunction<T extends ServerResponse> {
  Mono<T> handle(ServerRequest request);
}
```

単純に ServerRequest を受け取って、ServerResponse を返すだけのシンプルな関数。なので、Lamda式で書いても構わない。</br>
また、複数のルーターも一つにまとめる事ができるので、上記 Handler / Router は、以下の様にして一つのクラスにまとめる事ができる

RouterConfigure.java

```java

@Configuration
public class RouterConfigure {
  @Bean
  public RouterFunction<ServerResponse> appRoute(Publisher publisher, Subscriber subscriber) {
    return RouterFunctions.route()
            .path("/publish", builder -> builder
                    .GET("/greeting", request -> ServerResponse
                            .ok()
                            .body(publisher.greeting(), String.class))
                    .GET("/numberstream", request -> ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_NDJSON)
                            .body(publisher.numberStream(), Long.class))
            )
            .path("/subscribe", builder -> builder
                    .GET("/greeting", request -> ServerResponse
                            .ok()
                            .body(subscriber.subscribeGreeting(), String.class))
                    .GET("/fizzbuzz", request -> ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_NDJSON)
                            .body(subscriber.fizzBuzz(), Long.class))
            )
            .build();
  }
}
```

## 参考サイト

[Functional Endpoints](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-fn)</br>
[Introduction to the Functional Web Framework in Spring 5](https://www.baeldung.com/spring-5-functional-web)</br>