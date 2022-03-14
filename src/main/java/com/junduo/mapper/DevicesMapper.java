package com.junduo.mapper;

import com.junduo.server.WebSocketServer;
import com.junduo.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName RedisMapper
 * Description
 * Create by JBuck
 * Date 2022/3/10 15:22
 * @author JBuck
 */
@Repository
public class DevicesMapper {

    @Autowired
    private RedisUtil redisUtil;

    private Logger logger = LoggerFactory.getLogger(DevicesMapper.class);

    public boolean flushAll(){
        return redisUtil.flushAll();
    }

    public boolean flushDeviceInfo(String deviceId){
        try{
            // 此处不删除redis中的warm信息，便于设备离线之后查看错误
            redisUtil.del(deviceId+"Cars");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 初始化设备在线session的map
     */
    public void initDevicesMap(){
        try{
            Map<String, Object> devicesMap = new ConcurrentHashMap<>();
            redisUtil.hmset("devicesMap", devicesMap);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("DevicesMapper发生错误");
        }
    }

    /**
     * 获取设备在线session的map
     */
    public Map<Object, Object> getDMap(){
        try{
            return redisUtil.hmget("devicesMap");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void removeDMap(String key){
        redisUtil.hdel("devicesMap", key);
    }

    public void putDMap(String key, Object value){
        redisUtil.hset("devicesMap", key, value);
    }

    public Integer getDMap(String key){
        return (Integer) redisUtil.hget("devicesMap", key);
    }

    public boolean containsKeyDMap(String key){
        return redisUtil.hHashKey("devicesMap", key);
    }

    /**
     * 初始化在线设备数
     */
    public int initOnlineCount(){
        redisUtil.set("onlineCount", 0);
        return 0;
    }

    /**
     * 获取当前在线的设备数
     */
    public int getOnlineCount(){
        return (Integer) redisUtil.get("onlineCount");
    }

    /**
     * 增加当前的在线设备数
     */
    public void addOnlineCount(){
        redisUtil.incr("onlineCount", 1);
    }

    /**
     * 减少当前的在线设备数
     */
    public void subOnlineCount(){
        redisUtil.decr("onlineCount", 1);
    }

    //========================DeviceCars========================

    public void initDeviceCars(String deviceId, Integer lightNum){
        for(int i=0; i<lightNum; i++){
            redisUtil.lSet(deviceId+"Cars", 0);
        }
        logger.info("[INFO](" + deviceId + ") 交通灯数量 " + lightNum + " 初始化完成");
    }

    public boolean setDeviceCars(String deviceId,Integer lightIndex, Integer cars){
        try{
            return redisUtil.lUpdateIndex(deviceId+"Cars",lightIndex.longValue(), cars);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Integer getDeviceCars(String deviceId, Integer lightIndex){
        return (Integer) redisUtil.lGetIndex(deviceId+"Cars", lightIndex.longValue());
    }

    //====================DeviceWarm===========================

    public boolean setDeviceWarm(String deviceId, String warmMsg){
        return redisUtil.lSet(deviceId+"Warm", warmMsg);
    }

    public List<Object> getDeviceWarm(String deviceId, long start, long end){
        return redisUtil.lGet(deviceId+"Warm", start, end);
    }

    /**
     * 用于在前端管理页面删除特定的报错信息
     * @param deviceId  需要删除报错信息的设备
     * @param index     为负数时删除所有信息，>=0时删除特定索引的信息
     */
    public void removeDeviceWarm(String deviceId, long index){
        if(index >= 0){
            redisUtil.lUpdateIndex(deviceId+"Warm", index, "null");
            redisUtil.lRemove(deviceId+"Warm", 0, "null");
        }else{
            redisUtil.del(deviceId+"Warm");
        }
    }
    // =============================DeviceControllers===================
    /**
     * 初始化设备运行状态
     */
    public int initDeviceControllers(String deviceId){
        redisUtil.set(deviceId+"Controller", 1);
        return 1;
    }

    /**
     * 获取设备的运行状态
     */
    public int getDeviceControllers(String deviceId){
        return (Integer) redisUtil.get(deviceId+"Controller");
    }

    /**
     * 设置设备的运行状态
     * @param deviceId  设备的id
     * @param state 0:异常离线，1：正常运行，2：控制终止进入自主运行(温和的，适用于停机维护使用)
     */
    public void setDeviceControllers(String deviceId, Integer state){
        redisUtil.set(deviceId+"Controller", state);
    }


    //==================================DeviceMaxTimeMap===========================
    /**
     * 初始化设备最长绿灯时间
     */
    public void initDevicesMaxTimeMap(){
        try{
            Map<String, Object> devicesMap = new ConcurrentHashMap<>();
            redisUtil.hmset("devicesMaxTimeMap", devicesMap);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("DevicesMapper发生错误");
        }
    }
    /**
     * 获取设备最长绿灯时间的map
     */
    public Map<Object, Object> getDMTMap(){
        try{
            return redisUtil.hmget("devicesMaxTimeMap");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void removeDMTMap(String key){
        redisUtil.hdel("devicesMaxTimeMap", key);
    }

    public void putDMTMap(String key, Object value){
        redisUtil.hset("devicesMaxTimeMap", key, value);
    }

    public Integer getDMTMap(String key){
        return (Integer) redisUtil.hget("devicesMaxTimeMap", key);
    }

    public boolean containsKeyDMTMap(String key){
        return redisUtil.hHashKey("devicesMaxTimeMap", key);
    }


}
