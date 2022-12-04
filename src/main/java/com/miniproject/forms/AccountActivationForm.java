package com.miniproject.forms;

import lombok.Data;

@Data
public class AccountActivationForm {
	private String email;
	private String tempPw;
	private String newPw;
	private String  conformedPw;
	
}
