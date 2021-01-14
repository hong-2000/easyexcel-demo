package com.example.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.example.easyexcel.config.DemoDataListener;
import com.example.easyexcel.config.ReadExcelListener;
import com.example.easyexcel.po.ConverterData;
import com.example.easyexcel.po.DemoData;
import com.example.easyexcel.po.ExcelPO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import static com.example.easyexcel.utils.SetData.setDemoData;
import static com.example.easyexcel.utils.SetData.setExcelPoData;

@SpringBootTest
@Slf4j
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
        EasyExcel.write(fileNameTwo, ExcelPO.class).sheet("写入方法一").doWrite(setExcelPoData());
    }

    @Test
    void simpleWriteTwo() {

        ExcelWriter excelWriter = EasyExcel.write(fileName, DemoData.class).build();
        WriteSheet writeSheet = EasyExcel.writerSheet("写入方法二").build();
        excelWriter.write(setDemoData(), writeSheet);
        /// 关闭流
        excelWriter.finish();
    }

    /**
     * 运用泛型 做统一监听器
     * 只有读取文件的时候才会需要监听器
     *
     * @return: void
     * @Version 1.0
     * @author hong-2000
     * @date 2021/1/13 17:49
     */
    @Test
    void ReadExcel() {
        // 这里需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileNameTwo, ExcelPO.class, new ReadExcelListener()).sheet().doRead();
    }

    @Test
    void ReadFile() {
        // 这里需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        File file = new File(fileNameTwo);
        EasyExcel.read(file, ExcelPO.class, new ReadExcelListener()).sheet().doRead();
    }

    /**
     * 日期、数字或者自定义格式转换
     * <p>
     * 默认读的转换器{@link #()}
     * <p>1. 创建excel对应的实体对象 参照{@link ConverterData}.里面可以使用注解{@link DateTimeFormat}、{@link NumberFormat}或者自定义注解
     * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link ReadExcelListener}
     * <p>3. 直接读即可
     */
    @Test
    void converterRead() {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(fileName, ConverterData.class, new ReadExcelListener())
                // 这里注意 我们也可以registerConverter来指定自定义转换器， 但是这个转换变成全局了， 所有java为string,excel为string的都会用这个转换器。
                // 如果就想单个字段使用请使用@ExcelProperty 指定converter
                // .registerConverter(new CustomStringStringConverter())
                // 读取sheet
                .sheet().doRead();
    }

    /**
     * 读多个或者全部sheet,这里注意一个sheet不能读取多次，多次读取需要重新读取文件
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
     * <p>3. 直接读即可
     */
    @Test
    public void repeatedRead() {
        // 读取全部sheet
        // 这里需要注意 DemoDataListener的doAfterAllAnalysed 会在每个sheet读取完毕后调用一次。然后所有sheet都会往同一个DemoDataListener里面写
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).doReadAll();
        // 读取部分sheet
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(fileName).build();
            // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
            ReadSheet readSheet1 =
                    EasyExcel.readSheet(0).head(DemoData.class).registerReadListener(new DemoDataListener()).build();
            ReadSheet readSheet2 =
                    EasyExcel.readSheet(1).head(DemoData.class).registerReadListener(new DemoDataListener()).build();
            // 这里注意 一定要把sheet1 sheet2 一起传进去，不然有个问题就是03版的excel 会读取多次，浪费性能
            excelReader.read(readSheet1, readSheet2);
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }
    }

    /**
     * 多行头
     *
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
     * <p>3. 设置headRowNumber参数，然后读。 这里要注意headRowNumber如果不指定， 会根据你传入的class的{@link ExcelProperty#value()}里面的表头的数量来决定行数，
     * 如果不传入class则默认为1.当然你指定了headRowNumber不管是否传入class都是以你传入的为准。
     */
    @Test
    public void complexHeaderRead() {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet()
                // 这里可以设置1，因为头就是一行。如果多行头，可以设置其他值。不传入也可以，因为默认会根据DemoData 来解析，他没有指定头，也就是默认1行
                .headRowNumber(2).doRead();
    }

    /**
     * 同步的返回，不推荐使用，如果数据量大会把数据放到内存里面
     */
    @Test
    public void synchronousRead() {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<DemoData> list = EasyExcel.read(fileName).head(DemoData.class).sheet().doReadSync();
        for (DemoData data : list) {
            log.info("读取到数据:{}", JSON.toJSONString(data));
        }
        // 这里 也可以不指定class，返回一个list，然后读取第一个sheet 同步读取会自动finish
        List<Map<Integer, String>> listMap = EasyExcel.read(fileName).sheet().doReadSync();
        for (Map<Integer, String> data : listMap) {
            // 返回每条数据的键值对 表示所在的列 和所在列的值
            log.info("读取到数据:{}", JSON.toJSONString(data));
        }
    }

    /**
     * 额外信息（批注、超链接、合并单元格信息读取）
     * <p>由于是流式读取，没法在读取到单元格数据的时候直接读取到额外信息，所以只能最后通知哪些单元格有哪些额外信息
     *
     * <p>1. 创建excel对应的实体对象 参照{@link }
     * <p>2. 由于默认异步读取excel，所以需要创建excel一行一行的回调监听器，参照{@link }
     * <p>3. 直接读即可
     *
     * @since 2.2.0-beat1
     */
    @Test
    public void extraRead() {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener())
                // 需要读取批注 默认不读取
                .extraRead(CellExtraTypeEnum.COMMENT)
                // 需要读取超链接 默认不读取
                .extraRead(CellExtraTypeEnum.HYPERLINK)
                // 需要读取合并单元格信息 默认不读取
                .extraRead(CellExtraTypeEnum.MERGE).sheet().doRead();
    }

    /**
     * 测网速
     *
     * @return: void
     * @Version 1.0
     * @author hong-2000
     * @date 2021/1/14 16:08
     */
    @Test
    void PingTest() {
        String[] addrs = {"www.baidu.com"};
        if (addrs.length < 1) {
            log.error("{}", "syntax Error!");
        } else {
            for (int i = 0; i < addrs.length; i++) {
                String line = "";
                try {
                    // 指令
                    Process pro = Runtime.getRuntime().exec("ping " + addrs[i] + " -l 1000 -n 4");
                    // 执行，解决中文乱码
                    BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream(), "GB2312"));
                    while ((line = buf.readLine()) != null) {
                        log.info("{}", line);
                        if (line.contains("平均")) {
                            log.info("{}", line);
                            String value = line.substring(line.lastIndexOf(" ") + 1, line.lastIndexOf("ms"));
                            log.info("{}{}{}", "网速：", (1000 / Integer.parseInt(value)), "KB");
                        }
                    }
                } catch (Exception ex) {
                    log.error("{}", ex.getMessage());
                }
            }
        }
    }


}
