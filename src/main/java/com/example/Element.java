package com.example;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;


public class Element {
    private String name;
    private Map<QName, String> attributes = new HashMap<>();

    public Element(String name) {
        this.name = name;
    }

   /* @XmlRootElement
    public static class ElementWrapper {
        private Element element;

        public ElementWrapper(Element element) {
            this.element = element;
        }

        @XmlElement(name = "Element")
        public Element getElement() {
            return element;
        }
        @XmlElement(name = "Element")
        public void setElement(Element element) {
            this.element = element;
        }
    }*/

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAnyAttribute
    public Map<QName, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<QName, String> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(String name, String value) {
        this.attributes.put(new QName(name), value);
    }

	
}
