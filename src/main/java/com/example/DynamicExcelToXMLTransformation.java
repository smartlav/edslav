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


public class DynamicExcelToXMLTransformation {
	
	private static final Logger logger = LogManager.getLogger(DynamicExcelToXMLTransformation.class);
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
            JAXBContext context = JAXBContext.newInstance(Element.class, ElementWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Iterate through rows
            for (Row row : sheet) {
                // Get the name for the element
                String elementName = getCellValueAsString(row.getCell(0));

                // Create element with the dynamic name
                Element element = new Element(elementName);

                // Iterate through columns within the current row
                for (int col = 1; col < maxColumns; col++) {
                    Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    if (cell != null) {
                        String cellValue = getCellValueAsString(cell);
                        element.addAttribute("Attribute" + col, cellValue);
                    } else {
                        element.addAttribute("Attribute" + col, "");
                    }
                }

                // Marshal element to XML
                marshaller.marshal(new ElementWrapper(element), new File(elementName + ".xml"));
                logger.info(elementName + ".xml");
            }

        } catch (IOException | JAXBException e) {
            e.printStackTrace();
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
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


