package com.example.easyexcel.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 尝试在监听器上运用泛型
 *
 * @author hong-2000
 * @date :2020/10/27 8:57
 */
@Slf4j
public class ReadExcelListener<T> extends AnalysisEventListener<T> {

    List<T> list = new ArrayList<>();

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(T data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        list.add(data);
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        //清除数据
        log.info("所有数据解析完成！");
    }

    public List<T> getData() {
        return list;
    }
}