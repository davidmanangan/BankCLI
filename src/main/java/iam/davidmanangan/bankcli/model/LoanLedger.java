package iam.davidmanangan.bankcli.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BANK_LOAN_LEDGER")
public class LoanLedger {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="LOAN_ID")
	private Integer loanId;
	
	@Column(name="CREDITOR_USERNAME")
	String creditorUsername;
	
	@Column(name="DEBITOR_USERNAME")
	String debitorUsername;
	
	@Column(name="AMOUNT")
	BigDecimal amount;

	public LoanLedger() {
	}

	public LoanLedger(String creditorUsername, String debitorUsername, BigDecimal amount) {
		this.creditorUsername = creditorUsername;
		this.debitorUsername = debitorUsername;
		this.amount = amount;
	}

	public Integer getLoanId() {
		return loanId;
	}

	public void setLoanId(Integer loanId) {
		this.loanId = loanId;
	}

	public String getCreditorUsername() {
		return creditorUsername;
	}

	public void setCreditorUsername(String creditorUsername) {
		this.creditorUsername = creditorUsername;
	}

	public String getDebitorUsername() {
		return debitorUsername;
	}

	public void setDebitorUsername(String debitorUsername) {
		this.debitorUsername = debitorUsername;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
}
