package com.itheima.reggie.common;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Value;

public class SMSUtils {


    public static  void sendCheckCode(String signName,String phoneNumbers,String param){
        try {
            Config config = new Config()
                    // 您的AccessKey ID
                    .setAccessKeyId("LTAI5tMgrBKer7G3Ad3z7nvR")
                    // 您的AccessKey Secret
                    .setAccessKeySecret("01uJWcl5kZdXxCxHvR9LSbBttLSYi0");
            // 访问的域名
            config.endpoint = "dysmsapi.aliyuncs.com";
            Client client = new Client(config);
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setSignName("阿里云短信测试")
                    .setTemplateCode("SMS_154950909")
                    .setPhoneNumbers(phoneNumbers)
                    .setTemplateParam("{\"code\":\"" + param +"\"}");
            // 复制代码运行请自行打印 API 的返回值
            client.sendSms(sendSmsRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
