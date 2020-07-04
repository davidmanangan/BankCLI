package iam.davidmanangan.bankcli.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BANK_ACCOUNT_TRANSACTIONS")
public class AccountTransaction {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="TRANSACTION_ID")
	String transactionID;
	
	@Column(name="ACCOUNT_USERNAME")
	String accountUsername;
	
	@Column(name="TRANCODE")
	Integer transactionCode;
	
	@Column(name="ACCOUNT_ENTRY_TYPE")
	Integer accountEntryType;
	
	@Column(name="AMOUNT")
	BigDecimal amount;

	public AccountTransaction() {
	}

	public AccountTransaction(String accountUsername, Integer transactionCode, Integer accountEntryType,
			BigDecimal amount) {
		this.accountUsername = accountUsername;
		this.transactionCode = transactionCode;
		this.accountEntryType = accountEntryType;
		this.amount = amount;
	}

	public String getAccountUsername() {
		return accountUsername;
	}

	public void setAccountUsername(String accountUsername) {
		this.accountUsername = accountUsername;
	}

	public Integer getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(Integer transactionCode) {
		this.transactionCode = transactionCode;
	}

	public Integer getAccountEntryType() {
		return accountEntryType;
	}

	public void setAccountEntryType(Integer accountEntryType) {
		this.accountEntryType = accountEntryType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
