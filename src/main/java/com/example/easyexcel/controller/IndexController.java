package com.example.easyexcel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.example.easyexcel.config.ReadExcelListener;
import com.example.easyexcel.po.DemoData;
import com.example.easyexcel.po.ExcelPO;
import com.example.easyexcel.utils.ExcelUtils;
import com.example.easyexcel.utils.SetData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hong-2000
 * @version 1.0
 * @description
 * @create 2021/1/13 17:36
 */
@Slf4j
@RestController
@RequestMapping("easyexcel")
public class IndexController {
    /**
     * 文件下载（失败了会返回一个有部分数据的Excel）
     * <p>1. 创建excel对应的实体对象 参照{@link}
     * <p>2. 设置返回的 参数
     * <p>3. 直接写，这里注意，finish的时候会自动关闭OutputStream,当然你外面再关闭流问题不大
     * 历史代码：
     * // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
     * response.setContentType("application/vnd.ms-excel");
     * response.setCharacterEncoding("utf-8");
     * // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
     * String fileName = URLEncoder.encode("测试", "UTF-8");
     * response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
     * EasyExcel.write(response.getOutputStream(), DownloadData.class).sheet("模板").doWrite(data());
     */
    @GetMapping("download")
    public void download(HttpServletResponse response) {
        ExcelUtils.writeExcel(response, "demoData", DemoData.class, SetData.setDemoData());
    }

    /**
     * 文件上传
     * <p>1. 创建excel对应的实体对象 参照{@link }
     * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link }
     * <p>3. 直接读即可
     * 历史代码
     * EasyExcel.read(file.getInputStream(), UploadData.class, new UploadDataListener(uploadDAO)).sheet().doRead();
     */
    @PostMapping("upload")
    @ResponseBody
    public String upload(MultipartFile file) {
        ExcelUtils.readExcel(file, ExcelPO.class, new ReadExcelListener<>());
        return "success";
    }

    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @since 2.1.1
     */
    @GetMapping("downloadFailedUsingJson")
    public void downloadFailedUsingJson(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), DemoData.class).autoCloseStream(Boolean.FALSE).sheet("模板")
                    .doWrite(SetData.setDemoData());
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>(16);
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }
}
