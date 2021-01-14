package com.example.easyexcel.po;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.example.easyexcel.utils.CustomStringStringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hong-2000
 * @version 1.0
 * @description 自定义数据实体类
 * @create 2021/1/14 13:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConverterData {
    /**
     * 我自定义 转换器，不管数据库传过来什么 。我给他加上“自定义：”
     */
    @ExcelProperty(value = "name", converter = CustomStringStringConverter.class, index = 0)
    private String string;
    /**
     * 这里用string 去接日期才能格式化。我想接收年月日格式
     */
    @ExcelProperty(value = "birthday", index = 2)
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    private String date;
    /**
     * 我想接收百分比的数字
     */
    @ExcelProperty(value = "age", index = 1)
    @NumberFormat("#.##%")
    private String doubleData;
}
