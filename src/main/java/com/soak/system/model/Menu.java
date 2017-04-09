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
public class Menu implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 200)
	private String name;

	@Column(length = 50)
	private String target;

	@Column(length = 200)
	private String link;

	@Column(length = 200)
	private String picUrl;

	@Column(length = 500)
	private String description;

	@Column(name = "menulevel")
	private Integer level;

	@ManyToOne(fetch = FetchType.EAGER)
	private Menu parent;

	@Column(name = "pos_no")
	private Integer posNo;

	@Enumerated(value = EnumType.STRING)
	private ValidFlag validFlag;
	
	public ValidFlag getValidFlag() {
		return validFlag;
	}

	public void setValidFlag(ValidFlag validFlag) {
		this.validFlag = validFlag;
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

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public Integer getPosNo() {
		return posNo;
	}

	public void setPosNo(Integer posNo) {
		this.posNo = posNo;
	}
}
