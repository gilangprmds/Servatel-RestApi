package com.juaracoding.tugasakhir.util;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 11/02/2025 14:35
@Last Modified 11/02/2025 14:35
Version 1.0
*/
import com.juaracoding.tugasakhir.config.OtherConfig;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ExcelWriter {

    private XSSFWorkbook workbook ;
    private XSSFRow row;
    private XSSFSheet spreadsheet ;
    private Map<Integer, String[]> mapData  ;
    private String[] exceptionString = new String[2];
    private String[] strDatas = null;
    private ServletOutputStream outputStream ;

    /*SELECT LIST FROM RESULT SQL STATEMENT SELECT FROM DATABASE*/
    public ExcelWriter(String[][] datas , String[] header, String sheetName, HttpServletResponse response )
    {
        workbook = new XSSFWorkbook();
        spreadsheet = workbook.createSheet(sheetName);
        mapData  = new TreeMap<Integer, String[]>();
        exceptionString[0] = "ExcelWriter";
        writeToExcel(datas, header,response);
    }

    public void writeToExcel(String[][] datas, String[] header, HttpServletResponse response )
    {
        mapData.clear();
        CellStyle headerStyle ;
        try {
            int k=1;/*initiate for rows data in excel*/

            mapData.put(1,header);

            for(int i=0;i<datas.length;i++)
            {
                k++;/*start from 2*/
                strDatas = new String[header.length];
                for(int j=0;j<header.length;j++)
                {
                    strDatas[j] = datas[i][j];
                }
                mapData.put(k,strDatas);
            }
            headerStyle = workbook.createCellStyle();
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setWrapText(true);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            Set<Integer> keyid = mapData.keySet();
            int rowid = 0;
            for (int key : keyid) {

                row = spreadsheet.createRow(rowid++);
                Object[] objectArr = mapData.get(key);
                int cellid = 0;

                for (Object obj : objectArr) {
                    Cell cell = row.createCell(cellid++);
                    cell.setCellValue((String)obj);
                    cell.setCellStyle(cellStyle);
                }
            }

        } catch (Exception e) {
            LoggingFile.logException("Excel Writer","writeToExcel(String[][] datas, String[] header, HttpServletResponse response ) --> Line 87", e, OtherConfig.getEnableLogFile());
        }
        finally {
            try {
                outputStream = response.getOutputStream();
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
            } catch (IOException e) {
                LoggingFile.logException("Excel Writer","writeToExcel(String[][] datas, String[] header, HttpServletResponse response ) --> Line 96", e, OtherConfig.getEnableLogFile());
//                LoggingFile.exceptionStringz("Excel Writer","writeToExcel", e, OtherConfig.getFlagLogging());
            }
        }
    }
}