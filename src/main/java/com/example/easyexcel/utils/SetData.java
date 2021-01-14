package com.example.easyexcel.utils;

import com.example.easyexcel.po.DemoData;
import com.example.easyexcel.po.ExcelPO;
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

    public static List<DemoData> setDemoData() {
        int count = 11;
        List<DemoData> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(DemoData.builder().name("test").age(i).birthday(new Date()).build());
        }
        return list;
    }

    public static List<ExcelPO> setExcelPoData() {
        int count = 10;
        List<ExcelPO> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(ExcelPO.builder().userName("test").r3Name(String.valueOf(i)).r3Code(String.valueOf(i + 2)).column(String.valueOf(i + 5)).build());
        }
        return list;
    }
}
