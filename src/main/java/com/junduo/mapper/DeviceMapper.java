package com.junduo.mapper;

import com.junduo.pojo.Device;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ClassName DeviceMapper
 * Description
 * Create by JBuck
 * Date 2022/3/17 18:14
 * @author JBuck
 */
@Mapper
@Repository
public interface DeviceMapper {

    List<Device> queryDeviceList();
}
