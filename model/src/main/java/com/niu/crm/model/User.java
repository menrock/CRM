package com.niu.crm.model;

import java.util.Date;
import java.util.Collection;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public class User extends BaseModel implements UserDetails{
	private static final long serialVersionUID = 1557897999112008628L;
	
	private static final SimpleGrantedAuthority role_auth_admin = new SimpleGrantedAuthority(
			"ROLE_ADMIN");
	
	private String name;	
	private String gender;	
	private String account=null;
	private String password;

	private Long unitId;
	private Long companyId;
	private String phone;
	private String email;
	private String weixinId;
	private String position;
	
	private boolean online;
	private boolean enabled;
	private Date createdAt;
	private Long creatorId;
	private Date updatedAt;
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAccount(){
		return this.account;
	}
	public void setAccount(String account){
		this.account = account;
	}
	
	//支持 接口 UserDetails
	public String getUsername(){
		return getAccount();
	}
	public void setUsername(String username){
		setAccount(username);
	}
	
	public String getGender(){
		return gender;
	}
	public void setGender(String gender){
		this.gender = gender;
	}
	
	public String getPassword(){
		return this.password;
	}
	public void setPassword(String password){
		this.password = password;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		auths.add(role_auth_admin);
		return auths;
	}
	
	@Override
	public boolean isAccountNonExpired(){
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked(){
		return true;
	}

    public boolean isCredentialsNonExpired(){
    	return true;
    }
	
    public boolean getOnline(){
		return online;
	}
	public void setOnline(boolean online){
		this.online = online;
	}
	
    public boolean isEnabled(){
		return enabled;
	}
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		if( StringUtils.isEmpty(phone) )
			this.phone = null;
		else
			this.phone = phone.trim();
	}
	
	public String getWeixinId() {
		return weixinId;
	}
	public void setWeixinId(String weixinId) {
		this.weixinId = weixinId;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
}
