package org.sweet.revelation.revelation.job.admin;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ElementBuilder {

    public static ElementBuilder build(Element element) {
        return new ElementBuilder(element);
    }

    private final Element element;

    public ElementBuilder(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("Element is mandatory");
        }

        this.element = element;
    }

    public ElementBuilder textContent(String s) {
        element.setTextContent(s);

        return this;
    }

    public ElementBuilder attribute(String name, String value) {
        if (value != null) {
            element.setAttribute(name, value);
        }

        return this;
    }

    public ElementBuilder attribute(String name, final boolean value) {
        element.setAttribute(name, Boolean.toString(value));

        return this;
    }

    public ElementBuilder appendChild(Node child) {
        element.appendChild(child);

        return this;
    }

    public Element build() {
        return element;
    }

    public Element appendTo(ElementBuilder parentBuilder) {
        if (element.hasAttributes() || element.getTextContent() != null || element.hasChildNodes()) {
            parentBuilder.element.appendChild(element);
        }

        return element;
    }

    public Element appendTo(Node parent) {
        if (element.hasAttributes() || element.getTextContent() != null || element.hasChildNodes()) {
            parent.appendChild(element);
        }

        return element;
    }
}
