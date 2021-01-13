package com.example.easyexcel.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author hong-2000
 * @version 1.0
 * @description
 * @create 2021/1/12 15:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DemoData {
    private String name;
    private int age;
    private Date birthday;
}
