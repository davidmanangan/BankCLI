package iam.davidmanangan.bankcli.model;

public class BankTransaction {

	private Integer tranCode;
	
	private String amount;
	
	private String username;

	public BankTransaction() {
	}

	public BankTransaction(String username) {
		this.username = username;
	}
	
	public BankTransaction(String amount, String username) {
		this.amount = amount;
		this.username = username;
	}

	public BankTransaction(Integer tranCode) {
		this.tranCode = tranCode;
	}

	public BankTransaction(Integer tranCode, String amount) {
		this.tranCode = tranCode;
		this.amount = amount;
	}

	public BankTransaction(Integer tranCode, String amount, String username) {
		this.tranCode = tranCode;
		this.amount = amount;
		this.username = username;
	}

	public Integer getTranCode() {
		return tranCode;
	}

	public void setTranCode(Integer tranCode) {
		this.tranCode = tranCode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
