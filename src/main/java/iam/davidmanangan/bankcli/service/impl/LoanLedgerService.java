package iam.davidmanangan.bankcli.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import iam.davidmanangan.bankcli.model.LoanLedger;
import iam.davidmanangan.bankcli.repository.LoanLedgerRepository;

@Service("loanLedgerService")
@Transactional
public class LoanLedgerService {

	@Autowired
	LoanLedgerRepository loanLedgerRepository;
	
	public void addToLoanLedger(LoanLedger ledger) {
		loanLedgerRepository.saveAndFlush(ledger);
	}
	
}
