//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.12.16 at 12:06:23 AM CET 
//


package org.lsc.configuration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for databaseDestinationServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="databaseDestinationServiceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://lsc-project.org/XSD/lsc-core-2.0.xsd}serviceType">
 *       &lt;sequence>
 *         &lt;element name="requestNameForList" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requestNameForObject" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="requestsNameForInsert" type="{http://lsc-project.org/XSD/lsc-core-2.0.xsd}valuesType"/>
 *         &lt;element name="requestsNameForUpdate" type="{http://lsc-project.org/XSD/lsc-core-2.0.xsd}valuesType"/>
 *         &lt;element name="requestsNameForDelete" type="{http://lsc-project.org/XSD/lsc-core-2.0.xsd}valuesType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "databaseDestinationServiceType", namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd", propOrder = {
    "requestNameForList",
    "requestNameForObject",
    "requestsNameForInsert",
    "requestsNameForUpdate",
    "requestsNameForDelete"
})
public class DatabaseDestinationServiceType
    extends ServiceType
{

    @XmlElement(namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd", required = true)
    protected String requestNameForList;
    @XmlElement(namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd", required = true)
    protected String requestNameForObject;
    @XmlElement(namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd", required = true)
    protected ValuesType requestsNameForInsert;
    @XmlElement(namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd", required = true)
    protected ValuesType requestsNameForUpdate;
    @XmlElement(namespace = "http://lsc-project.org/XSD/lsc-core-2.0.xsd", required = true)
    protected ValuesType requestsNameForDelete;

    /**
     * Gets the value of the requestNameForList property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
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
    public void setRequestNameForObject(String value) {
        this.requestNameForObject = value;
    }

    /**
     * Gets the value of the requestsNameForInsert property.
     * 
     * @return
     *     possible object is
     *     {@link ValuesType }
     *     
     */
    public ValuesType getRequestsNameForInsert() {
        return requestsNameForInsert;
    }

    /**
     * Sets the value of the requestsNameForInsert property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValuesType }
     *     
     */
    public void setRequestsNameForInsert(ValuesType value) {
        this.requestsNameForInsert = value;
    }

    /**
     * Gets the value of the requestsNameForUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link ValuesType }
     *     
     */
    public ValuesType getRequestsNameForUpdate() {
        return requestsNameForUpdate;
    }

    /**
     * Sets the value of the requestsNameForUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValuesType }
     *     
     */
    public void setRequestsNameForUpdate(ValuesType value) {
        this.requestsNameForUpdate = value;
    }

    /**
     * Gets the value of the requestsNameForDelete property.
     * 
     * @return
     *     possible object is
     *     {@link ValuesType }
     *     
     */
    public ValuesType getRequestsNameForDelete() {
        return requestsNameForDelete;
    }

    /**
     * Sets the value of the requestsNameForDelete property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValuesType }
     *     
     */
    public void setRequestsNameForDelete(ValuesType value) {
        this.requestsNameForDelete = value;
    }

}
