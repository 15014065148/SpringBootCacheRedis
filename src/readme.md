####Spring boot Spring Cache+Redis
* 利用缓存中间件提供缓存
* 通过注解来实现缓存功能

这里我们利用redis作为缓存中间件

  pom.xml 引入相关jar包
  ```
    	<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    	</dependencies>
  ```
RedisConfig 配置redis相关缓存
```
@Configuration
@CacheConfig
public class RedisConfig {

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        //设置缓存失效时间,单位秒,key是redis缓存的key
        Map<String, Long> expires = new HashMap<>();
        expires.put("getCount2", 60L);
        expires.put("getCount", 60L);
        expires.put("getCount4", 60L);

        cacheManager.setExpires(expires);
        return cacheManager;
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        final RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //设置值序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //设置键序列化
        template.setKeySerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory factory) {
        final StringRedisTemplate template = new StringRedisTemplate(factory);
        return template;
    }
```
在Controller层缓存
```$xslt
@RestController
public class Controller {
    @RequestMapping("/")
    @Cacheable(key = "'getCount_'+#p0",value = "getCount")
    public String getCount(String key){
        Random random = new Random();
        return random.nextInt(100)+"";
    }

    @RequestMapping("/2")
    @Cacheable(value = "getCount2")
    public String getCount2(String key){
        Random random = new Random();
        Optional<String> integer = Optional.of(key);
        if (integer.isPresent()){
            System.out.println(integer.get());
        }else {
            System.out.println("no key");
        }
        return random.nextInt(100)+"";
    }
    @RequestMapping("/3")
    @CachePut(value = "getCount2")
    public String getCount3(String key){
        Random random = new Random();
        System.out.println(key);
        return random.nextInt(100)+"";
    }

    @RequestMapping("/4")
    @Cacheable(value = "getCount4",condition = "#key.age>10",unless = "#result>50")
    public Integer getCount4(User key){
        Random random = new Random();
        System.out.println(key);
        return random.nextInt(100);
    }
    @RequestMapping("/5")
    @CacheEvict(value = "getCount4",allEntries = true)
    public String deleteCount4(){
        System.out.println("delete getCount4 ");
        return "delete getCount4 ";
    }
}
```
 注解@Cacheable 从缓存中去取，如果没有，则执行该注解标识的方法，再根据条件决定是否放入缓存。
 value 缓存名,默认为方法名。key 缓存中的键，默认为参数值。key支持SpEL表达式
 '#p0' 表示第一个参数。condition 表示满足条件才取缓存。unless 表示取出结果后除去满足该条件的值
 
 注解@CachePut 更新缓存 一般在插入或更新语句中加盖注解， value 缓存名,默认为方法名。
 key 缓存中的键，默认为参数值。key支持SpEL表达式
'#p0' 表示第一个参数。condition 表示满足条件放入缓存。unless 表示取出结果后除去满足该条件的值时，放入缓存
 注解@CacheEvict 删除缓存 allEntries=true 表示删除该缓存中所有的缓存
 
 一般缓存都在DAO层做，根据自己的需求，去决定在哪层做缓存。
 
 
  


























