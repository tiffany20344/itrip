package cn.itrip.auth.test;

import cn.itrip.auth.service.SmsService;
import cn.itrip.auth.service.SmsServiceImpl;
import cn.itrip.common.MD5;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SmsTest {
    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("app*.xml");
        SmsService smsService = (SmsServiceImpl) ctx.getBean("smsServiceImpl");
        String code [] = {""+ MD5.getRandomCode()+"","1"};
        smsService.send("17673466864","1",code);
    }
}
