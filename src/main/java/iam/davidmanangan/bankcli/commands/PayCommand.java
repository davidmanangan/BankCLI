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
public class PayCommand {

	@Autowired
	AccountBalanceInquiryService accountBalanceInquiryService;
	
	@Autowired
	AccountingEntryService accountingEntryService;
	
	@ShellMethod("Send Payment to another account")
	public void pay(@ShellOption String username, @ShellOption String amount) {
		
		Integer transactionGroup = 2100;
		
		String receipientUserName = username.toLowerCase();
		
		BankTransaction bankTx = new BankTransaction(transactionGroup,amount,receipientUserName);
		
		accountingEntryService.processTransaction(bankTx);
		
		accountBalanceInquiryService.processTransaction(new BankTransaction(transactionGroup,amount,receipientUserName));
	}
}
