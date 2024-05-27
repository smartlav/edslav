package com.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExcelColumnWiseReader {
    public static void main(String[] args) {
        String excelFilePath = "C://Training/AEM EDS/eds_form_requirements.xlsx";

        try (FileInputStream fis = new FileInputStream(new File(excelFilePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Get the first sheet
            Sheet sheet = workbook.getSheetAt(0);

            // Determine the number of columns by finding the maximum cell index
            int maxColumns = 0;
            for (Row row : sheet) {
                if (row.getLastCellNum() > maxColumns) {
                    maxColumns = row.getLastCellNum();
                }
            }

            // Iterate through columns
            for (int col = 0; col < maxColumns; col++) {
                System.out.println("Column " + col + ":");
                // Iterate through rows within the current column
                for (Row row : sheet) {
                    Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell != null) {
                        switch (cell.getCellType()) {
                            case STRING:
                                System.out.println(cell.getStringCellValue());
                                break;
                            case NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    System.out.println(cell.getDateCellValue());
                                } else {
                                    System.out.println(cell.getNumericCellValue());
                                }
                                break;
                            case BOOLEAN:
                                System.out.println(cell.getBooleanCellValue());
                                break;
                            case FORMULA:
                                System.out.println(cell.getCellFormula());
                                break;
                            case BLANK:
                                System.out.println("");
                                break;
                            default:
                                System.out.println("");
                        }
                    } else {
                        System.out.println(""); // Print blank for missing cells
                    }
                }
                System.out.println(); // New line for next column
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

