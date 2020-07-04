package iam.davidmanangan.bankcli.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iam.davidmanangan.bankcli.model.AccountTransaction;
import iam.davidmanangan.bankcli.model.BankTransaction;
import iam.davidmanangan.bankcli.model.UserSession;
import iam.davidmanangan.bankcli.repository.AccountTransactionRepository;
import iam.davidmanangan.bankcli.repository.UserSessionRepository;
import iam.davidmanangan.bankcli.service.BankProcessingService;

@Service("accountingEntryService")
@Transactional
public class AccountingEntryService implements BankProcessingService{

	@Autowired
	UserSessionService userSessionService;
	
	@Autowired
	AccountTransactionRepository accountTransactionRepository;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public Double processTransaction(BankTransaction bankTransaction) {

		/**
		 * GET USER THAT IS CURRENTLY LOGGED IN
		 */
		UserSession currentLoggedInUser = userSessionService.getCurrentUser();

		/**
		 * GET TRANCODE IN DATABASE 
		 */

		Integer creditTxcode = bankTransaction.getTranCode() + 10;
		
		Integer debitTxcode = bankTransaction.getTranCode() + 11;
		
		List<Integer> creditTxcodes = jdbcTemplate.query(
				String.format("SELECT CODE FROM BANK_TRANSACTION_CODE WHERE CODE ='%d'", creditTxcode),
				(rs, row) -> rs.getInt(1));
		
		List<Integer> debitTxcodes = jdbcTemplate.query(
				String.format("SELECT CODE FROM BANK_TRANSACTION_CODE WHERE CODE ='%d'", debitTxcode),
				(rs, row) -> rs.getInt(1));

		
		/**
		 * GET THE TRANSFRER MODE IN TRANSACTION CODE 
		 * TRANSFER MODE IS THE DIGIT AT THE HUNDER'S PLACE VALUE
		 * E.G. TRNCODE OF 2100, HAS A TRANSFER MODE OF 1
		 * 
		 * TF MODE 1 or more - TRANSFER TO BE CREDITED TO ONE or more ACCOUNT
		 * 					 - TRANSFER TO BE DEBITTED FROM OWN ACCOUNT
		 *  
		 * TF MODE 0		 - TRANSFER TO BE CREDITED TO OWN ACCOUNT (NO OTHER ACCOUNT IS INVOLVE)
		 * 					 - TRANSFER TO BE DEBITTED FROM OWN ACOUNT BUT WITH ZERO AMOUNT 
		 */
		
		Integer fundTransferMode = bankTransaction.getTranCode() % 1000 / 100;

		AccountTransaction acctTxn = new AccountTransaction();
		
		if (fundTransferMode > 0) {

			if (creditTxcodes != null && creditTxcodes.size() > 0 && creditTxcodes.get(0) != null) {

				acctTxn = new AccountTransaction(bankTransaction.getUsername(), creditTxcode,
						creditTxcode % 100, new BigDecimal(bankTransaction.getAmount()));
				
				accountTransactionRepository.saveAndFlush(acctTxn);
				
			}
		} else {
			if (creditTxcodes != null && creditTxcodes.size() > 0 && creditTxcodes.get(0) != null) {

				acctTxn = new AccountTransaction(currentLoggedInUser.getUserName(), creditTxcode,
						creditTxcode % 100, new BigDecimal(bankTransaction.getAmount()));
				
				accountTransactionRepository.saveAndFlush(acctTxn);
				
			}
		}
		if (fundTransferMode > 0) {
			if (debitTxcodes != null && debitTxcodes.size() > 0 && debitTxcodes.get(0) != null) {
				acctTxn = new AccountTransaction(currentLoggedInUser.getUserName(), debitTxcode,
						debitTxcode % 100, new BigDecimal(bankTransaction.getAmount()));
				
				accountTransactionRepository.saveAndFlush(acctTxn);
			
			}	
		}else {
			if (debitTxcodes != null && debitTxcodes.size() > 0 && debitTxcodes.get(0) != null) {
				acctTxn = new AccountTransaction(currentLoggedInUser.getUserName(), debitTxcode,
						debitTxcode % 100, BigDecimal.ZERO);
				
				accountTransactionRepository.saveAndFlush(acctTxn);
			
			}
		}
		
		return new BigDecimal(bankTransaction.getAmount()).doubleValue();
		
		
	}

}
