package com.example.easyexcel.utils;

import com.alibaba.excel.EasyExcel;
import com.example.easyexcel.config.ReadExcelListener;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {
    /**
     * 读Excel操作
     *
     * @param file
     * @param cls
     * @param listener
     * @param <T>
     * @return
     */
    public static <T> List<T> readExcel(MultipartFile file, Class<T> cls, ReadExcelListener<T> listener) {
        List<T> data = new ArrayList<>();
        try {
            EasyExcel.read(file.getInputStream(), cls, listener).sheet().doRead();
            //根据实际业务需求来选择，是否有返回值
            data = listener.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 写Excel操作
     *
     * @param response
     * @param fileName
     * @param cls
     * @param dataList
     * @param <T>
     */
    public static <T> void writeExcel(HttpServletResponse response, String fileName, Class<T> cls, List<T> dataList) {
        try {
            EasyExcel.write(getOutputStream(fileName, response), cls).sheet("sheet1").doWrite(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 导出文件时为Writer生成OutputStream
     *
     * @param fileName
     * @param response
     * @return
     * @throws Exception
     */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response)
            throws Exception {
        try {
            fileName = URLEncoder.encode(fileName, "utf-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            //此处指定了文件类型为xls，如果是xlsx的，请自行替换修改
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
            response.setHeader("Pragma", "public");
            response.setHeader("Cache-Control", "no-store");
            response.addHeader("Cache-Control", "max-age=0");
            return response.getOutputStream();
        } catch (IOException e) {
            throw new Exception("导出文件失败！");
        }
    }
}
