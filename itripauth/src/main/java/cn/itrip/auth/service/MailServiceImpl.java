package cn.itrip.auth.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MailServiceImpl implements  MailService {

    @Resource
    private SimpleMailMessage activationMailMessage;

    @Resource
    private MailSender mailSender;

    public void sendActivetionMail(String mailTo, String activetionCode) {
            activationMailMessage.setTo(mailTo);
            activationMailMessage.setText("您的激活码是:"+activetionCode);
            mailSender.send(activationMailMessage);
    }
}
