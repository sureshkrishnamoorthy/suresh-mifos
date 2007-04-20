//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.2-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.04.03 at 01:24:13 PM PDT 
//


package org.mifos.migration.generated;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}name"/>
 *         &lt;element ref="{}officeId"/>
 *         &lt;element ref="{}loanOfficerId"/>
 *         &lt;choice>
 *           &lt;element ref="{}weeklyMeeting"/>
 *           &lt;element ref="{}monthlyMeeting"/>
 *         &lt;/choice>
 *         &lt;element ref="{}externalId" minOccurs="0"/>
 *         &lt;element ref="{}mfiJoiningDate"/>
 *         &lt;element ref="{}address" minOccurs="0"/>
 *         &lt;element ref="{}customField" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}feeAmount" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "name",
    "officeId",
    "loanOfficerId",
    "weeklyMeeting",
    "monthlyMeeting",
    "externalId",
    "mfiJoiningDate",
    "address",
    "customField",
    "feeAmount"
})
@XmlRootElement(name = "center")
public class Center {

    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String name;
    protected short officeId;
    protected short loanOfficerId;
    protected WeeklyMeeting weeklyMeeting;
    protected MonthlyMeeting monthlyMeeting;
    protected String externalId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar mfiJoiningDate;
    protected Address address;
    protected List<CustomField> customField;
    protected List<FeeAmount> feeAmount;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the officeId property.
     * 
     */
    public short getOfficeId() {
        return officeId;
    }

    /**
     * Sets the value of the officeId property.
     * 
     */
    public void setOfficeId(short value) {
        this.officeId = value;
    }

    /**
     * Gets the value of the loanOfficerId property.
     * 
     */
    public short getLoanOfficerId() {
        return loanOfficerId;
    }

    /**
     * Sets the value of the loanOfficerId property.
     * 
     */
    public void setLoanOfficerId(short value) {
        this.loanOfficerId = value;
    }

    /**
     * Gets the value of the weeklyMeeting property.
     * 
     * @return
     *     possible object is
     *     {@link WeeklyMeeting }
     *     
     */
    public WeeklyMeeting getWeeklyMeeting() {
        return weeklyMeeting;
    }

    /**
     * Sets the value of the weeklyMeeting property.
     * 
     * @param value
     *     allowed object is
     *     {@link WeeklyMeeting }
     *     
     */
    public void setWeeklyMeeting(WeeklyMeeting value) {
        this.weeklyMeeting = value;
    }

    /**
     * Gets the value of the monthlyMeeting property.
     * 
     * @return
     *     possible object is
     *     {@link MonthlyMeeting }
     *     
     */
    public MonthlyMeeting getMonthlyMeeting() {
        return monthlyMeeting;
    }

    /**
     * Sets the value of the monthlyMeeting property.
     * 
     * @param value
     *     allowed object is
     *     {@link MonthlyMeeting }
     *     
     */
    public void setMonthlyMeeting(MonthlyMeeting value) {
        this.monthlyMeeting = value;
    }

    /**
     * Gets the value of the externalId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * Sets the value of the externalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalId(String value) {
        this.externalId = value;
    }

    /**
     * Gets the value of the mfiJoiningDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMfiJoiningDate() {
        return mfiJoiningDate;
    }

    /**
     * Sets the value of the mfiJoiningDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMfiJoiningDate(XMLGregorianCalendar value) {
        this.mfiJoiningDate = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link Address }
     *     
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link Address }
     *     
     */
    public void setAddress(Address value) {
        this.address = value;
    }

    /**
     * Gets the value of the customField property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customField property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomField().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomField }
     * 
     * 
     */
    public List<CustomField> getCustomField() {
        if (customField == null) {
            customField = new ArrayList<CustomField>();
        }
        return this.customField;
    }

    /**
     * Gets the value of the feeAmount property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the feeAmount property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFeeAmount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FeeAmount }
     * 
     * 
     */
    public List<FeeAmount> getFeeAmount() {
        if (feeAmount == null) {
            feeAmount = new ArrayList<FeeAmount>();
        }
        return this.feeAmount;
    }

}
