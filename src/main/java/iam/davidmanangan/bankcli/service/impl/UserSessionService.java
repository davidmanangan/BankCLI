package iam.davidmanangan.bankcli.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iam.davidmanangan.bankcli.model.User;
import iam.davidmanangan.bankcli.model.UserSession;
import iam.davidmanangan.bankcli.repository.UserRepository;
import iam.davidmanangan.bankcli.repository.UserSessionRepository;

@Service("userSessionService")
public class UserSessionService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserSessionRepository userSessionRepository;
	
	@Transactional
	public void logUserSession(String username) {
		
		String loginUser = username.toLowerCase();
		
		Optional<User> user = userRepository.findById(loginUser);
		
		if(!user.isPresent()) {
			userRepository.save(new User(loginUser));
		}
		
		userSessionRepository.saveAndFlush(new UserSession(loginUser,new Date()));
	}
	
	public UserSession getCurrentUser() {
		
		List<UserSession> userSessions = userSessionRepository.findAll();
		if(userSessions != null && userSessions.size() > 0) {
			
			return userSessions.get(userSessions.size() - 1);
			
		}
		return null;
	}
	
}
