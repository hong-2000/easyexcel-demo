package com.example.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.example.easyexcel.config.DemoDataListener;
import com.example.easyexcel.config.ReadExcelListener;
import com.example.easyexcel.po.DemoData;
import com.example.easyexcel.po.ExcelPO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.easyexcel.utils.SetData.data;
import static com.example.easyexcel.utils.SetData.dataTwo;

@SpringBootTest
class EasyExcelApplicationTests {

    String fileName = "src\\test\\java\\com\\example\\easyexcel\\xlsx\\demoData.xlsx";
    String fileNameTwo = "src\\test\\java\\com\\example\\easyexcel\\xlsx\\ExcelPO.xlsx";

    @Test
    void contextLoads() {
    }

    /**
     * 最简单的读
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
     * <p>3. 直接读即可
     */
    @Test
    void simpleReadOne() {
        // 这里需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet().doRead();
    }

    @Test
    void simpleReadTwo() {
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } finally {
            if (excelReader != null) {
                // 关闭
                excelReader.finish();
            }
        }
    }

    @Test
    void simpleWriteOne() {
        //EasyExcel.write(fileName, DemoData.class).sheet("写入方法一").doWrite(data());
        EasyExcel.write(fileNameTwo, ExcelPO.class).sheet("写入方法一").doWrite(dataTwo());
    }

    @Test
    void simpleWriteTwo() {

        ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("写入方法二").build();
        excelWriter.write(data(), writeSheet);
        /// 关闭流
        excelWriter.finish();
    }

    @Test
    void ReadExcel() {
        // 这里需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileNameTwo, ExcelPO.class, new ReadExcelListener()).sheet().doRead();
    }

}
