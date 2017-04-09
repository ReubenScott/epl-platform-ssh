package com.soak.system.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.soak.common.constant.ValidFlag;



@Entity
public class Theme implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length=100)
	private String name;
	
	@Column(length=2000)
	private String description;
	
	@Enumerated(value = EnumType.STRING)
	private ValidFlag validFlag;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Theme parent;

	public Theme getParent() {
		return parent;
	}

	public void setParent(Theme parent) {
		this.parent = parent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public ValidFlag getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(ValidFlag validFlag) {
		this.validFlag = validFlag;
	}
}
