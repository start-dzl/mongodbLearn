package com.dzl.mongodb.util;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.csv.CsvCell;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.AbstractCellStyleStrategy;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;
import java.util.List;

public class ExcelCellStyleStrategy extends AbstractCellStyleStrategy {

    @Override
    protected void setHeadCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
        //super.setHeadCellStyle(cell, head, relativeRowIndex);
    }

    @Override
    protected void setContentCellStyle(Cell cell, Head head, Integer relativeRowIndex) {
        if(head.getColumnIndex() == 0) {
            String stringCellValue = cell.getStringCellValue();
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(Integer.parseInt(stringCellValue));
        }

        if(head.getHeadNameList().contains("税收")){
            String stringCellValue = cell.getStringCellValue();
            cell.setCellType(CellType.NUMERIC);
            BigDecimal bigDecimal = new BigDecimal(stringCellValue);
            BigDecimal decimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
            String plainString = decimal.toPlainString();
            cell.setCellValue(Double.parseDouble(plainString));
        }
    }
}

