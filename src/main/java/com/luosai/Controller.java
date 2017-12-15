package com.luosai;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Random;

/**
 * Created by sai.luo on 2017-12-13.
 */
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
    @CachePut(value = "getCount4",condition = "#key.age>10",unless = "#result>50")
    public Integer getCount5(User key){
        Random random = new Random();
        System.out.println(key);
        return random.nextInt(100);
    }
    @RequestMapping("/6")
    @CacheEvict(value = "getCount4",allEntries = true)
    public String deleteCount4(){
        System.out.println("delete getCount4 ");
        return "delete getCount4 ";
    }
}
