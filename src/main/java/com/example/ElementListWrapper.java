package com.example;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Elements")
public class ElementListWrapper {
    private List<Element> elements;

    public ElementListWrapper() {
    }

    public ElementListWrapper(List<Element> elements) {
        this.elements = elements;
    }

    @XmlElement(name = "Element")
    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}

