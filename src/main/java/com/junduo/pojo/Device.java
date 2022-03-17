package com.junduo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName Device
 * Description
 * Create by JBuck
 * Date 2022/3/17 18:07
 * @author JBuck
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    private int id;
    private String deviceName;
    private String coord;
    private int trafficLightId1;
    private int trafficLightId2;
    private int trafficLightId3;
    private int trafficLightId4;
}
