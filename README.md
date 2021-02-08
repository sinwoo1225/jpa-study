# JPA 스터디

자바 ORM 표준 JPA 강의 정리 및 스터디 [강의링크](https://www.inflearn.com/course/ORM-JPA-Basic/)

---

## JPA 시작

#### pom.xml

JPA를 사용하기 위해 Maven의 의존성을 추가해야한다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>jpa-study</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>
    <dependencies>
        <!-- JPA 하이버네이트 -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>5.3.10.Final</version>
        </dependency>
        <!-- H2 데이터베이스 -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>1.4.199</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.16.20</version>
            <scope>provided</scope>
        </dependency>

    </dependencies>
</project>
```

#### persitance.xml

JPA를 사용하기 위해서는 `/META-INF/persistence.xml` 파일을 설정해야 한다. jdbc 설정 및 하이버네이트의 경우 방언과 같은 실행옵션을 설정할 수 있다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2"
             xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
    <persistence-unit name="hello">
        <properties>
            <!-- 필수 속성 -->
            <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="javax.persistence.jdbc.user" value="sa"/>
            <property name="javax.persistence.jdbc.password" value=""/>
            <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>

            <!-- 옵션 -->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.use_sql_comments" value="true"/>
            <!--<property name="hibernate.hbm2ddl.auto" value="create" />-->
        </properties>
    </persistence-unit>
</persistence>
``` 

### JPA 기본사용법

#### Member.java

```java

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

}
```

`@Entity`어노테이션을 통해 JPA가 관리할 영속성 객체임을 표시한다.
`@Id`는 테이블의 기본키(PK)와 매핑한다.

#### 생성,수정,삭제,조회

```java
public class JpaStudy {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try{
            // 생성
            Member member = new Member();
            member.setName("시루떡");
            em.persist(member);
            em.flush(); // ✅ 명시적으로 DB에 쿼리를 보냄

            // 수정
            member.setName("시루");
            em.flush();

            // 삭제
            em.remove(member);
            em.flush();

            // 단건 조회
            member = em.find(Member.class, 1L );
            System.out.println(member);

            tx.commit();
        } catch(Exception ex) {
            tx.rollback();
            ex.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
```
JPA를 이용하기 위해서는 `EntityMangerFactory`를 생성하고 `EntityManager`를 실행해야한다. 그리고 데이터 변경은
트랜잭션 안에서 실행되어야한다.
`EntityManager`의 `persist`를 통해 JPA가 객체를 영속성 컨텍스트에 등록한다. 그리고 트랜잭션이 닫히거나 영속성 컨택스트가 flush될때 쿼리를 
DB에게 보낸다. 영속성 컨택스트에서 관리되는 객체를 수정하게 되면 DB에 반영이 된다. `EntityManager`의 `remove`를 통해 객체 그리고 객체와
연관되어있는 테이블 행을 삭제할 수 있다. 