package com.example.easyexcel.dao;

import com.example.easyexcel.po.DemoData;

import java.util.List;

/**
 * @author hong-2000
 * @version 1.0
 * @description
 * @create 2021/1/12 15:36
 */
public class DemoDAO {
    public void save(List<DemoData> list) {
        // 如果是mybatis,尽量别直接调用多次insert,自己写一个mapper里面新增一个方法batchInsert,所有数据一次性插入
    }
}
