//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.01.23 at 07:48:47 PM CET 
//


package org.lsc.configuration;

import java.math.BigInteger;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ldapSourceServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ldapSourceServiceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://lsc-project.org/XSD/lsc-core-2.0.xsd}ldapServiceType">
 *       &lt;sequence>
 *         &lt;element name="filterAsync" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateFormat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="interval" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ldapSourceServiceType", namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd", propOrder = {
    "filterAsync",
    "dateFormat",
    "interval"
})
@XmlSeeAlso({
    AsyncLdapSourceServiceType.class
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
public class LdapSourceServiceType
    extends LdapServiceType
{

    @XmlElement(namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected String filterAsync;
    @XmlElement(namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected String dateFormat;
    @XmlElement(namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd", defaultValue = "5")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected BigInteger interval;

    /**
     * Gets the value of the filterAsync property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public String getFilterAsync() {
        return filterAsync;
    }

    /**
     * Sets the value of the filterAsync property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setFilterAsync(String value) {
        this.filterAsync = value;
    }

    /**
     * Gets the value of the dateFormat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Sets the value of the dateFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setDateFormat(String value) {
        this.dateFormat = value;
    }

    /**
     * Gets the value of the interval property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public BigInteger getInterval() {
        return interval;
    }

    /**
     * Sets the value of the interval property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setInterval(BigInteger value) {
        this.interval = value;
    }

}
