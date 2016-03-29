# predix-rdbr-cf

Predix Labs example with RabbitMq, Database and Redis cache services
The example contains Rest controller to call database, RabbitMq and Redis services.  The services configuration uses libraries:
- spring-boot-starter-data-jpa
- spring-boot-starter-redis
- spring-boot-starter-amqp
- spring-cloud-cloudfoundry-connector

######Please see details in the Developer notes.

##Project structure

   ``` 
├── LICENSE.md
├── README.md
├── manifest.yml
├── pom.xml
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── ge
    │   │           └── predix
    │   │               └── labs
    │   │                   └── data
    │   │                       └── jpa
    │   │                           ├── Application.java
    │   │                           ├── config
    │   │                           │   ├── CloudFoundryDataSourceConfiguration.java
    │   │                           │   └── ServicesConfiguration.java
    │   │                           ├── domain
    │   │                           │   ├── Barber.java
    │   │                           │   ├── Customer.java
    │   │                           │   └── Visit.java
    │   │                           ├── service
    │   │                           │   ├── BarberService.java
    │   │                           │   ├── CustomerService.java
    │   │                           │   └── VisitService.java
    │   │                           └── web
    │   │                               ├── BarberController.java
    │   │                               ├── CacheController.java
    │   │                               ├── CustomerApiController.java
    │   │                               ├── RabbitMqController.java
    │   │                               └── VisitController.java
    │   └── resources
    │       └── initialCustomers.sql
    └── test

   ``` 
#### CloudFoundryDataSourceConfiguration.java:
 -  extends AbstractCloudConfig class to get the ConnectionFactory object associated with the bound services Postgress, RabbitMq and Redis
 
  ```
@SuppressWarnings("deprecation")
@Configuration
public class CloudFoundryDataSourceConfiguration extends AbstractCloudConfig  {

    @Bean
    public DataSource dataSource() {
    		return connectionFactory().dataSource();
    }
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return connectionFactory().redisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() throws Exception {
        RedisTemplate<String, Object> ro = new RedisTemplate<String, Object>();
        ro.setConnectionFactory(redisConnectionFactory());
        return ro;
    }
    
    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean( DataSource dataSource  ) throws Exception {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource( dataSource );
        em.setPackagesToScan(Customer.class.getPackage().getName());
        em.setPersistenceProvider(new HibernatePersistence());
        Map<String, String> p = new HashMap<String, String>();
        p.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "create");
        p.put(org.hibernate.cfg.Environment.HBM2DDL_IMPORT_FILES, "initialCustomers.sql");
        p.put(org.hibernate.cfg.Environment.DIALECT, PostgreSQLDialect.class.getName());
        p.put(org.hibernate.cfg.Environment.SHOW_SQL, "true");
        em.setJpaPropertyMap(p);
        return em;
    }

    @Bean
    public CacheManager cacheManager() throws Exception {
        return new RedisCacheManager(redisTemplate());
    }
    
	@Bean
	public RabbitTemplate rabbitTemplate() {
		return new RabbitTemplate(connectionFactory().rabbitConnectionFactory());
	}

}
	
   ``` 
   
#### ServiceConfiguration.java
 - Enable Transaction Manager 

   ```  
@Configuration
@EnableCaching
@EnableTransactionManagement
@ComponentScan(basePackageClasses = {CustomerService.class})
public class ServicesConfiguration {

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) throws Exception {
        return new JpaTransactionManager(entityManagerFactory);
    }
} 

   ```  
## Installation
 - clone repository  
    `>git clone https://github.com/PredixDev/predix-rdbr-cf.git`
 - check that you have on your market space postgres, rabbitmq and redis services 
 
    `>cf m`
   
   ``` 
   Getting services from the marketplace in org sergey.vyatkin@ge.com / space dev as sergey.vyatkin@ge.com...
   OK

   service                    plans       description   
   business-operations        beta        Upgrade your service using a subscription-based business model.   
   logstash-3                 free        Logstash 1.4 service for application development and testing   
   p-rabbitmq                 standard    RabbitMQ is a robust and scalable high-performance multi-protocol messaging broker.  
   postgres                   shared      Reliable PostgrSQL Service   
   redis-1                    shared-vm   Redis service to provide a key-value store   
   riakcs                     developer   An S3-compatible object store based on Riak CS 
   ...
   ```
 - create new services for postgres and redis like 
    ``` 
     >cf cs postgres shared postgres_sv 
     >cf cs  redis-1  shared-vm redis_sv 
     >cf cs  p-rabbitmq  standard rabbitmq_sv 
    ``` 
###### application dynamically detects bond services.  You do not need to change a code. 

 - deploy application 
 
  ```
    >cd predix-rdbr-cf
    
    >mvn clean package
    
    >cf push 
    
  ```
## Use a browser to test app's REST: 

- test customers

 [http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/customers] (http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/customers)

   ```  
 returns json list of customer from resources/initialCustomers.sql
 [{"id":785001,"name":"Sam","phone":"(925)-123-4567","tstamp":1447719635998},
 {"id":785002,"name":"Sergey","phone":"(925)-223-4567","tstamp":1447719636007},
 {"id":785003,"name":"Robert","phone":"(925)-423-4567","tstamp":1447719636015},
 {"id":785004,"name":"Alex","phone":"(925)-523-4567","tstamp":1447719636018},
 {"id":785005,"name":"Savva","phone":"(925)-623-4567","tstamp":1447719636020},
 {"id":785006,"name":"Josh","phone":"(925)-723-4567","tstamp":1447719636022},
 {"id":785007,"name":"Patrick","phone":"(925)-823-4567","tstamp":1447719636025},
 {"id":785008,"name":"Andy","phone":"(925)-923-4567","tstamp":1447719636027},
 {"id":785009,"name":"Eric","phone":"(925)-013-4567","tstamp":1447719636030},
 {"id":785010,"name":"Chris","phone":"(925)-023-4567","tstamp":1447719636032},
 {"id":785011,"name":"Raj","phone":"(925)-033-4567","tstamp":1447719636034},
 {"id":785012,"name":"Vic","phone":"(925)-043-4567","tstamp":1447719636037},
 {"id":785013,"name":"Rich","phone":"(925)-053-4567","tstamp":1447719636040},
 {"id":785014,"name":"Mark","phone":"(925)-063-4567","tstamp":1447719636042}] 
 
 http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/customers/{id}
 returns customer by id number 
 {"id":785001,"name":"Sam","phone":"(925)-123-4567","tstamp":1447719635998}
    ```  
 
 [http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/search?q=J] (http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/search?q=J)
    ```  
 returns all customers containing letter "J" 
 [{"id":785006,"name":"Josh","phone":"(925)-723-4567","tstamp":1447719636022},
 {"id":785011,"name":"Raj","phone":"(925)-033-4567","tstamp":1447719636034}]
   ```  

- test barber Shop

  [http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/barbers] (http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/barbers)

   ```  
  
   [{"id":945015,"name":"Jovanni","hairCutPrice":15.2,"tstamp":1448063020415},
   {"id":945016,"name":"Marchello","hairCutPrice":24.99,"tstamp":1448063020419}]
   
   http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/barbersearch?q=March
   [{"id":945016,"name":"Marchello","hairCutPrice":24.99,"tstamp":1448063020419}]
   ```  
   
- test Barber Shop Simulator

 [http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/visitsseries] (http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/visitsseries)
  
   ```  
   returns visits log
   1448307363066: Barber Shop starts to work
   1448307363066: Jovanni start to work
   1448307363066: Marchello start to work
   1448307363069: Sam is comming in a queue
   1448307363270: Sergey is comming in a queue
   1448307363470: Robert is comming in a queue
   1448307363671: Alex is comming in a queue
   1448307363871: Savva is comming in a queue
   1448307364071: Josh is comming in a queue
   1448307364272: Patrick is comming in a queue
   1448307364472: Andy is comming in a queue
   1448307364673: Eric is comming in a queue
   1448307364873: Chris is comming in a queue
   1448307365073: Raj is comming in a queue
   1448307365273: Vic is comming in a queue
   1448307365474: Rich is comming in a queue
   1448307365674: Mark is comming in a queue
   
   ```  
- test visits in the Barber Shop   

   [http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/visits] (http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/visits)

   ```
   [{"id":945017,"customerName":"Sam","customerPhone":"(925)-123-4567","barberName":"Jovanni","startTimeVisit":1448063020422,"endTimeVisit":1448063020422,"hairCutPrice":15.2},
   {"id":945018,"customerName":"Sergey","customerPhone":"(925)-223-4567","barberName":"Marchello","startTimeVisit":1448063020425,"endTimeVisit":1448063020425,"hairCutPrice":24.99},
   {"id":945019,"customerName":"Bob","customerPhone":"(925)-323-4567","barberName":"Marchello","startTimeVisit":1448063020429,"endTimeVisit":1448063020429,"hairCutPrice":15.2},
   ...
   {"id":945089,"customerName":"Mark","customerPhone":"(925)-063-4567","barberName":"Jovanni","startTimeVisit":1448307367121,"endTimeVisit":1448307367721,"hairCutPrice":15.2}]
   ```  
- test Cache - show all caches 
  [http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/cache] (http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/cache) 

   ```

customers - Cache contains records: 

1365001:Name:Sam:Phone:(925)-123-4567
1365011:Name:Raj:Phone:(925)-033-4567
1365012:Name:Vic:Phone:(925)-043-4567

visits - Cache contains records: 

1345017:Barber:Jovanni:Customer:Sam:Price:15.2:Date:2015/11/30 18:53:44
1345019:Barber:Marchello:Customer:Bob:Price:24.99:Date:2015/11/30 18:53:44

barbers - Cache contains records: 

1345015:Name:Jovanni:Price:15.2

   ```
- test Cache by Cache name 
  [http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/cache/customers] (http://predix-rdbr-sv.run.aws-usw02-pr.ice.predix.io/cache/customers)

   ```

customers - Cache contains records: 

1365001:Name:Sam:Phone:(925)-123-4567
1365011:Name:Raj:Phone:(925)-033-4567
1365012:Name:Vic:Phone:(925)-043-4567
   ```

#### Developer notes:

 - To load in eclipse you may use [SpringSource Tool Suite - STS](https://spring.io/tools/sts/all)  
  ```
  >mvn eclipse:clean eclipse:eclipse  
  
  open eclipse and use the following commands:
  File/Import/General/Existing Projects/Browse to predix-rdbr-cf dir   
  ```
 - Maven library dependency for Database, RabbitMq and Redis services:
    ```
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-redis</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-amqp</artifactId>
		</dependency>


		<!-- CloudFoundry -->

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-cloudfoundry-connector</artifactId>
			<version>${spring-cloud.version}</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-spring-service-connector</artifactId>
			<version>${spring-cloud.version}</version>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
    ```
