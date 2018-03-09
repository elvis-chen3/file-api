package com.file.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "METADATA")
public class Metadata {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "filename")
	private String fileName;

	@Column(name = "filesize")
	private String fileSize;

	@Column(name = "contenttype")
	private String contentType;

	@Column(name = "created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	public Metadata() {
	}
	
	public Metadata(long id, String fileName, String fileSize, String contentType, Date created) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.contentType = contentType;
		this.created = created;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Date getDate() {
		return created;
	}

	public void setDate(Date date) {
		this.created = date;
	}

	@Override
	public String toString() {
		return "Metadata [id=" + id + ", fileName=" + fileName + ", fileSize=" + fileSize + ", contentType="
				+ contentType + "]";
	}
}
