package cn.itrip.auth.service;

public interface MailService {
    //发送邮件
    public void sendActivetionMail(String mailTo,String activetionCode);
}
