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
import iam.davidmanangan.bankcli.service.impl.UserSessionService;

@ShellComponent
public class TopUpCommand {
	
	@Autowired
	UserSessionService userSessionService;
	
	@Autowired
	AccountBalanceInquiryService accountBalanceInquiryService;
	
	@Autowired
	AccountingEntryService accountingEntryService;
	
	@ShellMethod("Top up account balance")
	public void topup(@ShellOption String amount) {

		
		Integer transactionGroup = 1000;
		
		BankTransaction bankTx = new BankTransaction(transactionGroup,amount);
		
		accountingEntryService.processTransaction(bankTx);
		

		/**
		 * GET USER THAT IS CURRENTLY LOGGED IN
		 */
		UserSession currentLoggedInUser = userSessionService.getCurrentUser(); 
		
		accountBalanceInquiryService.processTransaction(new BankTransaction(transactionGroup,amount,currentLoggedInUser.getUserName()));
		
	}
}
