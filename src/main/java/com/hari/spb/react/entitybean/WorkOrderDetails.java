package com.hari.spb.react.entitybean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "work_order")
@EntityListeners(AuditingEntityListener.class)
public class WorkOrderDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long workOrderId;

	@PrimaryKeyJoinColumn
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assetId", nullable = false)
    private AssetMapping assetMapping;

	@Column(nullable = false)
	private String woStatus;

	@Column(nullable = false)
	private String woType;

	@Column(nullable = false)
	private String priority;

	@Column(nullable = false)
	private String reportedDate;

	@Column(nullable = true)
	private Date targetStartDate;

	@Column(nullable = true)
	private Date targetFinishDate;

	@Column(nullable = true)
	private Date scheduledStartDate;

	@Column(nullable = true)
	private Date scheduledFinishDate;
	
	@Column(nullable = true)
	private Date actualStartDate;
	
	@Column(nullable = true)
	private Date actualFinishDate;
	
	@Column(nullable = true)
	private Date actualCloseDate;
	
	@Column(nullable = true)
	private String failureClass;
	
	@Column(nullable = true)
	private String problem;
	
	@Column(nullable = true)
	private String cause;
	
	@Column(nullable = true)
	private String remedy;

	public Long getWorkOrderId() {
		return workOrderId;
	}

	public void setWorkOrderId(Long workOrderId) {
		this.workOrderId = workOrderId;
	}

	public AssetMapping getAssetMapping() {
		return assetMapping;
	}

	public void setAssetMapping(AssetMapping assetMapping) {
		this.assetMapping = assetMapping;
	}

	public String getWoStatus() {
		return woStatus;
	}

	public void setWoStatus(String woStatus) {
		this.woStatus = woStatus;
	}

	public String getWoType() {
		return woType;
	}

	public void setWoType(String woType) {
		this.woType = woType;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getReportedDate() {
		return reportedDate;
	}

	public void setReportedDate(String reportedDate) {
		this.reportedDate = reportedDate;
	}

	public Date getTargetStartDate() {
		return targetStartDate;
	}

	public void setTargetStartDate(Date targetStartDate) {
		this.targetStartDate = targetStartDate;
	}

	public Date getTargetFinishDate() {
		return targetFinishDate;
	}

	public void setTargetFinishDate(Date targetFinishDate) {
		this.targetFinishDate = targetFinishDate;
	}

	public Date getScheduledStartDate() {
		return scheduledStartDate;
	}

	public void setScheduledStartDate(Date scheduledStartDate) {
		this.scheduledStartDate = scheduledStartDate;
	}

	public Date getScheduledFinishDate() {
		return scheduledFinishDate;
	}

	public void setScheduledFinishDate(Date scheduledFinishDate) {
		this.scheduledFinishDate = scheduledFinishDate;
	}

	public Date getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public Date getActualFinishDate() {
		return actualFinishDate;
	}

	public void setActualFinishDate(Date actualFinishDate) {
		this.actualFinishDate = actualFinishDate;
	}

	public Date getActualCloseDate() {
		return actualCloseDate;
	}

	public void setActualCloseDate(Date actualCloseDate) {
		this.actualCloseDate = actualCloseDate;
	}

	public String getFailureClass() {
		return failureClass;
	}

	public void setFailureClass(String failureClass) {
		this.failureClass = failureClass;
	}

	public String getProblem() {
		return problem;
	}

	public void setProblem(String problem) {
		this.problem = problem;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public String getRemedy() {
		return remedy;
	}

	public void setRemedy(String remedy) {
		this.remedy = remedy;
	}
}
