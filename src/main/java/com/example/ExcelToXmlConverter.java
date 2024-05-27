package com.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@XmlRootElement(name = "guideContainer")
class GuideContainer {
    @XmlElement(name = "total")
    int total;

    @XmlElement(name = "offset")
    int offset;

    @XmlElement(name = "limit")
    int limit;
    
    @XmlElement(name = "rootPanel")
    Object rootPanel;

    @XmlElement(name = "data")
    Object data ;
    
    @XmlElement(name = "items")
    List<Entry> items = new ArrayList<>();
    
    private List<OldElement> elements = new ArrayList<>();

    @XmlElement(name = "Element")
    public List<OldElement> getElements() {
        return elements;
    }

    public void setElements(List<OldElement> elements) {
        this.elements = elements;
    }

    public void addElement(OldElement element) {
        this.elements.add(element);
    }
}

class Entry {
    @XmlElement(name = "Source")
    String source;

    @XmlElement(name = "Destination")
    String destination;
}

public class ExcelToXmlConverter {
	
	private static final Logger logger = LogManager.getLogger(ExcelToXmlConverter.class);
	
    public static void main(String[] args) throws Exception {
    	try {
            logger.debug("Starting the Excel to XML conversion process.");
            
            // Load Excel file
            logger.info("Loading Excel file...");
        Workbook workbook = new XSSFWorkbook(new File("C://Training/AEM EDS/eds_form_requirements.xlsx"));
        Sheet sheet = workbook.getSheetAt(0);

        // Create JAXB context and marshaller
        JAXBContext context = JAXBContext.newInstance(GuideContainer.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Create root object
        GuideContainer guideContainer = new GuideContainer();
       
        guideContainer.total = sheet.getLastRowNum();// + 1;
        guideContainer.offset = 0;
        guideContainer.limit = guideContainer.total;

        // Populate data
        logger.debug("Populating data from Excel.");
        Iterator<Row> iterator = sheet.iterator();
        while (iterator.hasNext()) {
            Row row = iterator.next();
            Entry entry = new Entry();
            entry.source = row.getCell(0).getStringCellValue();
            entry.destination = row.getCell(1).getStringCellValue();
            guideContainer.items.add(entry);
        }
  
        // Marshal root object to XML
        logger.info("Marshalling data to XML...");
        marshaller.marshal(guideContainer, new File("C://Training/AEM EDS/data.xml"));
        logger.info("Transformation complete. Output saved to data.xml.");

        // Close workbook
        workbook.close();
    }catch (Exception e) {
        logger.error("An error occurred while converting Excel to XML", e);
    }
 }
}