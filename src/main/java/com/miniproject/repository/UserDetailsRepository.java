package com.miniproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miniproject.entities.UsersDetails;
import com.miniproject.forms.User;
@Repository
public interface UserDetailsRepository extends JpaRepository<UsersDetails, Integer>{
	boolean existsUsersDetailsByEmailId(String emailId);
	boolean existsUsersDetailsByEmailIdAndPassword(String emailId,String password);
	List<UsersDetails> findByEmailId(String emailId);
	List<UsersDetails> findByEmailIdAndPassword(String emailId,String password);
}
