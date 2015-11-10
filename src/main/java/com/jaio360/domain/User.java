package com.jaio360.domain;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

	private String firstname;
	
	private String lastname;
	
        private String bussiness;
        
                
        private Date creationdate;
               
        
	private Integer age;
	
	private String street;
	
	private String city;
	
	private String postalCode;
	
	private String info;
	
	private String email;
	
	private String phone;

        
         public User (String email, String firstname, String bussiness, Date creationdate) {
		this.email = email;
		this.firstname = firstname;
		this.bussiness = bussiness;
		this.creationdate = creationdate;
	}

    public User() {
        
    }

  
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
        
           public String getBussiness() {
        return bussiness;
    }

    public void setBussiness(String bussiness) {
        this.bussiness = bussiness;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }
}
