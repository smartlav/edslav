package com.example;

import jakarta.xml.bind.annotation.XmlAnyAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement(name = "Element_old")
public class OldElement {
    private Map<QName, String> attributes = new HashMap<>();

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
