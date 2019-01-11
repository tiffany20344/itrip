package cn.itrip.auth.test;

import cn.itrip.common.RedisAPI;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RedisAPITest {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("app*.xml");
        RedisAPI api=(RedisAPI)ctx.getBean("redisAPI");
        api.set("name","xiaoming");
        System.out.print("值a---"+api.get("name"));
        System.out.print("值b---"+api.exist("name"));
        System.out.print("值c---"+api.ttl("name"));

    }
}
