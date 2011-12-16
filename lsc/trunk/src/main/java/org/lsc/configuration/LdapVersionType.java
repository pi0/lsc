//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.16 at 01:28:04 AM CET 
//


package org.lsc.configuration;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ldapVersionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ldapVersionType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="VERSION2"/>
 *     &lt;enumeration value="VERSION_3"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ldapVersionType", namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd")
@XmlEnum
public enum LdapVersionType {

    @XmlEnumValue("VERSION2")
    VERSION_2("VERSION2"),
    VERSION_3("VERSION_3");
    private final String value;

    LdapVersionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LdapVersionType fromValue(String v) {
        for (LdapVersionType c: LdapVersionType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
