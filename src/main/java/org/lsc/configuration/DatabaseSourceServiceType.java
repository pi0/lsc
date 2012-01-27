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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for databaseSourceServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="databaseSourceServiceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://lsc-project.org/XSD/lsc-core-2.0.xsd}serviceType">
 *       &lt;sequence>
 *         &lt;element name="interval" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="requestNameForList" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestNameForObject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestNameForNextId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestNameForClean" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "databaseSourceServiceType", namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd", propOrder = {
    "interval",
    "requestNameForList",
    "requestNameForObject",
    "requestNameForNextId",
    "requestNameForClean"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
public class DatabaseSourceServiceType
    extends ServiceType
{

    @XmlElement(namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd", defaultValue = "5")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected BigInteger interval;
    @XmlElement(namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected String requestNameForList;
    @XmlElement(namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected String requestNameForObject;
    @XmlElement(namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected String requestNameForNextId;
    @XmlElement(namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    protected String requestNameForClean;

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

    /**
     * Gets the value of the requestNameForList property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public String getRequestNameForList() {
        return requestNameForList;
    }

    /**
     * Sets the value of the requestNameForList property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setRequestNameForList(String value) {
        this.requestNameForList = value;
    }

    /**
     * Gets the value of the requestNameForObject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public String getRequestNameForObject() {
        return requestNameForObject;
    }

    /**
     * Sets the value of the requestNameForObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setRequestNameForObject(String value) {
        this.requestNameForObject = value;
    }

    /**
     * Gets the value of the requestNameForNextId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public String getRequestNameForNextId() {
        return requestNameForNextId;
    }

    /**
     * Sets the value of the requestNameForNextId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setRequestNameForNextId(String value) {
        this.requestNameForNextId = value;
    }

    /**
     * Gets the value of the requestNameForClean property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public String getRequestNameForClean() {
        return requestNameForClean;
    }

    /**
     * Sets the value of the requestNameForClean property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2012-01-23T07:48:47+01:00", comments = "JAXB RI vJAXB 2.1.10 in JDK 6")
    public void setRequestNameForClean(String value) {
        this.requestNameForClean = value;
    }

}
