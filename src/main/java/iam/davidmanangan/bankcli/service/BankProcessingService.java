package iam.davidmanangan.bankcli.service;

import iam.davidmanangan.bankcli.model.BankTransaction;

public interface BankProcessingService {
	
	public Double processTransaction(BankTransaction bankTransaction);

}
