package com.gdpdemo.GDPSprint1Project;

import java.util.Date;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;




@Entity
@Table(name = "home",
		uniqueConstraints = {
					@UniqueConstraint(columnNames = "email")
})
public class Home {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	private static final long OTP_VALID_DURATION = 5 * 60 * 1000;
	
	@NotBlank(message= "First Name is required!")
	private String firstName;
	@NotBlank(message= "Last Name is required!")
	private String lastName;
	
	@NotBlank(message= "Email field is required!")
	@Email(regexp="^[a-zA-Z0-9._%+-]+@(gmail\\.com|nwmissouri\\.edu)$", message="Enter email in required format")
	private String email;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@NotBlank(message= "Password field is required!")
	/*
	 * @Email(regexp=
	 * "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
	 * message="Password must contain atleast one upper case, lower case, number and special characters!!"
	 * )
	 */
	private String password;
	@NotBlank(message= "Repeat Password field is required!")
	private String rePassword;
	@Column(name = "one_time_password")
    private String oneTimePassword; 
    @Column(name = "otp_requested_time")
    private Date otpRequestedTime;
    
//    @OneToMany(mappedBy = "id_author", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
//    private List<PostsAuthors> postsAuthors;
     
	
	
	public String getRePassword() {
		return rePassword;
	}
	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOneTimePassword() {
		return oneTimePassword;
	}
	public Date getOtpRequestedTime() {
		return otpRequestedTime;
	}
	public void setOneTimePassword(String oneTimePassword) {
		this.oneTimePassword = oneTimePassword;
	}
	public void setOtpRequestedTime(Date otpRequestedTime) {
		this.otpRequestedTime = otpRequestedTime;
	}
	
	
	 @Override
	public String toString() {
		return "Home [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", password=" + password
				+ ", rePassword=" + rePassword + "]";
	}
	public boolean isOTPRequired() {
	        if (this.getOneTimePassword() == null) {
	            return false;
	        }
	         
	        long currentTimeInMillis = System.currentTimeMillis();
	        long otpRequestedTimeInMillis = this.otpRequestedTime.getTime();
	         
	        if (otpRequestedTimeInMillis + OTP_VALID_DURATION < currentTimeInMillis) {
	            // OTP expires
	            return false;
	        }
	         
	        return true;
	    }
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
