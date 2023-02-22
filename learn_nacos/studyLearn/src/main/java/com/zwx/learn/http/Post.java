package com.zwx.learn.http;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class Post {

    private  String  MACHINE_CODE = "5619b0a4-40b2-40a2-9c3b-094a51ccd4fd";
    /**
     * doPost实体参数发送方
     */
    @Test
    public void doPostTestThree(){
        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Post请求
        HttpPost httpPost = new HttpPost("https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key="+MACHINE_CODE);

        /*{
            "msgtype":"text",
                "text":
                {
            "content":"广州今日天气：29度，大部分多云，降雨概率：60%",
                    "mentioned_list":["wangqing", "@all"],
            "mentioned_mobile_list":["13800001111", "@all"]
        }
        }*/
        //模拟body体
        //外面大的json
        JSONObject jsonObject  = new JSONObject();
        jsonObject.put("msgtype","text");
        //里面小的
        JSONObject jsonObject1  = new JSONObject();
        jsonObject1.put("content","广州今日天气：29度，大部分多云，降雨概率：60%");

        ArrayList  arrayList = new ArrayList();
        arrayList.add("wangqing");
        arrayList.add("@all");
        jsonObject1.put("mentioned_list",arrayList);

        ArrayList  arrayList1 = new ArrayList();
        arrayList1.add("13800001111");
        arrayList1.add("@all");
        jsonObject1.put("mentioned_mobile_list",arrayList1);
        jsonObject.put("text",jsonObject1);

        String jsonString = jsonObject.toString();
        StringEntity entity = new StringEntity(jsonString, "UTF-8");
        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);
        //消息头
        httpPost.setHeader("Content-Type", "application/json");

        // 响应模型
        CloseableHttpResponse response = null;

        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            //返回信息
            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
