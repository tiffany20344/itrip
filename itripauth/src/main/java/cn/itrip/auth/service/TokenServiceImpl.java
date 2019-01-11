package cn.itrip.auth.service;

import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.common.MD5;
import cn.itrip.common.RedisAPI;
import com.alibaba.fastjson.JSON;
import nl.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Resource
    private RedisAPI redisAPI;

    /**
     * 生成Token字符串
     * 前缀PC-USERCODE-USERID-CREATIONDATE-RONDEM[6位]
     * @param userAgent
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public String generateToken(String userAgent, ItripUser user) throws Exception {
        StringBuilder str = new StringBuilder();
        str.append("token:");
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        if(agent.getOperatingSystem().isMobileDevice())
            str.append("MOBILE-");
        else
            str.append("PC-");
        str.append(MD5.getMd5(user.getUserCode(),32)+"-");
        str.append(user.getId().toString()+"-");
        str.append(new SimpleDateFormat("yyyyMMddHHmmsss").format(new Date())+"-");
        str.append(MD5.getMd5(userAgent,6));
        return str.toString();
    }

    /**
     * 保存token信息
     * @param token
     * @param user
     * @throws Exception
     */
    public void save(String token,ItripUser user) throws Exception {
        if(token.startsWith("token:PC-")){
            redisAPI.set(token,2*60*60, JSON.toJSONString(user));
        }else{
            redisAPI.set(token, JSON.toJSONString(user));
        }
    }

    /**
     * 验证token
     * @param userAgent
     * @param token
     * @return
     * @throws Exception
     */
    public boolean vaildate(String userAgent, String token) throws Exception {
        if (!redisAPI .exist(token))
            return false;
        String agen=token.split("-")[4];
        if (!MD5.getMd5(userAgent,6).equals(agen))
            return false;
        return  true;
    }

    @Override
    public void delete(String token) throws Exception {
        redisAPI.delete(token);
    }
}
