package iam.davidmanangan.bankcli.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iam.davidmanangan.bankcli.model.BankTransaction;
import iam.davidmanangan.bankcli.model.LoanLedger;
import iam.davidmanangan.bankcli.model.UserSession;
import iam.davidmanangan.bankcli.repository.LoanLedgerRepository;
import iam.davidmanangan.bankcli.repository.UserSessionRepository;
import iam.davidmanangan.bankcli.service.BankProcessingService;

@Service("accountBalanceInquiryService")
public class AccountBalanceInquiryService implements BankProcessingService{

	@Autowired
	UserSessionRepository userSessionRepository;
	
	@Autowired
	LoanLedgerService loanLedgerService;

	@Autowired
	AccountingEntryService accountingEntryService;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public void processTransaction(BankTransaction bankTransaction) {
		
		List<UserSession> userSessions = userSessionRepository.findAll();
		
		UserSession currentLoggedInUser = null; 
		if(userSessions != null && userSessions.size() > 0) {
			
			currentLoggedInUser = userSessions.get(userSessions.size() - 1);
			
		}
		
		List<BigDecimal> creditTransactions = jdbcTemplate.query(
				String.format("SELECT SUM(AMOUNT) FROM BANK_ACCOUNT_TRANSACTIONS WHERE ACCOUNT_USERNAME='%s' AND ACCOUNT_ENTRY_TYPE = 10", 
						currentLoggedInUser.getUserName()),
				(rs, row) -> rs.getBigDecimal(1));

		List<BigDecimal> debitTransactions = jdbcTemplate.query(
				String.format("SELECT SUM(AMOUNT) FROM BANK_ACCOUNT_TRANSACTIONS WHERE ACCOUNT_USERNAME='%s' AND ACCOUNT_ENTRY_TYPE = 11", 
						currentLoggedInUser.getUserName()),
				(rs, row) -> rs.getBigDecimal(1));

		DecimalFormat df = new DecimalFormat("#.#####");
		
		BigDecimal totalCredit = BigDecimal.ZERO;
		
		BigDecimal totalDebit = BigDecimal.ZERO;
		
		if(creditTransactions != null && creditTransactions.size() >0 && creditTransactions.get(0) != null) {
			totalCredit = creditTransactions.get(0);
		}
		
		if(debitTransactions != null && debitTransactions.size() >0 && debitTransactions.get(0) != null) {
			totalDebit = debitTransactions.get(0);
		}
		
		
		BigDecimal totalBalance = totalCredit.subtract(totalDebit);
		
		Double currentBalance = totalBalance.doubleValue();
		
		//if currentBalance is negative, check for loan
		String loanMessage = null;
		/**
		 * GET ENTIRE LOAN LEDGER
		 */
		String loanSQL = "SELECT CREDITOR_USERNAME, DEBITOR_USERNAME, AMOUNT FROM BANK_LOAN_LEDGER WHERE DEBITOR_USERNAME='%s'";
		
		List<LoanLedger> loanList = jdbcTemplate.query(String.format(loanSQL, currentLoggedInUser.getUserName()), 
				(rs,row)-> new LoanLedger(rs.getString(1),rs.getString(2),rs.getBigDecimal(3)));
		
		if(bankTransaction.getTranCode() / 1000 > 0) {

			/**
			 * If Trancode is an Account Entry
			 */
			if(currentBalance < 0) {
				/**
				 * If balance is negative, then current user has a pending payment to another account
				 */
				
				if(bankTransaction.getTranCode() % 1000 / 100 > 0 ) {
					/*
					 * If TRANSFER MODE IS 1 or Greater than 1, Add to Loan Ledger
					 */
					
					String formattedLoanValue = null;
					String recipientUsername = bankTransaction.getUsername();

					if(loanList != null && loanList.size() > 0) {
						/*
						 * If there is more than one loan, use absolute(tx amt) as loan amount
						 */
						BigDecimal txAmt = new BigDecimal( Math.abs( new Double(bankTransaction.getAmount())));
						
						BigDecimal transferAmt = new BigDecimal(bankTransaction.getAmount()).subtract(txAmt);
						
						formattedLoanValue = df.format(transferAmt.doubleValue());
						
						loanLedgerService.addToLoanLedger(
								new LoanLedger(
										bankTransaction.getUsername(),
										currentLoggedInUser.getUserName(),
										txAmt));
			
					}else {
						/*
						 * If first loan, use the absolute (negative bal) as loan amount
						 */
						BigDecimal txAmt = new BigDecimal( Math.abs( currentBalance));
						
						BigDecimal transferAmt = new BigDecimal(bankTransaction.getAmount()).subtract(txAmt);
						
						formattedLoanValue = df.format(transferAmt.doubleValue());
						
						loanLedgerService.addToLoanLedger(
								new LoanLedger(
										bankTransaction.getUsername(),
										currentLoggedInUser.getUserName(),
										txAmt));

					}
					

					
					if(formattedLoanValue != null && recipientUsername !=null) {
						
						System.out.println(String.format("Transferred %s to %s", formattedLoanValue,recipientUsername));						
					}
					
					
				}else {
					
					payLoanByTopup(bankTransaction);

				}				
				
			}else {
				/**
				 * If Balance is not negative, there is no pending loan payment for current user
				 * But need to check if a fund transfer can be used to wave payments from current debtors
				 */
				if(bankTransaction.getTranCode() % 1000 / 100 > 0 ) {
					/**
					 * If TX code is a Payment 
					 */
					BigDecimal txAmt = new BigDecimal(bankTransaction.getAmount());
					BigDecimal payAmt = BigDecimal.ZERO;
					
					String pendingPaymentSQL 
						= "SELECT CREDITOR_USERNAME, DEBITOR_USERNAME, SUM(AMOUNT)  FROM BANK_LOAN_LEDGER " + 
							" WHERE DEBITOR_USERNAME = '%s' AND CREDITOR_USERNAME ='%s'" + 
							" GROUP BY CREDITOR_USERNAME, DEBITOR_USERNAME ";
					List<LoanLedger> pendingPaymentList = jdbcTemplate.query(String.format(pendingPaymentSQL, bankTransaction.getUsername(),currentLoggedInUser.getUserName()),
							(rs,row)->new LoanLedger(rs.getString(1),rs.getString(2),rs.getBigDecimal(3)));
					
					String formattedLoanValue = df.format(txAmt.doubleValue());;
					String recipientUsername= bankTransaction.getUsername();
					
					if(pendingPaymentList != null && pendingPaymentList.size() > 0) {
						for(LoanLedger ledger : pendingPaymentList) {
							payAmt = ledger.getAmount().subtract(txAmt);
							
							if(payAmt.doubleValue() < 0) {
								BigDecimal loanPayAmt = txAmt.add(payAmt);
								
								formattedLoanValue = df.format(loanPayAmt.doubleValue());

								recipientUsername = ledger.getCreditorUsername();

								loanPayAmt = loanPayAmt.multiply(new BigDecimal(-1)); //NEGATE AMOUNT
								
								loanLedgerService.addToLoanLedger(
										new LoanLedger(
												currentLoggedInUser.getUserName(),
												ledger.getDebitorUsername(),
												loanPayAmt
												));
								
								txAmt = txAmt.add(loanPayAmt);
								
							}else {
								
								formattedLoanValue = df.format(txAmt.doubleValue());

								recipientUsername = ledger.getCreditorUsername();
								
								BigDecimal loanPayAmt = txAmt.multiply(new BigDecimal(-1)); //NEGATE AMOUNT
								loanLedgerService.addToLoanLedger(
										new LoanLedger(
												currentLoggedInUser.getUserName(),
												ledger.getDebitorUsername(),
												loanPayAmt
												));
							}
						}
					}
					
					if(formattedLoanValue != null && recipientUsername !=null) {
						
						System.out.println(String.format("Transferred %s to %s", formattedLoanValue,recipientUsername));						
					}

					
				}else{
					
					payLoanByTopup(bankTransaction);
					
				}
				
			}
		}
		
		String totalLoanSQL = "SELECT SUM(AMOUNT) FROM BANK_LOAN_LEDGER WHERE DEBITOR_USERNAME='%s'";
		
		List<BigDecimal> totalLoan 
			= jdbcTemplate
				.query(String.format(
						totalLoanSQL, 
						currentLoggedInUser.getUserName()), 
							(rs,row)-> rs.getBigDecimal(1));
//		System.out.println("BALANCE: "+totalBalance.doubleValue());

		if(totalLoan != null && totalLoan.size() > 0) {
			
			if(totalLoan.get(0) !=null) {
				
//				System.out.println("LOANS: "+totalLoan.get(0).doubleValue());
				
				totalBalance = new BigDecimal(Math.abs(totalBalance.doubleValue())).subtract(totalLoan.get(0));
				
				loanMessage = String.format("Owing %s to %s", df.format(totalLoan.get(0).doubleValue()),bankTransaction.getUsername());

			}
		}
		
		String totalPendingPaymentSQL= "SELECT SUM(AMOUNT) FROM BANK_LOAN_LEDGER WHERE CREDITOR_USERNAME='%s'";
		
		List<BigDecimal> totalPendingPayment 
		= jdbcTemplate
			.query(String.format(
					totalPendingPaymentSQL, 
					currentLoggedInUser.getUserName()), 
						(rs,row)-> rs.getBigDecimal(1));		
		

		if(totalPendingPayment != null && totalPendingPayment.size() > 0) {
			if(totalPendingPayment.get(0) != null) {
				totalBalance = new BigDecimal(Math.abs(totalBalance.doubleValue())).subtract(totalPendingPayment.get(0));
			}
		}
		
		
		/**
		 * If trancode is 0, which is the default code for login
		 * Print pending loan payments
		 */
		String pendingPaymentSQL = "SELECT CREDITOR_USERNAME,DEBITOR_USERNAME, SUM(AMOUNT) " + 
				" FROM BANK_LOAN_LEDGER " + 
				" WHERE CREDITOR_USERNAME = '%s' " + 
				" GROUP BY CREDITOR_USERNAME, DEBITOR_USERNAME ";
		
		List<LoanLedger> pendingPaymentList = jdbcTemplate.query(String.format(pendingPaymentSQL,currentLoggedInUser.getUserName()),
				(rs,row)-> new LoanLedger(rs.getString(1),rs.getString(2),rs.getBigDecimal(3))
				);
		
		if(pendingPaymentList != null && pendingPaymentList.size() > 0) {
			
			for(LoanLedger ledger: pendingPaymentList) {
				
				if(ledger.getAmount().doubleValue() > 0) {
					
					System.out.println(String.format("Owing %s from %s", df.format(ledger.getAmount().doubleValue()),ledger.getDebitorUsername())+".");					
				}
			}
		}
		
		
			
		String formattedValue = df.format(totalBalance.doubleValue());
		
		System.out.println(String.format("Your balance is %s", formattedValue)+".");
		
		String loanSumSQL = "SELECT CREDITOR_USERNAME,DEBITOR_USERNAME, SUM(AMOUNT) " + 
				" FROM BANK_LOAN_LEDGER " + 
				" WHERE DEBITOR_USERNAME = '%s' " + 
				" GROUP BY CREDITOR_USERNAME, DEBITOR_USERNAME ";
		List<LoanLedger> loanSumList = jdbcTemplate.query(String.format(loanSumSQL,currentLoggedInUser.getUserName()),
			(rs,row)-> new LoanLedger(rs.getString(1),rs.getString(2),rs.getBigDecimal(3))
			);
		if(loanSumList != null && loanSumList.size() > 0) {
			
			for(LoanLedger ledger: loanSumList) {

				if(ledger.getAmount().doubleValue() > 0) {
					
					System.out.println(String.format("Owing %s to %s", df.format(ledger.getAmount().doubleValue()),ledger.getCreditorUsername())+".");					
				}

			}
		}			

		

		
		System.out.println();
		
	}
	
	private void payLoanByTopup(BankTransaction bankTransaction) {
		
		List<UserSession> userSessions = userSessionRepository.findAll();
		
		UserSession currentLoggedInUser = null; 
		if(userSessions != null && userSessions.size() > 0) {
			
			currentLoggedInUser = userSessions.get(userSessions.size() - 1);
			
		}
		
		/*
		 * If TRANSFER MODE IS 0 (TOPUP), add to loan ledger with a negative amount
		 * THIS EFFECTIVELY TRANFERS AMOUNT
		 */
		DecimalFormat df = new DecimalFormat("#.#####");
		
		BigDecimal txAmt = new BigDecimal(bankTransaction.getAmount());
		
		BigDecimal payAmt = txAmt;


		String loanSumSQL = "SELECT CREDITOR_USERNAME,DEBITOR_USERNAME, SUM(AMOUNT) " + 
								" FROM BANK_LOAN_LEDGER " + 
								" WHERE DEBITOR_USERNAME = '%s' " + 
								" GROUP BY CREDITOR_USERNAME, DEBITOR_USERNAME ";
		List<LoanLedger> loanSumList = jdbcTemplate.query(String.format(loanSumSQL,currentLoggedInUser.getUserName()),
				(rs,row)-> new LoanLedger(rs.getString(1),rs.getString(2),rs.getBigDecimal(3))
				);
		
		for(LoanLedger ledger: loanSumList){
			
			payAmt = ledger.getAmount().subtract(txAmt);
			
			if(payAmt.doubleValue() < 0) {
				BigDecimal loanPayAmt = txAmt.add(payAmt);
				
				String formattedLoanValue = df.format(loanPayAmt.doubleValue());
				
				System.out.println(String.format("Transferred %s to %s", formattedLoanValue,ledger.getCreditorUsername()));
				
				loanPayAmt = loanPayAmt.multiply(new BigDecimal(-1)); //NEGATE AMOUNT
				
				loanLedgerService.addToLoanLedger(
						new LoanLedger(
								ledger.getCreditorUsername(),
								currentLoggedInUser.getUserName(),
								loanPayAmt
								));
				
				txAmt = txAmt.add(loanPayAmt);
				
			}else {
				
				String formattedLoanValue = df.format(txAmt.doubleValue());
				
				System.out.println(String.format("Transferred %s to %s", formattedLoanValue,ledger.getCreditorUsername()));
				
				BigDecimal loanPayAmt = txAmt.multiply(new BigDecimal(-1)); //NEGATE AMOUNT
				
				loanLedgerService.addToLoanLedger(
						new LoanLedger(
								ledger.getCreditorUsername(),
								currentLoggedInUser.getUserName(),
								loanPayAmt
								));
				
			}
			
		}
		
	}

}
