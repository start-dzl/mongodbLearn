package com.dzl.mongodb.util;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import org.apache.poi.ss.usermodel.*;

public class EasyExcelUtil {

    public static WriteCellStyle getHeadStyle() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("黑体");//设置字体名字
        headWriteFont.setFontHeightInPoints((short) 11);//设置字体大小
        headWriteFont.setBold(true);//字体加粗
        headWriteCellStyle.setWriteFont(headWriteFont); //在样式用应用设置的字体;
        // 样式
        headWriteCellStyle.setBorderBottom(BorderStyle.THIN);//设置底边框;
        headWriteCellStyle.setBottomBorderColor((short) 0);//设置底边框颜色;
        headWriteCellStyle.setBorderLeft(BorderStyle.THIN);  //设置左边框;
        headWriteCellStyle.setLeftBorderColor((short) 0);//设置左边框颜色;
        headWriteCellStyle.setBorderRight(BorderStyle.THIN);//设置右边框;
        headWriteCellStyle.setRightBorderColor((short) 0);//设置右边框颜色;
        headWriteCellStyle.setBorderTop(BorderStyle.THIN);//设置顶边框;
        headWriteCellStyle.setTopBorderColor((short) 0); //设置顶边框颜色;
        headWriteCellStyle.setWrapped(false);  //设置自动换行;
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);//设置水平对齐的样式为居中对齐;
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);  //设置垂直对齐的样式为居中对齐;
        //headWriteCellStyle.setShrinkToFit(true);//设置文本收缩至合适

        return headWriteCellStyle;
    }

}
