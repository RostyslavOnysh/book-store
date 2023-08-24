# book-store

## Hw-1
An entity named "Book" was created with the following additional fields:
  * id (Long, PK)
  * title (String, not null)
  * author (String, not null)
  * isbn (String, not null, unique)
  * price (BigDecimal, not null)
  * description (String)
  * coverImage (String)

### For the fields where "not null" is required annotation was utilized : 
- *@Column(nullable = false)*

### The following components were implemented:

* BookRepository along with its implementation
* Book service

### Upon establishing a connection with MySQL, I encountered an issue with the CI (Continuous Integration) checks. To address this problem, I adhered to the prescribed steps: 

I  Followed the rules
1. Create a new folder resources in the src/test directory
2. Create a new file application.properties in the src/test/resources folder
3. Add the following content to the application.properties file
```bash 
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2D
```

And add this dependencies to *pom.xml*
```xml
<dependency>
<groupId>com.h2database</groupId>
<artifactId>h2</artifactId>
<scope>test</scope>
</dependency>
```