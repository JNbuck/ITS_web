package com.junduo.mapper;

import com.junduo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ClassName UserMapper
 * Description
 * Create by JBuck
 * Date 2022/1/24 19:49
 * @author JBuck
 */

/**
 * 这个注解表示了这是一个MyBatis的mapper类
 */
@Mapper
@Repository
public interface UserMapper {

    List<User> queryUserList();

    User queryUserById(int id);

    int addUser(User user);

    int updateUser(User user);

    int deleteUser(int id);
}
