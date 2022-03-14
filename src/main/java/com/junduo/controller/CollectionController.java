package com.junduo.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.junduo.mapper.DevicesMapper;
import com.junduo.server.WebSocketServer;
import com.junduo.service.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * ClassName CollectionController
 * Description: 负责对图片传输的处理，为路口摄像头提供服务，不属于测试类
 * Create by JBuck
 * Date 2022/1/24 20:34
 * @author JBuck
 */
@Controller
public class CollectionController {
    @Autowired
    private DevicesMapper devicesMapper;
    @Autowired
    private HttpClient httpClient;

    static Logger logger = LoggerFactory.getLogger(CollectionController.class);


    /**
     * (核心代码之一)
     * 建立一个统一的对外url接口，负责对接收到的json字符串中包含的图片进行车数识别，并返回车辆数量
     * @param jsonParams    外部传入的json字符串
     * @return  返回含有车数量的json字符串
     */
    @RequestMapping(value = "/device/detection", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String detection(@RequestBody JSONObject jsonParams){
        String deviceId = (String) jsonParams.get("deviceId");
        Integer lightIndex = (Integer) jsonParams.get("lightIndex");

        // 使用post访问外部链接，并得到返回的信息
        String url = "http://127.0.0.1:8000/";
        JSONObject jsonObject = httpClient.client(url, jsonParams);

        Integer cars = jsonObject.getInteger("cars");
        // 单独测试此链接时，下方代码可能出错
        try{
            devicesMapper.setDeviceCars(deviceId, lightIndex, cars);
        }catch (RedisSystemException e){
            e.printStackTrace();
            logger.error("devicesMapper.setDeviceCars(deviceId, lightIndex, cars) 测试错误");
        }
        logger.info("[INFO]("+deviceId+" "+ lightIndex +") cars: "+cars);

        // 补充deviceId, lightIndex到返回的json中
        jsonObject.put("deviceId", deviceId);
        jsonObject.put("lightIndex", lightIndex);
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "/device/controller/{deviceId}",method = RequestMethod.GET)
    @ResponseBody
    public String getController(@PathVariable("deviceId") String deviceId){
        JSONObject jsonObject = new JSONObject();
        Integer state = devicesMapper.getDeviceControllers(deviceId);
        jsonObject.put(deviceId, state);
        logger.info("[INFO]获取状态码: "+deviceId+"-->"+state);
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "/device/controller/{deviceId}/{state}",method = RequestMethod.POST)
    @ResponseBody
    public void setController(@PathVariable("deviceId") String deviceId,
                              @PathVariable("state") Integer state){
        devicesMapper.setDeviceControllers(deviceId, state);
        logger.info("[INFO]切换状态码: "+deviceId+"-->"+state);
    }

    @RequestMapping(value = "/device/maxGreenTime/{deviceId}",method = RequestMethod.GET)
    @ResponseBody
    public String getDeviceMaxGreenTime(@PathVariable String deviceId){
        JSONObject jsonObject = new JSONObject();
        Integer maxGreenTime = devicesMapper.getDMTMap(deviceId);
        logger.info("[INFO]获取最长绿灯时间: "+deviceId+" GreenTime --> "+maxGreenTime);
        jsonObject.put(deviceId, maxGreenTime);
        return jsonObject.toJSONString();
    }

}
