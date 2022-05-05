package com.dzl.mongodb.util;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.List;

public class RowWriteHandler implements CellWriteHandler {
    private HashMap<Integer, List<Integer>> map;

    public RowWriteHandler(HashMap<Integer, List<Integer>> map, Short colorIndex) {
        this.map = map;
    }

    public RowWriteHandler() {
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

        //当前行的第i列
        // int i = cell.getColumnIndex();
        //不处理第一行
        if (0 != cell.getRowIndex()) {
            if (head.getColumnIndex() == 0) {
                Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
                CellStyle cellStyle = workbook.createCellStyle();
                DataFormat dataFormat = workbook.createDataFormat();
                cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
                cell.setCellStyle(cellStyle);
                String cellValue = cell.getStringCellValue();
                cell.setCellType(CellType.NUMERIC);
                if(StringUtils.isNotBlank(cellValue)) {
                    cell.setCellValue(Integer.parseInt(cellValue));
                }

            }
        }
    }

    //加@Override会报错
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {


    }

}