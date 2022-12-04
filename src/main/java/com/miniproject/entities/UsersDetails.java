package com.miniproject.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "users_details")
public class UsersDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_Id")
	private Integer userId;

	@Column(name = "full_name", length = 30)
	private String fullName;

	@Column(name = "email_id", length = 40)
	private String emailId;

	@Column(name = "mobile_no", length = 15)
	private long mobileNo;
	private LocalDate dob;

	@Column(length = 20)
	private long ssn;

	@Column(length = 8)
	private String gender;

	@Column(name = "acc_activation_status", length = 10)
	private String accActivationStatus;

	@Column(name = "password", length = 20)
	private String password;

	@Column(name = "registred_date")
	private LocalDate registredDate;

	@Column(name = "updated_date")
	private LocalDate updatedDate;

	@Column(name = "login_date_time")
	private LocalDate loginDateTime;

	@Column(name = "logout_date_time")
	private LocalDate logoutDateTime;

	@Column(name = "crated_by", length = 40)
	private String cratedBy;

	@Column(name = "updated_by", length = 40)
	private String updatedBy;

}
