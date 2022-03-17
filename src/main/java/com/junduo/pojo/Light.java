package com.junduo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName Light
 * Description
 * Create by JBuck
 * Date 2022/3/17 18:10
 * @author JBuck
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Light {
    private int id;
    private String coord;
    private int direction;
}
