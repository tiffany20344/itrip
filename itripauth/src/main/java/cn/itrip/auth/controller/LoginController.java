package cn.itrip.auth.controller;

import cn.itrip.auth.service.TokenService;
import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.beans.vo.ItripTokenVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.EmptyUtils;
import cn.itrip.common.ErrorCode;
import cn.itrip.common.MD5;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

@Controller
@RequestMapping("/api")
public class LoginController {

    @Resource
    private TokenService tokenService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/dologin",method = RequestMethod.POST,produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public Dto login(@RequestParam("name") String name, @RequestParam("password") String password, HttpServletRequest request){
        try {
            String pwd = MD5.getMd5(""+password+"",32);
            ItripUser user = userService.login(name, MD5.getMd5(""+password+"",32));
            if (EmptyUtils.isNotEmpty(user)){
                String userAgent = request.getHeader("user-agent");
                String token = tokenService.generateToken(userAgent,user);
                tokenService.save(token,user);
                ItripTokenVO vo = new ItripTokenVO(token,
                        Calendar.getInstance().getTimeInMillis()+2*60*60*1000
                        , Calendar.getInstance().getTimeInMillis());
                //return DtoUtil工具类
                return DtoUtil.returnDataSuccess(vo);
            }else{
                //登录失败
                return DtoUtil.returnFail("用户密码错误", ErrorCode.AUTH_AUTHENTICATION_FAILED);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(),ErrorCode.AUTH_AUTHENTICATION_FAILED);
        }
    }

    @RequestMapping(value ="/logout",method = RequestMethod.GET,produces = {"application/json;charset=utf-8"},headers = "token")
    @ResponseBody
    public Dto logout(HttpServletRequest request){
        String token = request.getHeader("token");
        try {
            if (tokenService.vaildate(request.getHeader("user-agent"),token)){
                tokenService.delete(token);
                return DtoUtil.returnSuccess("退出成功");
            }else{
                return DtoUtil.returnFail("token无效",ErrorCode.AUTH_TOKEN_INVALID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DtoUtil.returnFail("退出失败",ErrorCode.AUTH_TOKEN_INVALID);
        }
    }
}
