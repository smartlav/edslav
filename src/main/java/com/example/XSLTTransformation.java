package com.example;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import java.io.*;


public class XSLTTransformation {
	
	public static void applyXsltTransformation(String xmlFilePath, String xsltFilePath, String outputFilePath) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File(xsltFilePath));
        Transformer transformer = factory.newTransformer(xslt);

        Source xml = new StreamSource(new File(xmlFilePath));
        Result result = new StreamResult(new File(outputFilePath));

        transformer.transform(xml, result);
    }

}
