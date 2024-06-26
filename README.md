# A few examples of how to handle high volume traffic in Spring Boot
1. [Use @Async](#AsyncExample)
2. [Use @Cacheable](#Cacheable)
3. [Use Compression](#Compression)
4. [Other Options](#OtherOptions)
5. [UI test automation](#UITest)

<a name="AsyncExample"></a>
## Use @Async
* Async Test URL: `http://localhost:8080/async`
* Sync Test URL: `http://localhost:8080/sync`  
To improve `@Async` performance, additional ThreadPool configuration is required
```java
@Configuration
public class AsyncConfig {
    @Bean(name="taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        executor.setMaxPoolSize(1000);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
```
* Use `CompletableFuture<~>` return type for `@Async` method
```java
@Service
public class AsyncService {
    @Async("taskExecutor")
    public CompletableFuture<String> asyncHello() {
        return CompletableFuture.completedFuture("Hello from Async service");
    }
}
```
* Also need to annotate the configuration class with `@EnableAsync`


### Test result - Jmeter
* Number of Threads (users): 5000
* Ramp-up period(seconds): 10
* Loop Count: 1

The result is clear. Using `@Async` service provides consistent response time until the end of performance test while sync service performance goes down dramatically

#### Sync Service
![img/async_off_result.pn](img/async_off_result.png)
#### Async Service
![img/async_on_result.pn](img/async_on_result.png)

<a name="Cacheable"></a>
## Use @Cacheable 
* Test URL: `http://localhost:8080/cached`  
Calls the following Service component with `@Cacheable` annotation
```java
@Service
public class CachedService {

    @Cacheable(value="cached-single")
    public String getHello(){
        System.out.println("Fetching and cache the data:"+System.currentTimeMillis());
        try{
            Thread.sleep(1000);
        }catch( InterruptedException e){
            Thread.currentThread().interrupt();
        }

        return "Cached Hello";
    }
}
```
* Also need to add `@EnableCaching` annotation on configuration class
* Following configuration must be added in application.yml file
```yaml
spring:
  cache:
    type: simple
    cache-names:        # This 'cache-names' can limit the name spaces for Cache. If this property doesn't exist, it will allow all. If it exists, only the listed names will be allowed to cache
    - cached-single
```

### Test environment - Jmeter
__Note__: This is local machine test, so it cannot measure as standard result
* Number of Threads(users): 6000
* Ramp-up period(seconds): 10
* Loop Count: 1

__Result__: You will see latency drops down dramatically after a few hundred requests. It needs to pass a few hundreds request because of `Thread.sleep` setting.

<a name="Compression"></a>
## Use Compression 
* Test URL: `http://localhost:8080/compressed`

Add following configuration in application.yml
```yaml
server:
  compression:
    enabled: true
    min-response-size: 1024
    mime-types: application/json,application/xml,text/html,text/xml,text/plain
```
Then call `http://localhost:8080/compressed` with `Accept-Encoding: gzip` in the Request Header.
You will see the response body `Bytes` size difference
* Result with header `Accept-Encoding: gzip` on  
![img/compressed_on_response_header_result.png](img/compressed_on_response_header_result.png)
![img/compressed_on_result.png](img/compressed_on_result.png)
* Result without header `Accept-Encoding: gzip` on
![img/compressed_off_result.png](img/compressed_off_result.png)

<a name="OtherOptions"></a>
## Other Options
### Paginate Returning result
Use `Pageable` option when getting the data from Database
### Optimize Database Connection Pool
Set the hikari database connection pool to optimize the DB connection
### Use Actuator to check the performance/status
`localhost:8080/actuator/metrics` has a list of measurable end point such as `localhost:8080/actuator/metrics/jvm.memory.used` is showing how much memory has been used by JVM.

<a name="UITest"></a>
## UI automation test
* This can be done with JMeter and Blazemeter Chrome plugin  
Record the action by Blazemeter and open the save actions on JMeter for testing