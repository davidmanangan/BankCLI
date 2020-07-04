package iam.davidmanangan.bankcli.commands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import iam.davidmanangan.bankcli.model.BankTransaction;
import iam.davidmanangan.bankcli.model.UserSession;
import iam.davidmanangan.bankcli.repository.UserSessionRepository;
import iam.davidmanangan.bankcli.service.impl.AccountBalanceInquiryService;
import iam.davidmanangan.bankcli.service.impl.AccountingEntryService;

@ShellComponent
public class TopUpCommand {

	@Autowired
	AccountBalanceInquiryService accountBalanceInquiryService;
	
	@Autowired
	AccountingEntryService accountingEntryService;
	
	@Autowired
	UserSessionRepository userSessionRepository;
	
	@ShellMethod("Top up account balance")
	public void topup(@ShellOption String amount) {

		
		Integer transactionGroup = 1000;
		
		BankTransaction bankTx = new BankTransaction(transactionGroup,amount);
		
		accountingEntryService.processTransaction(bankTx);
		

		/**
		 * GET USER THAT IS CURRENTLY LOGGED IN
		 */
		List<UserSession> userSessions = userSessionRepository.findAll();
		
		UserSession currentLoggedInUser = userSessions.get(userSessions.size() - 1);
		
		accountBalanceInquiryService.processTransaction(new BankTransaction(transactionGroup,amount,currentLoggedInUser.getUserName()));
		
	}
}
