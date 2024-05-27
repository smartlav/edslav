package com.example;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Element")
public class ElementWrapper {
    private Element element;

    public ElementWrapper() {
    }

    public ElementWrapper(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }
}
