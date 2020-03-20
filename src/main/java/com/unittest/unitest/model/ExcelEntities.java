/**
 * 
 */
package com.unittest.unitest.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Supriya.Srivastava
 *
 */
@Entity
@Table(name="excelEntities")
public class ExcelEntities implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer testRun;
	private Integer faliures;
	private Integer errors;
	private Integer skipped;
	private String className;
	private Date date;
	
	/**
	 * @return the date
	 */
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	public Integer getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return the testRun
	 */
	@Column(name="testRun")
	public Integer getTestRun() {
		return testRun;
	}
	/**
	 * @param testRun the testRun to set
	 */
	public void setTestRun(Integer testRun) {
		this.testRun = testRun;
	}
	/**
	 * @return the faliures
	 */
	@Column(name="faliures")
	public Integer getFaliures() {
		return faliures;
	}
	/**
	 * @param faliures the faliures to set
	 */
	public void setFaliures(Integer faliures) {
		this.faliures = faliures;
	}
	/**
	 * @return the errors
	 */
	@Column(name="errors")
	public Integer getErrors() {
		return errors;
	}
	/**
	 * @param errors the errors to set
	 */
	public void setErrors(Integer errors) {
		this.errors = errors;
	}
	/**
	 * @return the skipped
	 */
	@Column(name="skipped")
	public Integer getSkipped() {
		return skipped;
	}
	/**
	 * @param skipped the skipped to set
	 */
	public void setSkipped(Integer skipped) {
		this.skipped = skipped;
	}
	/**
	 * @return the className
	 */
	@Column(name="className")
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	@Column(name="date")
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
