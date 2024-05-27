package com.example;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;

public class combinedExcelToXmlConverter {
    public static void main(String[] args) {
        String excelFilePath = "C://Training/AEM EDS/eds_form_requirements_1.xlsx";
        String xmlFilePath = "C://Training/AEM EDS/elements_1.xml";
        String xsltFilePath = "C://Training/AEM EDS/transform_1.xslt";
        String transformedXmlFilePath = "C://Training/AEM EDS/result_1.xml";

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

            // Create a list to hold all elements
            List<Element> elements = new ArrayList<>();

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

                // Add the element to the list
                elements.add(element);
            }

            // Create JAXB context and marshaller
            JAXBContext context = JAXBContext.newInstance(ElementListWrapper.class, Element.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Create the wrapper and set the elements
            ElementListWrapper wrapper = new ElementListWrapper(elements);

            // Marshal the wrapper to XML
           // marshaller.marshal(wrapper, new File("elements.xml"));
            
            // Marshal the wrapper to XML
            StringWriter xmlWriter = new StringWriter();
            marshaller.marshal(wrapper, xmlWriter);
            
         // Write the XML to file
            try (FileWriter fileWriter = new FileWriter(xmlFilePath)) {
                fileWriter.write(xmlWriter.toString());
            }

            // Apply XSLT transformation            
            XSLTTransformation.applyXsltTransformation(xmlFilePath, xsltFilePath, transformedXmlFilePath);

        } catch (IOException | JAXBException | TransformerException e) {
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

