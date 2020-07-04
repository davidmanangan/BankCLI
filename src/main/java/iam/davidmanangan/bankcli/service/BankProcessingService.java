package iam.davidmanangan.bankcli.service;

import iam.davidmanangan.bankcli.model.BankTransaction;

public interface BankProcessingService {
	
	public void processTransaction(BankTransaction bankTransaction);

}
