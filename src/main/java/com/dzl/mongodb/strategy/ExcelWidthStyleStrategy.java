package com.dzl.mongodb.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public class ExcelWidthStyleStrategy extends AbstractColumnWidthStyleStrategy {

    // 统计setColumnWidth被调用多少次
    private static int count = 0;

    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {

        // 简单设置
        Sheet sheet = writeSheetHolder.getSheet();
        sheet.setColumnWidth(cell.getColumnIndex(), 5000);
        count++;
        System.out.println(count);
        //super.setColumnWidth(writeSheetHolder, cellDataList, cell, head, relativeRowIndex, isHead);
    }
}
