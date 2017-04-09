package com.soak.system.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.soak.common.constant.EGenderType;
import com.soak.common.constant.ValidFlag;



@Entity
@NamedQueries( { @NamedQuery(name = "getPassword", query = "select u from User u where u.loginName=:loginName") })
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 50)
	private String name;

	@Column(length = 50)
	private String workcode;
	
	@Column
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(length = 20)
	private String loginName;

	@Column(length = 100)
	private String password;

	@Column(length = 100)
	private String email;

	@Column(length = 200)
	private String telephone;

	@Column(length = 200)
	private String address;

	@Column(length = 2000)
	private String webContentConfig;
	
	@Column
	private String positionA;
	
	@Column
	private String positionC;
	
	public String getPositionC() {
		return positionC;
	}

	public void setPositionC(String positionC) {
		this.positionC = positionC;
	}

	@Column
	private Integer plevel;
	
	@Column
	private String positionB;
	
	@ManyToOne(fetch = FetchType.EAGER,cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private Organization parent;
	
	@Column
	private Boolean isLeave;
	
	@ManyToOne
	private User b;

	public Boolean getIsLeave() {
		return isLeave;
	}

	public void setIsLeave(Boolean isLeave) {
		this.isLeave = isLeave;
	}

	public User getB() {
		return b;
	}

	public void setB(User bUser) {
		this.b = bUser;
	}

	public Organization getParent() {
		return parent;
	}

	public void setParent(Organization parent) {
		this.parent = parent;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name = "t_user_org", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "org_id"))
	private List<Organization> orgList = new ArrayList<Organization>();
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name = "t_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roleList = new ArrayList<Role>();

	public List<Organization> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<Organization> orgList) {
		this.orgList = orgList;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@Enumerated(value = EnumType.STRING)
	private EGenderType sex;

	@Column
	private Integer age;

	@Column(length=64)
	private String pinyin;

	@Enumerated(value = EnumType.STRING)
	private ValidFlag validFlag = ValidFlag.VALID;

	public String getWebContentConfig() {
		return webContentConfig;
	}

	public void setWebContentConfig(String webContentConfig) {
		this.webContentConfig = webContentConfig;
	}


	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
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

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public EGenderType getSex() {
		return sex;
	}

	public void setSex(EGenderType sex) {
		this.sex = sex;
	}

	public String getWorkcode() {
		return workcode;
	}

	public void setWorkcode(String workcode) {
		this.workcode = workcode;
	}

	public String getPositionA() {
		return positionA;
	}

	public void setPositionA(String positionA) {
		this.positionA = positionA;
	}

	public Integer getPlevel() {
		return plevel;
	}

	public void setPlevel(Integer level) {
		this.plevel = level;
	}

	public String getPositionB() {
		return positionB;
	}

	public void setPositionB(String positionB) {
		this.positionB = positionB;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
