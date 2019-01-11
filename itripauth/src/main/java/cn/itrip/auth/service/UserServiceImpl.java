package cn.itrip.auth.service;

import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.common.MD5;
import cn.itrip.common.RedisAPI;
import cn.itrip.dao.user.ItripUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private ItripUserMapper userMapper;

    //@Resource
    //private SmsService smsService;

    @Resource
    private RedisAPI redisAPI;

    @Resource
    private SmsService smsService;

    @Resource
    private MailService mailService;
    /**
     * 用户名密码验证
     * @param userCode
     * @param userPassword
     * @return
     * @throws Exception
     */
    @Override
    public ItripUser login(String userCode, String userPassword) throws Exception {
        ItripUser user = this.findUserByUserCode(userCode);
        if(null!=user&&user.getUserPassword().equals(userPassword)){
            if(user.getActivated()!=1) {
                throw new Exception("用户未激活");
            }else{
                return user;
            }
        }
        else
            return null;
    }

    @Override
    public ItripUser findUserByUserCode(String userCode) throws Exception {
        Map<String,Object>  hashMap =  new HashMap<String,Object>();
        hashMap.put("userCode",userCode);
        List<ItripUser> list = userMapper.getItripUserListByMap(hashMap);
        ItripUser user =null;
        for(int i=0;i<list.size();i++){
             user = list.get(i);
        }
        return  user;

    }

    @Override
    public void itriptxCreateUserByPhone(ItripUser user) throws Exception {
        //1.创建用户
        userMapper.insertItripUser(user);
        //2.生成验证码（1111-9999）
        int code = MD5.getRandomCode();
        //3.发送验证码
        smsService.send(user.getUserCode(),"1",new String[]{String.valueOf(code),"51"});
        //4.缓存验证码到Redis
        redisAPI.set("activation"+user.getUserCode(),5*60,String.valueOf(code));
    }

    //短信验证
    public boolean validatePhone(String phoneNum,String code) throws Exception{
        //1.比对验证码
        String key = "activation" + phoneNum;
        String value = redisAPI.get(key);
        if(null!=value&&value.equals(code)) {
            ItripUser user = this.findUserByUserCode(phoneNum);
            if(null!=user) {
                //2.更新用户激活状态
                user.setActivated(1);
                user.setFlatID(user.getId());
                user.setUserType(0);
                //userMapper.updateItripUser(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public void itripMailCreateUser(ItripUser user) throws Exception {
        //1.创建用户
        userMapper.insertItripUser(user);
        //2.生成激活码
        String code = MD5.getMd5(new Date().toLocaleString(),32);
        //3.发送邮件
        mailService.sendActivetionMail(user.getUserCode(),code);
        //4.激活码存入redis
        redisAPI.set("activation"+user.getUserCode(),30*60,code);
    }

    @Override
    public boolean validateMail(String mail, String code) throws Exception {
        //1.比对验证码
        String key = "activation" + mail;
        String value = redisAPI.get(key);
        if(null!=value&&value.equals(code)) {
            ItripUser user = this.findUserByUserCode(mail);
            if(null!=user) {
                //2.更新用户激活状态
                user.setActivated(1);
                user.setFlatID(user.getId());
                user.setUserType(0);
                userMapper.updateItripUser(user);
                return true;
            }
        }
        return false;
    }
}
