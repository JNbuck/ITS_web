package com.junduo.controller;

import com.junduo.mapper.DeviceMapper;
import com.junduo.pojo.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

/**
 * ClassName DeviceController
 * Description
 * Create by JBuck
 * Date 2022/3/17 18:18
 * @author JBuck
 */
@Controller
public class DeviceController {
    @Autowired
    private DeviceMapper deviceMapper;

    static Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @RequestMapping("/devices")
    public String list(Model model){
        Collection<Device> deviceList = deviceMapper.queryDeviceList();
        model.addAttribute("devices", deviceList);
        return "/device/list";
    }
}
