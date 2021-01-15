package com.example.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.write.merge.LoopMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.example.easyexcel.config.CustomCellWriteHandler;
import com.example.easyexcel.config.CustomSheetWriteHandler;
import com.example.easyexcel.po.*;
import com.example.easyexcel.utils.SetData;
import com.example.easyexcel.utils.TestFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.InputStream;
import java.util.*;

import static com.example.easyexcel.utils.SetData.setDemoData;
import static com.example.easyexcel.utils.SetData.setExcelPoData;

/**
 * @author hong-2000
 * @version 1.0
 * @description 写测试
 * @create 2021/1/14 17:00
 */
@SpringBootTest
@Slf4j
public class WriteTests {

    String fileName = "src\\test\\java\\com\\example\\easyexcel\\xlsx\\demoData.xlsx";
    String fileNameTwo = "src\\test\\java\\com\\example\\easyexcel\\xlsx\\ExcelPO.xlsx";

    @Test
    void contextLoads() {
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
     * 根据参数只导出指定列
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 根据自己或者排除自己需要的列
     * <p>3. 直接写即可
     *
     * @since 2.1.1
     */
    @Test
    public void excludeOrIncludeWrite() {
        // 根据用户传入字段 假设我们要忽略 date
        Set<String> excludeColumnFiledNames = new HashSet<String>();
        excludeColumnFiledNames.add("name");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoData.class).excludeColumnFiledNames(excludeColumnFiledNames).sheet("test-1")
                .doWrite(SetData.setDemoData());

        // 根据用户传入字段 假设我们只要导出 date
        Set<String> includeColumnFiledNames = new HashSet<String>();
        includeColumnFiledNames.add("name");
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoData.class).includeColumnFiledNames(includeColumnFiledNames).sheet("test-2")
                .doWrite(SetData.setDemoData());
    }

    /**
     * 重复多次写入
     * <p>1. 创建excel对应的实体对象 参照{@link}
     * <p>2. 使用{@link}注解指定复杂的头
     * <p>3. 直接调用二次写入即可
     */
    @Test
    public void repeatedWrite() {
        // 方法1 如果写到同一个sheet
        ExcelWriter excelWriter = null;
        try {
            // 这里 需要指定写用哪个class去写
            excelWriter = EasyExcel.write(fileName, DemoData.class).build();
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
            for (int i = 0; i < 5; i++) {
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<DemoData> data = SetData.setDemoData();
                excelWriter.write(data, writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

        // 方法2 如果写到不同的sheet 同一个对象
        try {
            // 这里 指定文件
            excelWriter = EasyExcel.write(fileName, DemoData.class).build();
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
            for (int i = 0; i < 5; i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + i).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<DemoData> data = SetData.setDemoData();
                excelWriter.write(data, writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }

        // 方法3 如果写到不同的sheet 不同的对象
        try {
            // 这里 指定文件
            excelWriter = EasyExcel.write(fileName).build();
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来。这里最终会写到5个sheet里面
            for (int i = 0; i < 5; i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样。这里注意DemoData.class 可以每次都变，我这里为了方便 所以用的同一个class 实际上可以一直变
                WriteSheet writeSheet = EasyExcel.writerSheet(i, "模板" + i).head(DemoData.class).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<DemoData> data = SetData.setDemoData();
                excelWriter.write(data, writeSheet);
            }
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 图片导出
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link ImageData}
     * <p>
     * 2. 直接写即可
     */
    @Test
    public void imageWrite() throws Exception {
        // 如果使用流 记得关闭
        InputStream inputStream = null;
        try {
            List<ImageData> list = new ArrayList<>();
            String imagePath = "E:\\easyexcel-demo\\src\\test\\java\\com\\example\\easyexcel\\image\\test.jpg";
            list.add(ImageData.builder()
                    // 放入五种类型的图片 实际使用只要选一种即可
                    .file(new File(imagePath))
                    .inputStream(FileUtils.openInputStream(new File(imagePath)))
                    .string(imagePath)
                    .byteArray(FileUtils.readFileToByteArray(new File(imagePath)))
                    //.url(new URL("https://raw.githubusercontent.com/alibaba/easyexcel/master/src/test/resources/converter/img.jpg"))
                    .build());
            EasyExcel.write("src\\test\\java\\com\\example\\easyexcel\\xlsx\\imageData.xlsx", ImageData.class).sheet().doWrite(list);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 根据模板写入
     * <p>1. 创建excel对应的实体对象 参照{@link  }
     * <p>2. 使用{@link }注解指定写入的列
     * <p>3. 使用withTemplate 写取模板
     * <p>4. 直接写即可
     */
    @Test
    public void templateWrite() {
        String templateFileName = this.fileName;
        String fileName = "src\\test\\java\\com\\example\\easyexcel\\xlsx\\demoData2.xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoData.class).withTemplate(templateFileName).sheet().doWrite(SetData.setDemoData());
    }

    /**
     * 注解形式自定义样式
     * <p>1. 创建excel对应的实体对象 参照{@link DemoStyleData}
     * <p>3. 直接写即可
     *
     * @since 2.2.0-beta1
     */
    @Test
    public void annotationStyleWrite() {
        String fileName = TestFileUtil.getPath() + "annotationStyleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoStyleData.class).sheet("模板").doWrite(setDemoData());
    }

    /**
     * 合并单元格
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData} {@link DemoMergeData}
     * <p>
     * 2. 创建一个merge策略 并注册
     * <p>
     * 3. 直接写即可
     *
     * @since 2.2.0-beta1
     */
    @Test
    public void mergeWrite() {
        // 方法1 注解
        String fileName = TestFileUtil.getPath() + "mergeWrite" + System.currentTimeMillis() + ".xlsx";
        // 在DemoStyleData里面加上ContentLoopMerge注解
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoMergeData.class).sheet("模板").doWrite(setDemoData());

        // 方法2 自定义合并单元格策略
        fileName = TestFileUtil.getPath() + "mergeWrite" + System.currentTimeMillis() + ".xlsx";
        // 每隔2行会合并 把eachColumn 设置成 3 也就是我们数据的长度，所以就第一列会合并。当然其他合并策略也可以自己写
        LoopMergeStrategy loopMergeStrategy = new LoopMergeStrategy(2, 0);
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoData.class).registerWriteHandler(loopMergeStrategy).sheet("模板").doWrite(setDemoData());
    }


    /**
     * 使用table去写入
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 然后写入table即可
     */
    @Test
    public void tableWrite() {
        String fileName = TestFileUtil.getPath() + "tableWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里直接写多个table的案例了，如果只有一个 也可以直一行代码搞定，参照其他案例
        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(fileName, DemoData.class).build();
            // 把sheet设置为不需要头 不然会输出sheet的头 这样看起来第一个table 就有2个头了
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").needHead(Boolean.FALSE).build();
            // 这里必须指定需要头，table 会继承sheet的配置，sheet配置了不需要，table 默认也是不需要
            WriteTable writeTable0 = EasyExcel.writerTable(0).needHead(Boolean.TRUE).build();
            WriteTable writeTable1 = EasyExcel.writerTable(1).needHead(Boolean.TRUE).build();
            // 第一次写入会创建头
            excelWriter.write(setDemoData(), writeSheet, writeTable0);
            // 第二次写如也会创建头，然后在第一次的后面写入数据
            excelWriter.write(setDemoData(), writeSheet, writeTable1);
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 动态头，实时生成头写入
     * <p>
     * 思路是这样子的，先创建List<String>头格式的sheet仅仅写入头,然后通过table 不写入头的方式 去写入数据
     *
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 然后写入table即可
     */
    @Test
    public void dynamicHeadWrite() {
        String fileName = TestFileUtil.getPath() + "dynamicHeadWrite" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName)
                // 这里放入动态头
                .head(this.head()).sheet("模板")
                // 当然这里数据也可以用 List<List<String>> 去传入
                .doWrite(setDemoData());
    }

    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = new ArrayList<String>();
        head0.add("字符串" + System.currentTimeMillis());
        List<String> head1 = new ArrayList<String>();
        head1.add("数字" + System.currentTimeMillis());
        List<String> head2 = new ArrayList<String>();
        head2.add("日期" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    /**
     * 自动列宽(不太精确)
     * <p>
     * 这个目前不是很好用，比如有数字就会导致换行。而且长度也不是刚好和实际长度一致。 所以需要精确到刚好列宽的慎用。 当然也可以自己参照
     * {@link LongestMatchColumnWidthStyleStrategy}重新实现.
     * <p>
     * poi 自带{@link SXSSFSheet#autoSizeColumn(int)} 对中文支持也不太好。目前没找到很好的算法。 有的话可以推荐下。
     *
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link LongestMatchColumnWidthData}
     * <p>
     * 2. 注册策略{@link LongestMatchColumnWidthStyleStrategy}
     * <p>
     * 3. 直接写即可
     */
    @Test
    public void longestMatchColumnWidthWrite() {
        String fileName =
                TestFileUtil.getPath() + "longestMatchColumnWidthWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, LongestMatchColumnWidthData.class)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).sheet("模板").doWrite(dataLong());
    }

    private List<LongestMatchColumnWidthData> dataLong() {
        List<LongestMatchColumnWidthData> list = new ArrayList<LongestMatchColumnWidthData>();
        for (int i = 0; i < 10; i++) {
            LongestMatchColumnWidthData data = new LongestMatchColumnWidthData();
            data.setString("测试很长的字符串测试很长的字符串测试很长的字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(1000000000000.0);
            list.add(data);
        }
        return list;
    }

    /**
     * 下拉，超链接等自定义拦截器（上面几点都不符合但是要对单元格进行操作的参照这个）
     * <p>
     * demo这里实现2点。1. 对第一行第一列的头超链接到:https://github.com/alibaba/easyexcel 2. 对第一列第一行和第二行的数据新增下拉框，显示 测试1 测试2
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 注册拦截器 {@link CustomCellWriteHandler} {@link CustomSheetWriteHandler}
     * <p>
     * 2. 直接写即可
     */
    @Test
    public void customHandlerWrite() {
        String fileName = TestFileUtil.getPath() + "customHandlerWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, DemoData.class).registerWriteHandler(new CustomSheetWriteHandler())
                .registerWriteHandler(new CustomCellWriteHandler()).sheet("模板").doWrite(setDemoData());
    }
}
