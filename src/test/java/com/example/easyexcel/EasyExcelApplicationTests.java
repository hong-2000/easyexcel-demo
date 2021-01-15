package com.example.easyexcel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

@SpringBootTest
@Slf4j
class EasyExcelApplicationTests {

    String[] addr = {"www.baidu.com", "www.hatech.com.cn"};

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
        if (addr.length < 1) {
            log.error("{}", "syntax Error!");
        } else {
            Arrays.stream(addr).forEach(addr -> {
                String line;
                try {
                    // 指令
                    Process pro = Runtime.getRuntime().exec("ping " + addr + " -l 1000 -n 4");
                    // 执行，解决中文乱码
                    BufferedReader buf = new BufferedReader(new InputStreamReader(pro.getInputStream(), "GB2312"));
                    while ((line = buf.readLine()) != null) {
                        log.info("{}", line);
                        if (line.contains("平均")) {
                            String value = line.substring(line.lastIndexOf(" ") + 1, line.lastIndexOf("ms"));
                            log.info("{}{}{}{}{}{}{}", "平均时间：", value, "ms", "\t", "网速：", (1000 / Integer.parseInt(value)), "KB/ms");
                        }
                    }
                } catch (Exception ex) {
                    log.error("{}", ex.getMessage());
                }
            });
        }
    }


}
