package com.miniproject.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.miniproject.forms.AccountActivationForm;
import com.miniproject.forms.User;
import com.miniproject.forms.UserLoginForm;
import com.miniproject.service.UserManagementService;

@RestController
public class UserManagementRestController {
	@Autowired
	private UserManagementService service;

	@PostMapping("/create-account")
	public ResponseEntity<String> saveUserRegistrationDetails(@RequestBody User user) {
		boolean isSaved = service.saveUserDetails(user);
		if (isSaved) {
			return new ResponseEntity<String>("registration sucess ", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User account creation failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/activate-account")
	public ResponseEntity<String> activateAccount(@RequestBody AccountActivationForm activationForm) {
		boolean isActivated = service.activateUserAccount(activationForm);
		if (isActivated) {
			return new ResponseEntity<String>("Account Activated ", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Account activation failed invalid temporery password",
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		List<User> allUsers = service.getAllUsers();
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

	@GetMapping("/get-user/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
		User user = service.getUserById(userId);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@DeleteMapping("/delete-user/{userId}")
	public ResponseEntity<String> deleteUserById(@PathVariable Integer userId) {
		boolean isDeleted = service.deleteUserById(userId);
		if (isDeleted) {
			return new ResponseEntity<String>("Delete sucess", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Delete faild", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/change-status/{userID}/{status}") 
		public ResponseEntity<String> changeStatus(@PathVariable Integer userID,@PathVariable String status){
			boolean isChanged = service.changeAccountStatus(userID, status);
			if(isChanged) {
				return new ResponseEntity<String>("Status change sucess",HttpStatus.OK);
			}else {
				return new ResponseEntity<String>("Status change failed", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	 
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserLoginForm loginForm){
	        String login = service.login(loginForm);
	        return new ResponseEntity<String>(login,HttpStatus.OK);
	        
	}
	
	@GetMapping("/forgotpw/{emailID}")
	public ResponseEntity<String> resetPassword(@PathVariable String emailID){
		String forgotPw = service.forgotPw(emailID);
		return new ResponseEntity<String> (forgotPw,HttpStatus.OK);
		
	}
	
	
	
	

}
