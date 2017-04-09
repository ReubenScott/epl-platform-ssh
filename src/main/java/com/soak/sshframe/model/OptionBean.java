package com.soak.sshframe.model;

import com.soak.sshframe.annotation.XMLParse;
import com.soak.sshframe.annotation.XMLParse.XMLType;

public class OptionBean {

	@XMLParse(key="class")
	private String cls;
	@XMLParse
	private String value;
	@XMLParse
	private boolean selected;
	@XMLParse(type=XMLType.SimpleElement)
	private String desc;
	
	public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
