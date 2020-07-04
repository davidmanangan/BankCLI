package iam.davidmanangan.bankcli;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JndiConnectionFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import iam.davidmanangan.bankcli.commands.LoginCommand;
import iam.davidmanangan.bankcli.model.BankTransaction;
import iam.davidmanangan.bankcli.repository.AccountTransactionRepository;
import iam.davidmanangan.bankcli.repository.LoanLedgerRepository;
import iam.davidmanangan.bankcli.repository.UserRepository;
import iam.davidmanangan.bankcli.repository.UserSessionRepository;
import iam.davidmanangan.bankcli.service.impl.AccountBalanceInquiryService;
import iam.davidmanangan.bankcli.service.impl.AccountingEntryService;
import iam.davidmanangan.bankcli.service.impl.LoanLedgerService;
import iam.davidmanangan.bankcli.service.impl.UserSessionService;

@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = { 
//		SpringShellConfig.class, 
//		AccountBalanceInquiryService.class,
//		AccountingEntryService.class, 
//		LoanLedgerService.class, 
//		UserSessionService.class,
//		AccountTransactionRepository.class,
//		LoanLedgerRepository.class,
//		UserRepository.class,
//		UserSessionRepository.class
//		})
@EnableAutoConfiguration(exclude = { 
		JndiConnectionFactoryAutoConfiguration.class, 
		DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class, 
		JpaRepositoriesAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class })
@TestExecutionListeners(value = { DependencyInjectionTestExecutionListener.class })
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = { "classpath:/schema-h2.sql",
		"classpath:/data-h2.sql" })
public class AppTests {

//	@Autowired
//	AccountBalanceInquiryService accountBalanceInquiryService;
//
//	@Autowired
//	UserSessionService userSessionService;

	@Test
	public void login() {

//		String username = "alice";
//
//		String loginUser = username.toLowerCase();
//
//		userSessionService.logUserSession(username);
//
//		System.out.println(String.format("Hi, %s", username));
//
//		accountBalanceInquiryService.processTransaction(new BankTransaction(0, "", loginUser));

	}

}
