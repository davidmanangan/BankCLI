package iam.davidmanangan.bankcli.commands;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.transaction.annotation.Transactional;

import iam.davidmanangan.bankcli.model.BankTransaction;
import iam.davidmanangan.bankcli.model.User;
import iam.davidmanangan.bankcli.model.UserSession;
import iam.davidmanangan.bankcli.repository.UserRepository;
import iam.davidmanangan.bankcli.repository.UserSessionRepository;
import iam.davidmanangan.bankcli.service.impl.AccountBalanceInquiryService;
import iam.davidmanangan.bankcli.service.impl.UserSessionService;

@ShellComponent("loginCommand")
public class LoginCommand {

	
	@Autowired
	AccountBalanceInquiryService accountBalanceInquiryService;
	
	@Autowired
	UserSessionService userSessionService;
	
	@ShellMethod("Login as client. Creates client username if its not yet created.")
	public void login(@ShellOption String username) {
		
		String loginUser = username.toLowerCase();

		userSessionService.logUserSession(username);
		
		System.out.println(String.format("Hi, %s", username));
		
		accountBalanceInquiryService.processTransaction(new BankTransaction(0,"",loginUser));
	}
	
}
