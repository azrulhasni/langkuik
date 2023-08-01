//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.09.27 at 03:06:45 PM MYT 
//


package com.azrul.langkuik.framework.workflow.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="workflow">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;choice maxOccurs="unbounded" minOccurs="0">
 *                     &lt;element name="startEvent" type="{}StartEvent" maxOccurs="unbounded"/>
 *                     &lt;element name="service" type="{}ServiceActivity" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element name="human" type="{}HumanActivity" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element name="xor" type="{}XorActivity" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element name="xor-atleast-one-approval" type="{}XorAtleastOneApprovalActivity" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element name="xor-unanimous-approval" type="{}XorUnanimousApprovalActivity" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element name="xor-majority-approval" type="{}XorMajorityApprovalActivity" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element name="end" type="{}End"/>
 *                   &lt;/choice>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/sequence>
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
    "version",
    "description",
    "workflow"
})
@XmlRootElement(name = "bizProcess")
public class BizProcess {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String version;
    @XmlElement(required = true)
    protected String description;
    protected BizProcess.Workflow workflow;

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
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the workflow property.
     * 
     * @return
     *     possible object is
     *     {@link BizProcess.Workflow }
     *     
     */
    public BizProcess.Workflow getWorkflow() {
        return workflow;
    }

    /**
     * Sets the value of the workflow property.
     * 
     * @param value
     *     allowed object is
     *     {@link BizProcess.Workflow }
     *     
     */
    public void setWorkflow(BizProcess.Workflow value) {
        this.workflow = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice maxOccurs="unbounded" minOccurs="0">
     *         &lt;element name="startEvent" type="{}StartEvent" maxOccurs="unbounded"/>
     *         &lt;element name="service" type="{}ServiceActivity" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="human" type="{}HumanActivity" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="xor" type="{}XorActivity" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="xor-atleast-one-approval" type="{}XorAtleastOneApprovalActivity" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="xor-unanimous-approval" type="{}XorUnanimousApprovalActivity" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="xor-majority-approval" type="{}XorMajorityApprovalActivity" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="end" type="{}End"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "startEventOrServiceOrHuman"
    })
    public static class Workflow {

        @XmlElements({
            @XmlElement(name = "startEvent", type = StartEvent.class),
            @XmlElement(name = "service", type = ServiceActivity.class),
            @XmlElement(name = "human", type = HumanActivity.class),
            @XmlElement(name = "xor", type = XorActivity.class),
            @XmlElement(name = "xor-atleast-one-approval", type = XorAtleastOneApprovalActivity.class),
            @XmlElement(name = "xor-unanimous-approval", type = XorUnanimousApprovalActivity.class),
            @XmlElement(name = "xor-majority-approval", type = XorMajorityApprovalActivity.class),
            @XmlElement(name = "end", type = End.class)
        })
        protected List<Activity> startEventOrServiceOrHuman;

        /**
         * Gets the value of the startEventOrServiceOrHuman property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the startEventOrServiceOrHuman property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getStartEventOrServiceOrHuman().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link StartEvent }
         * {@link ServiceActivity }
         * {@link HumanActivity }
         * {@link XorActivity }
         * {@link XorAtleastOneApprovalActivity }
         * {@link XorUnanimousApprovalActivity }
         * {@link XorMajorityApprovalActivity }
         * {@link End }
         * 
         * 
         */
        public List<Activity> getStartEventOrServiceOrHuman() {
            if (startEventOrServiceOrHuman == null) {
                startEventOrServiceOrHuman = new ArrayList<Activity>();
            }
            return this.startEventOrServiceOrHuman;
        }

        public List<StartEvent> getStartEvents() {
            List<StartEvent> startEvents = new ArrayList<>();
            for (Activity activity : getStartEventOrServiceOrHuman()) {
                if (activity instanceof StartEvent) {
                    startEvents.add((StartEvent) activity);
                }
            }
            return startEvents;
        }

    }
    
    public List<StartEvent> getStartEvents() {
        return this.workflow.getStartEvents();
    }
    
    public BizProcess withName(String name) {
        this.name = name;
        return this;
    }

}
