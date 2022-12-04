package com.miniproject.forms;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
	private String fullName;
	private long mobileNo;
	private String emailId;
	private String gender;
	private LocalDate dob;
	private long ssn;
}
