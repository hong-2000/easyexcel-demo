package com.example.easyexcel.utils;

import com.example.easyexcel.po.DemoData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hong-2000
 * @version 1.0
 * @description
 * @create 2021/1/13 14:16
 */
@Component
public class SetData {

    private SetData() {
    }

    public static List<DemoData> data() {
        int count = 10;
        List<DemoData> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(DemoData.builder().name("test").age(i).birthday(new Date()).build());
        }
        return list;
    }
}
