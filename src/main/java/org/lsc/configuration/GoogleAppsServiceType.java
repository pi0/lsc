//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.17 at 10:30:16 PM CEST 
//


package org.lsc.configuration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for googleAppsServiceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="googleAppsServiceType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://lsc-project.org/XSD/lsc-core-2.0.xsd}serviceType">
 *       &lt;sequence>
 *         &lt;element name="apiCategory" type="{http://lsc-project.org/XSD/lsc-core-2.0.xsd}googleAppsProvisioningType"/>
 *         &lt;element name="quotaLimitInMb" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "googleAppsServiceType", propOrder = {
    "apiCategory",
    "quotaLimitInMb"
})
public class GoogleAppsServiceType
    extends ServiceType
{

    @XmlElement(required = true, defaultValue = "UserAccounts")
    protected GoogleAppsProvisioningType apiCategory = GoogleAppsProvisioningType.USER_ACCOUNTS;
    @XmlElement(defaultValue = "25000")
    protected Integer quotaLimitInMb = 25000;

    /**
     * Gets the value of the apiCategory property.
     * 
     * @return
     *     possible object is
     *     {@link GoogleAppsProvisioningType }
     *     
     */
    public GoogleAppsProvisioningType getApiCategory() {
        return apiCategory;
    }

    /**
     * Sets the value of the apiCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link GoogleAppsProvisioningType }
     *     
     */
    public void setApiCategory(GoogleAppsProvisioningType value) {
        this.apiCategory = value;
    }

    /**
     * Gets the value of the quotaLimitInMb property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getQuotaLimitInMb() {
        return quotaLimitInMb;
    }

    /**
     * Sets the value of the quotaLimitInMb property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setQuotaLimitInMb(Integer value) {
        this.quotaLimitInMb = value;
    }

}
