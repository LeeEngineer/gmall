package com.atguigu.gmall.message.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.gmall.message.config.SmsProperties;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lee_engineer
 * @create 2020-08-04 19:03
 */
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class MessageListener {

    @Autowired
    private SmsProperties smsProperties;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "MESSAGE-SEND-QUEUE", durable = "true"),
            exchange = @Exchange(value = "COMMON-MESSAGE-EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = "message.*"
    ))
    public void msgListener(String msgInfo, Channel channel, Message message) {

        try {
            Map map = JSONObject.parseObject(msgInfo, Map.class);
            System.out.println(map);
            String code = (String) map.get("code");
            String phone = (String) map.get("phone");
            if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(phone)) {
                this.sendMsg(code,phone);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (message.getMessageProperties().getRedelivered()){
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(),false);
                }else {
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private void sendMsg(String code, String phone) throws Exception {

        DefaultProfile profile = DefaultProfile.getProfile(smsProperties.getRegionId(), smsProperties.getAccessKeyId(), smsProperties.getSecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", smsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", smsProperties.getSignName());
        request.putQueryParameter("TemplateCode", smsProperties.getTemplateCode());

        HashMap<String, String> param = new HashMap<>();
        param.put("code", code);
        String jsonParam = JSON.toJSONString(param);

        request.putQueryParameter("TemplateParam", jsonParam);

        CommonResponse response = client.getCommonResponse(request);
        System.out.println(response.getData());


    }

}
