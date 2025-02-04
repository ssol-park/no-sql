## Ref
- DataType: https://redis.io/docs/latest/develop/data-types/
- Config: https://www.baeldung.com/spring-data-redis-properties
- 레디스 정리: https://velog.io/@dodozee/Redis
---
### Redis 직렬화 종류 및 특징

#### 1. StringRedisSerializer
- 데이터를 **UTF-8 문자열로 변환**하여 저장하는 방식.
- Key와 Value를 **문자열 그대로 저장**하므로, **Redis CLI에서 쉽게 조회 가능** (`GET key` 사용 가능).
- **객체를 직렬화하는 것이 아니라, 단순한 문자열을 저장하는 경우 적합**.
- 데이터가 단순 문자열로 저장되므로, **객체의 식별 정보(Object Identity) 유지가 불가능**.

📌 **주의할 점**
- 숫자 데이터를 저장할 때, 가져올 때도 문자열로 반환됨 → `Integer`로 변환하는 추가 과정 필요.
- Java 객체를 직렬화하지 않기 때문에, 객체 필드나 상태를 유지할 수 없음.

✅ **추천 사용 사례**: 단순 Key-Value 저장이 필요한 경우  
🚫 **비추천 사례**: Java 객체를 저장해야 하는 경우

---

#### 2. JdkSerializationRedisSerializer
- Java의 기본 직렬화 방식(`Serializable` 인터페이스 사용)으로 **객체를 바이트 배열로 변환**하여 저장.
- **Spring Boot의 기본 직렬화기**이며, 별도 설정 없이 사용 가능.
- 직렬화된 데이터는 **바이너리 형태**로 저장되므로, Redis CLI에서 직접 읽을 수 없음.

📌 **주의할 점**
- **객체 식별 정보(Object Identity) 문제 발생 가능**
    - `serialVersionUID`가 다르면 역직렬화(`Deserialization`) 과정에서 **InvalidClassException**이 발생할 수 있음.
    - 즉, **클래스의 필드가 변경되거나 새로운 필드가 추가되면, 기존에 저장된 데이터와 호환되지 않을 수 있음**.
    - 직렬화된 객체를 불러올 때, 원본 객체와 동일한 해시코드(`hashCode()`)를 보장할 수 없음.

✅ **추천 사용 사례**: Java 객체를 저장하면서, Spring Boot 기본 설정을 유지하고 싶을 때  
🚫 **비추천 사례**: Redis CLI에서 데이터를 직접 조회해야 하는 경우, 다른 언어와의 호환성이 필요한 경우

---

#### 3. Jackson2JsonRedisSerializer
- Jackson을 사용하여 객체를 **JSON 형식으로 변환**하여 저장.
- JSON 기반이므로 **가독성이 뛰어나며, 다른 언어(JavaScript, Python 등)와의 호환성이 우수**.
- 객체를 JSON 문자열로 변환하므로, Redis CLI에서도 쉽게 확인 가능.

📌 **주의할 점**
- **객체 식별 정보가 유지되지 않음**
    - Jackson은 기본적으로 객체 ID를 저장하지 않으므로, **동일한 객체를 저장했다가 불러오면, 원본 객체와는 다른 새로운 객체가 생성됨**.
    - 해결 방법: `@JsonIdentityInfo` 어노테이션을 추가하여 ID 정보를 유지 가능.

  ```java
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  public class User {
      private Long id;
      private String name;
  }
  ```

- **객체 필드 변경 시 문제 발생 가능**
    - JSON 직렬화는 **객체의 필드명이 변경되거나 추가되더라도 역직렬화 시 크게 영향을 받지 않음**.
    - 하지만, 필드 타입이 변경되면 역직렬화 오류가 발생할 수 있음.

✅ **추천 사용 사례**: 데이터의 가독성이 중요하고, 다른 언어(JavaScript, Python)와의 호환이 필요한 경우  
🚫 **비추천 사례**: 객체의 ID를 유지하면서 저장해야 하는 경우

---

#### 4. GenericJackson2JsonRedisSerializer
- `Jackson2JsonRedisSerializer`와 유사하지만, 보다 **범용적인 JSON 직렬화를 지원**.
- 여러 종류의 객체를 저장할 수 있도록 설계됨.

📌 **주의할 점**
- **클래스 타입 정보가 포함되어 JSON 크기가 커질 수 있음**
    - 이 직렬화기는 기본적으로 객체의 클래스 타입 정보를 포함하여 저장하므로, **JSON 크기가 상대적으로 커질 수 있음**.
    - 클래스 정보가 포함되므로, **다른 언어에서 JSON을 읽을 때 불필요한 메타데이터가 포함될 수 있음**.

✅ **추천 사용 사례**: 다양한 타입의 객체를 Redis에 저장해야 할 때  
🚫 **비추천 사례**: JSON 데이터 크기를 최소화해야 하는 경우

---

### 2. 객체 식별(Object Identity) 문제 및 해결 방법

#### 객체 ID 유지 방식
1. **Java 기본 직렬화(`JdkSerializationRedisSerializer`)**
    - `serialVersionUID`를 사용하여 객체의 변경을 감지할 수 있음.
    - 하지만, `serialVersionUID`가 변경되면 **역직렬화 오류 발생 가능**.

2. **JSON 직렬화(`Jackson2JsonRedisSerializer`)**
    - 기본적으로 객체 ID를 저장하지 않음 → `@JsonIdentityInfo`를 사용하여 해결 가능.
    - 하지만, JSON 기반 직렬화는 객체를 새로 생성하는 방식이므로, 동일 객체 보장이 어려울 수 있음.

3. **객체 해시코드(`hashCode()`) 문제**
    - `JdkSerializationRedisSerializer`를 사용할 경우, 객체를 저장하고 다시 불러오면 **해시코드가 다를 수 있음**.
    - 이는 Java의 기본 `hashCode()` 구현이 객체의 메모리 주소를 기반으로 하기 때문.

---