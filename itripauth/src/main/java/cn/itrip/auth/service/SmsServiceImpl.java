package cn.itrip.auth.service;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.springframework.stereotype.Service;

import java.util.HashMap;

;

@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public void send(String to, String templateId, String[] datas) throws Exception {
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init("app.cloopen.com","8883");
        sdk.setAccount("8a216da867e886be01682116f15300f0","94059ee7608f4a18aa8d1e567b12a834");
        sdk.setAppId("8a216da867e886be01682116f17300f1");
        HashMap result = sdk.sendTemplateSMS(to, templateId, datas);
/*        if("000000".equals(result.get("statusCode"))){
            //正常返回输出data包体信息（map）
            HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
            Set<String> keySet = data.keySet();
            for(String key:keySet){
                Object object = data.get(key);
                System.out.println(key +" = "+object);
            }
        }else{
            //异常返回输出错误码和错误信息
            System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
        }*/
        if("000000".equals(result.get("statusCode")))
            System.out.println("短信发送成功");
        else
            throw new Exception(result.get("statusCode")+":"+result.get("statusMsg").toString());
    }
}
