package com.dzl.mongodb.strategy;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 自定义单元格合并策略
 */
public class CustomMergeStrategy implements RowWriteHandler {

    Integer start;

    Integer end;

    public CustomMergeStrategy(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }


    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                Row row, Integer relativeRowIndex, Boolean isHead) {
        // 如果是标题,则直接返回
        if (isHead) {
            return;
        }

        // 获取当前sheet
        Sheet sheet = writeSheetHolder.getSheet();


        // 判断是否需要和上一行进行合并
        // 不能和标题合并，只能数据行之间合并
        if (row.getRowNum() <= 1) {
            return;
        }
        // 获取上一行数据
        Row lastRow = sheet.getRow(row.getRowNum() - 1);

        for (int i = start; i < end; i++) {
            Cell cell = row.getCell(i);

            if(!Objects.isNull(cell)) {
                String cellValue = cell.getStringCellValue();
                // 将本行和上一行是同一类型的数据(通过主键字段进行判断)，则需要合并
                if (lastRow.getCell(i).getStringCellValue().equalsIgnoreCase(cellValue)) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(row.getRowNum() - 1, row.getRowNum(),
                            i, i);
                    sheet.addMergedRegionUnsafe(cellRangeAddress);
                }
            }
        }

    }
}
