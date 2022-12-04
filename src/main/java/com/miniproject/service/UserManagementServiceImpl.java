package com.miniproject.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miniproject.entities.UsersDetails;
import com.miniproject.forms.AccountActivationForm;
import com.miniproject.forms.User;
import com.miniproject.forms.UserLoginForm;
import com.miniproject.repository.UserDetailsRepository;
import com.miniproject.utils.EmailUtils;

@Service
public class UserManagementServiceImpl implements UserManagementService {
	private static final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	@Autowired
	private UserDetailsRepository repository;
	@Autowired
	EmailUtils emailUtils;

	private boolean sendEmail;

	// generating random password
	private String generatePassword(int lenght) {
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(lenght);
		for (int i = 0; i < lenght; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}

	// reading email body
	private String readEmailBody(String fullname, String password, File file) {
		String url = "";
		String mailBody = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuffer buffer = new StringBuffer();
			String line = reader.readLine();
			while (line != null) {
				buffer.append(line);
				line = reader.readLine();
			}
			reader.close();
			mailBody = buffer.toString();
			mailBody = mailBody.replace("{FULLNAME}", fullname);
			mailBody = mailBody.replace("{TEMP-PWD}", password);
			mailBody = mailBody.replace("{URL}", url);
			mailBody = mailBody.replace("{PWD}", password);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailBody;
	}

	@Override
	public boolean saveUserDetails(User user) {
		UsersDetails details = new UsersDetails();
		BeanUtils.copyProperties(user, details);
		if (repository.existsUsersDetailsByEmailId(user.getEmailId())) {
			return false;
		} else {
			details.setPassword(generatePassword(8));
			details.setAccActivationStatus("In-Active");
			details = repository.save(details);

			String subject = "Reset password";
			// String sfile = "classpath:REG-EMAIL-BODY.txt";

			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("REG-EMAIL-BODY.txt").getFile());

			String body = readEmailBody(details.getFullName(), details.getPassword(), file);
			emailUtils.sendEmail(details.getEmailId(), subject, body);
			return details.getUserId() != null;
		}

	}

	/*
	 * private void sendEmail(UsersDetails usersDetails) { String from =
	 * "fredriknelson88@gmail.com"; String to = usersDetails.getEmailId();
	 * 
	 * SimpleMailMessage message = new SimpleMailMessage();
	 * 
	 * message.setFrom(from); message.setTo(to);
	 * message.setSubject("Reset Password"); message.setText("Hello " +
	 * usersDetails.getFullName() + " your temporary passwored is '" +
	 * usersDetails.getPassword() + "' you have to set a strong password ");
	 * javaMailSender.send(message);
	 * 
	 * }
	 */

	@Override
	public boolean activateUserAccount(AccountActivationForm activationForm) {
		UsersDetails details = new UsersDetails();
		List<UsersDetails> findByEmailIdPassword = repository.findByEmailIdAndPassword(activationForm.getEmail(),
				activationForm.getTempPw());
		if (findByEmailIdPassword.isEmpty()) {
			return false;
		} else {
			details = findByEmailIdPassword.get(0);
			details.setPassword(activationForm.getConformedPw());
			details.setAccActivationStatus("Active");
			details.setUpdatedDate(LocalDate.now());
			details.setUpdatedBy(findByEmailIdPassword.get(0).getFullName());
			details = repository.save(details);
			return true;
		}
	}

	@Override
	public String login(UserLoginForm loginForm) {
		String message = "";
		List<UsersDetails> users = repository.findByEmailIdAndPassword(loginForm.getEmailId(), loginForm.getPassword());
		if (users.isEmpty()) {
			message = "invalid credentials";
		} else if (users.get(0).getAccActivationStatus().equalsIgnoreCase("Active")) {
			message = "Login Sucess";
		} else {
			message = "Account not Activated";
		}
		return message;
	}

	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		repository.findAll().forEach(entity -> {
			User user = new User();
			BeanUtils.copyProperties(entity, user);
			users.add(user);
		});

		return users;
	}

	@Override
	public User getUserById(Integer userId) {
		Optional<UsersDetails> findById = repository.findById(userId);
		if (findById.isPresent()) {
			User user = new User();
			BeanUtils.copyProperties(findById.get(), user);
			return user;
		}
		return null;
	}

	@Override
	public boolean deleteUserById(Integer userId) {
		try {
			repository.deleteById(userId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean changeAccountStatus(Integer userId, String accStatus) {
		Optional<UsersDetails> findById = repository.findById(userId);
		if (findById.isPresent()) {
			UsersDetails details = findById.get();
			details.setAccActivationStatus(accStatus);
			repository.save(details);
			return true;
		}
		return false;
	}

	@Override
	public String forgotPw(String mailId) {
		UsersDetails usersDetails = null;
		List<UsersDetails> details = repository.findByEmailId(mailId);
		usersDetails = details.get(0);
		usersDetails.setPassword(generatePassword(8));
		String msg = "";
		if (details.isEmpty()) {
			msg = "Invalid EmailId";
		}
		String subject = "forgot password";

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("FORGOT-PWD-BODY.txt").getFile());

		String body = readEmailBody(usersDetails.getFullName(), usersDetails.getPassword(), file);
		sendEmail = emailUtils.sendEmail(usersDetails.getEmailId(), subject, body);
		if (sendEmail) {
			return "password sent to yur emailId";
		}
		return null;
	}

}
