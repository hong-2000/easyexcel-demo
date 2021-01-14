package com.example.easyexcel.po;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hong-2000
 * @description 指定列的下标或者列名
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExcelPO {
    /**
     * R3编码
     */
    @ExcelProperty(value = "R3编码", index = 0)
    private String r3Code;

    /**
     * R3名称
     */
    @ExcelProperty(value = "R3名称", index = 1)
    private String r3Name;


    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名", index = 2)
    private String userName;

    @ExcelProperty(value = "只为加一列", index = 3)
    private String column;
}
