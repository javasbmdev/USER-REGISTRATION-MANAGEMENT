package com.miniproject.service;

import java.util.List;

import com.miniproject.forms.AccountActivationForm;
import com.miniproject.forms.User;
import com.miniproject.forms.UserLoginForm;

 

public interface UserManagementService {

	public boolean saveUserDetails(User user);

	public boolean activateUserAccount(AccountActivationForm activationForm);

	public String login(UserLoginForm loginForm);

	public List<User> getAllUsers();

	public User getUserById(Integer userId);

	public boolean deleteUserById(Integer userId);

	public boolean changeAccountStatus(Integer userId, String accStatus);

	public String forgotPw(String mailId);
}
