package cn.itrip.auth.test;

import cn.itrip.auth.service.UserService;
import cn.itrip.beans.pojo.ItripUser;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MailTest {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("app*.xml");
        UserService userService= (UserService) ctx.getBean("userServiceImpl");
        ItripUser user =new ItripUser();
        user.setUserCode("1326100799@qq.com");
        user.setUserName("test");
        try {
            userService.itripMailCreateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
