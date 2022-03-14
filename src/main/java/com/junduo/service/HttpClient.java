package com.junduo.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * ClassName HttpClient
 * Description
 * Create by JBuck
 * Date 2022/3/11 18:40
 * @author JBuck
 */
@Service
public class HttpClient {

    public JSONObject client(String url, JSONObject jsonObject){
        RestTemplate template = new RestTemplate();
        ResponseEntity<JSONObject> response = template.postForEntity(url, jsonObject, JSONObject.class);
        return response.getBody();
    }
}
