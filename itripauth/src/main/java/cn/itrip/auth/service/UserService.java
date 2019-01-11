package cn.itrip.auth.service;

import cn.itrip.beans.pojo.ItripUser;

public interface UserService {
    public ItripUser login(String userCode, String userPassword) throws Exception;
    public ItripUser findUserByUserCode(String userCode) throws Exception;
    public void itriptxCreateUserByPhone(ItripUser user) throws Exception;
    public boolean validatePhone(String phoneNum,String code) throws Exception;
    public void itripMailCreateUser(ItripUser user) throws Exception;
    public boolean validateMail(String mail,String code) throws Exception;
}
