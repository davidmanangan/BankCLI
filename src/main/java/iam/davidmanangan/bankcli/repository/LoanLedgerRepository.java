package iam.davidmanangan.bankcli.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import iam.davidmanangan.bankcli.model.LoanLedger;

@Repository 
public interface LoanLedgerRepository extends JpaRepository<LoanLedger,Integer>{ }
