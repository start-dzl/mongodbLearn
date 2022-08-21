package com.dzl.mongodb;

import com.alibaba.excel.EasyExcel;
import com.dzl.mongodb.strategy.CustomMergeStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class EasyExcelTest {

    /**
     * 动态头，实时生成头写入
     * <p>
     * 思路是这样子的，先创建List<String>头格式的sheet仅仅写入头,然后通过table 不写入头的方式 去写入数据
     *
     * <p>
     * 2. 然后写入table即可
     */
    @Test
    public void dynamicHeadWrite() {
        String fileName =  "/Users/dengzuliang/Desktop/" + System.currentTimeMillis() + ".xlsx";
        EasyExcel.write(fileName)
                .registerWriteHandler(new CustomMergeStrategy(0, 3))
                // 这里放入动态头
                .head(head()).sheet("模板")
                // 当然这里数据也可以用 List<List<String>> 去传入
                .doWrite(data());
    }

    private List<List<String>> head() {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = new ArrayList<String>();
        head0.add("字符串");
        List<String> head1 = new ArrayList<String>();
        head1.add("数字");
        List<String> head2 = new ArrayList<String>();
        head2.add("日期");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    private List<List<String>> data() {
        List<List<String>> list = new ArrayList<List<String>>();
        List<String> head0 = Arrays.asList("字符串", "数字", "日期","作者");

        List<String> head1 = Arrays.asList("字符串1", "数字", "数字","作者");

        List<String> head2 = Arrays.asList("字符串", "数字", "数字","作者");

        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }
}
