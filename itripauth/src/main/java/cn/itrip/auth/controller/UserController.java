package cn.itrip.auth.controller;

import cn.itrip.auth.service.UserService;
import cn.itrip.beans.dto.Dto;
import cn.itrip.beans.pojo.ItripUser;
import cn.itrip.beans.vo.userinfo.ItripUserVO;
import cn.itrip.common.DtoUtil;
import cn.itrip.common.ErrorCode;
import cn.itrip.common.MD5;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.regex.Pattern;

/**
 * 用户管理控制器
 * @author hduser
 *
 */
@Controller
@RequestMapping(value = "/api")
public class UserController {
    @Resource
    private UserService userService;


    @RequestMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    /**
     * 使用邮箱注册
     * @param userVO
     * @return
     */
    @RequestMapping(value="/doregister",method=RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public Dto doRegister(@RequestBody ItripUserVO userVO) {
        //1.邮箱验证
        if(this.validEmail(userVO.getUserCode())==false)

                return DtoUtil.returnFail("请输入正确的邮箱", ErrorCode.AUTH_ILLEGAL_USERCODE);

        //2.调用service
        ItripUser user = new ItripUser();
        user.setUserCode(userVO.getUserCode());
        user.setUserName(userVO.getUserName());
        try {
            if(null!= userService.findUserByUserCode(user.getUserCode())){
                return DtoUtil.returnFail("用户已经存在",ErrorCode.AUTH_USER_ALREADY_EXISTS);
            }else {
                user.setUserPassword(MD5.getMd5(userVO.getUserPassword(),32));
                /*int randomCode = MD5.getRandomCode();
                String[] abc=new String[]{""+randomCode+"","1"};
                smsService.send(vo.getUserCode(),"1",abc);*/

                userService.itripMailCreateUser(user);
                return DtoUtil.returnSuccess();
            }
        } catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(),ErrorCode.AUTH_UNKNOWN);
        }
    }

    /**
     * 使用手机注册
     * @param userVO
     * @return
     */
    @RequestMapping(value="/registerbyphone",method=RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public  Dto registerByPhone(@RequestBody ItripUserVO userVO){
        //1.手机验证
        if(this.validPhone(userVO.getUserCode())==false)

            return DtoUtil.returnFail("请使用正确的手机号码", ErrorCode.AUTH_ILLEGAL_USERCODE);

        //2.调用service
        ItripUser user = new ItripUser();
        user.setUserCode(userVO.getUserCode());
        user.setUserName(userVO.getUserName());
        try {
            if(null!= userService.findUserByUserCode(user.getUserCode())){
                return DtoUtil.returnFail("用户已经存在",ErrorCode.AUTH_USER_ALREADY_EXISTS);
            }else {
                user.setUserPassword(MD5.getMd5(userVO.getUserPassword(),32));
                /*int randomCode = MD5.getRandomCode();
                String[] abc=new String[]{""+randomCode+"","1"};
                smsService.send(vo.getUserCode(),"1",abc);*/

                userService.itriptxCreateUserByPhone(user);
                return DtoUtil.returnSuccess();
            }
        } catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail(e.getMessage(),ErrorCode.AUTH_UNKNOWN);
        }

    }

    /**
     * 检查用户是否已注册
     * @param name
     * @return
     */
    @RequestMapping(value="/ckusr",method=RequestMethod.GET,produces= "application/json")
    public @ResponseBody
    Dto checkUser(@RequestParam String name) {
        try {
            if (userService.findUserByUserCode(name)==null)
                return DtoUtil.returnSuccess("用户名可用");
            else
                return DtoUtil.returnSuccess("用户名不可用");
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("验证失败",ErrorCode.AUTH_UNKNOWN);
        }
    }

    /**
     * 邮箱激活
     * @param user
     * @param code
     * @return
     */
    @RequestMapping(value="/activate",method=RequestMethod.PUT,produces= "application/json")
    public @ResponseBody Dto activate(
            @RequestParam String user,
            @RequestParam String code){
        try {
            if (userService.validateMail(user,code))
                return DtoUtil.returnSuccess("验证成功");
            else
                return DtoUtil.returnSuccess("验证失败");
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("验证失败",ErrorCode.AUTH_UNKNOWN);
        }
    }


    @RequestMapping(value="/validatephone",method=RequestMethod.PUT,produces= "application/json")
    public @ResponseBody Dto validatePhone(
            @RequestParam String user,
            @RequestParam String code){
        try {
            if (userService.validatePhone(user,code))
                return DtoUtil.returnSuccess("验证成功");
            else
                return DtoUtil.returnSuccess("验证失败");
        }catch (Exception e){
            e.printStackTrace();
            return DtoUtil.returnFail("验证失败",ErrorCode.AUTH_UNKNOWN);
        }

    }




    /**			 *
     * 合法E-mail地址：
     * 1. 必须包含一个并且只有一个符号“@”
     * 2. 第一个字符不得是“@”或者“.”
     * 3. 不允许出现“@.”或者.@
     * 4. 结尾不得是字符“@”或者“.”
     * 5. 允许“@”前的字符中出现“＋”
     * 6. 不允许“＋”在最前面，或者“＋@”
     */
    private boolean validEmail(String email){

        String regex="^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$"  ;
        return Pattern.compile(regex).matcher(email).find();
    }
    /**
     * 验证是否合法的手机号
     * @param phone
     * @return
     */
    private boolean validPhone(String phone) {
        String regex="^1[3578]{1}\\d{9}$";
        return Pattern.compile(regex).matcher(phone).find();
    }
}
