package com.junduo.server;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.junduo.mapper.DevicesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName WebSocketServer
 * Description: WebSocket是类似于客户端服务端的形式（采用ws协议），此处的WebSocketServer其实相当于
 *              ws协议的一个Controller
 * Create by JBuck
 * Date 2022/3/10 14:58
 * @author JBuck
 */
@ServerEndpoint("/controller/{deviceId}")
@Component
public class WebSocketServer {

    @PostConstruct
    public void init(){
        /**
         * 重新上线初始化全部数据
         */
        devicesMapper.flushAll();
        /**
         * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象
         */
        devicesMapper.initDevicesMap();
        /**
         * DevicesMaxTimeMap包含了每一个设备的最长绿灯时间
         */
        devicesMapper.initDevicesMaxTimeMap();
        /**
         * 每次重启服务都会初始化在线设备数为0
         */
        devicesMapper.initOnlineCount();
        logger.info("WebSocket-redis数据库初始化成功");
    }
    /**
     * 初始化日志对象
     */
    static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private static DevicesMapper devicesMapper;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();


    /**
     * 与某个设备建立的连接会话，需要保存session以用来发送信息
     */
    private Session session;
    /**
     * 建立连接的设备的id
     */
    private String deviceId;

    @Autowired
    public void setDevicesMapper(DevicesMapper devicesMapper){
        WebSocketServer.devicesMapper = devicesMapper;
    }


    /**
     * 建立连接成功调用的方法
     * @param session   设备的session
     * @param deviceId  设备的id
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("deviceId") String deviceId){
        this.session = session;
        this.deviceId = deviceId;
        if(webSocketMap.containsKey(deviceId)){
            webSocketMap.remove(deviceId);
            webSocketMap.put(deviceId, this);
        }else {
            webSocketMap.put(deviceId, this);
            // 将开启的设备数量放入redis中
            devicesMapper.addOnlineCount();
            // 将设备的开启状态放入redis中
            devicesMapper.putDMap(deviceId, 1);
            // 为连接的设备设置其绿灯最长时间
            devicesMapper.putDMTMap(deviceId, 60);
            // 生成设备的初始控制状态 deviceControllers
            // redis: key:deviceId+Controller, value:Integer(0:异常离线，1：正常运行，2：控制终止进入自主运行)
            devicesMapper.initDeviceControllers(deviceId);

        }

        logger.info("设备编号:"+deviceId+" 接入成功,当前在线设备数量为:" + devicesMapper.getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            logger.error("设备 :"+deviceId+",网络异常离线，请尽快排查");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(deviceId)){
            webSocketMap.remove(deviceId);
            // 将关闭的设备数量放入redis中
            devicesMapper.subOnlineCount();
            // 将设备的关闭状态放入redis中
            devicesMapper.putDMap(deviceId, 0);
            // 设备离线，将该设备redis中相关信息删除
            devicesMapper.flushDeviceInfo(deviceId);
        }
        logger.info("设备编号:"+deviceId+" 离线,当前在线设备数量为:" + devicesMapper.getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *  客户端发来的信息应为json格式,例如{"fromDeviceId":"xxxx","cars":"xx"}
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.debug("用户消息:"+this.deviceId+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(! StringUtils.isEmpty(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                String warm = jsonObject.getString("warm");
                Integer lightNum = jsonObject.getInteger("lightNum");

                if(! StringUtils.isEmpty(warm)){
                    devicesMapper.setDeviceWarm(deviceId, warm);
                    logger.warn("[WARM]("+deviceId+")"+warm);
                }else if(! StringUtils.isEmpty(lightNum.toString())){
                    devicesMapper.initDeviceCars(deviceId, lightNum);
                }
            }catch (Exception e){
                e.printStackTrace();
                logger.error("[ERROR]onMessage模块发生错误");
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error){
        logger.error("[ERROR]("+deviceId+")原因："+error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送消息
     * @param message   需要推送的消息
     * @throws IOException  错误
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static void sendInfo(){

    }
}
