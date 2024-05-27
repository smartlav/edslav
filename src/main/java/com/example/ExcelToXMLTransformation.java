package com.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ExcelToXMLTransformation {
    private static final Logger logger = LogManager.getLogger(ExcelToXMLTransformation.class);

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

            // Create JAXB context and marshaller
            JAXBContext context = JAXBContext.newInstance(GuideContainer.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Create root object
            GuideContainer root = new GuideContainer();

            // Iterate through rows
            for (Row row : sheet) {
                OldElement element = new OldElement();

                // Iterate through columns within the current row
                for (int col = 0; col < maxColumns; col++) {
                    Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell != null) {
                        String cellValue = getCellValueAsString(cell);
                        element.addAttribute("Attribute" + (col + 1), cellValue);
                    } else {
                        element.addAttribute("Attribute" + (col + 1), "");
                    }
                }

                root.addElement(element);
            }

            // Marshal root object to XML
            marshaller.marshal(root, new File("C://Training/AEM EDS/data.xml"));

        } catch (IOException | JAXBException e) {
            logger.error("An error occurred while converting Excel to XML", e);
        }
    }

    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return Double.toString(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
