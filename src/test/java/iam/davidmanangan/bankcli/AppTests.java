package iam.davidmanangan.bankcli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import iam.davidmanangan.bankcli.commands.LoginCommand;
import iam.davidmanangan.bankcli.commands.PayCommand;
import iam.davidmanangan.bankcli.commands.TopUpCommand;
import iam.davidmanangan.bankcli.model.BankTransaction;
import iam.davidmanangan.bankcli.model.UserSession;
import iam.davidmanangan.bankcli.repository.AccountTransactionRepository;
import iam.davidmanangan.bankcli.repository.LoanLedgerRepository;
import iam.davidmanangan.bankcli.repository.UserRepository;
import iam.davidmanangan.bankcli.repository.UserSessionRepository;
import iam.davidmanangan.bankcli.service.impl.AccountBalanceInquiryService;
import iam.davidmanangan.bankcli.service.impl.AccountingEntryService;
import iam.davidmanangan.bankcli.service.impl.LoanLedgerService;
import iam.davidmanangan.bankcli.service.impl.UserSessionService;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { 
		SpringShellConfig.class, 
		AccountBalanceInquiryService.class,
		AccountingEntryService.class, 
		LoanLedgerService.class, 
		UserSessionService.class,
		AccountTransactionRepository.class,
		LoanLedgerRepository.class,
		UserRepository.class,
		UserSessionRepository.class,
		LoginCommand.class,
		PayCommand.class,
		TopUpCommand.class
		})
@EnableAutoConfiguration
@TestExecutionListeners(value = { MockitoTestExecutionListener.class,DependencyInjectionTestExecutionListener.class })
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/schema-h2.sql")
@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/data-h2.sql")
@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application.properties")
public class AppTests {

	@Autowired
	LoginCommand loginCommand;
	
	@Autowired
	TopUpCommand topUpCommand;
	
	@Autowired
	PayCommand payCommand;
	
	@MockBean
	UserSessionService userSessionService;

	
	@Autowired
	AccountBalanceInquiryService accountBalanceInquiryService;
	
	@Autowired
	AccountingEntryService accountingEntryService;
	
	@Test
	public void testCases() {
		/**
		 * Execute Test cases sequencially
		 */
		test1_Alice_Login();
		test2_Alice_Topup_100();
		test3_Bob_Login();
		test4_Bob_Topup_80();
		test5_Bob_Pay_Alice_50();
		test6_Bob_Pay_Alice_100();
		test7_Bob_Topup_30();
		test8_Alice_2nd_Login();
		test9_Alice_Pay_Bob_30();
		test10_Bob_2nd_Login();
		test11_Bob_Topup_100();
	}
	
	public void test1_Alice_Login() {
		
		when(userSessionService.getCurrentUser())
			.thenReturn(new UserSession("alice", new Date()));
		
		loginCommand.login("alice");
		
		Double accountBal = accountBalanceInquiryService.processTransaction(new BankTransaction(0,"","alice"));
		
		assertEquals(0d, accountBal);
	}
	
	
	public void test2_Alice_Topup_100() {
		
		when(userSessionService.getCurrentUser())
			.thenReturn(new UserSession("alice", new Date()));
		
		topUpCommand.topup("100");
		
		Double accountBal = accountBalanceInquiryService.processTransaction(new BankTransaction(0,"","alice"));
		
		assertEquals(100d, accountBal);
	}

	
	public void test3_Bob_Login() {

		when(userSessionService.getCurrentUser())
			.thenReturn(new UserSession("bob", new Date()));
		
		loginCommand.login("bob");

		Double accountBal = accountBalanceInquiryService.processTransaction(new BankTransaction(0,"","bob"));
		
		assertEquals(0d, accountBal);
	}
	
	
	public void test4_Bob_Topup_80() {
		when(userSessionService.getCurrentUser())
			.thenReturn(new UserSession("bob", new Date()));
		
		topUpCommand.topup("80");

		Double accountBal = accountBalanceInquiryService.processTransaction(new BankTransaction(0,"","bob"));
		
		assertEquals(80d, accountBal);
	}
	
	
	public void test5_Bob_Pay_Alice_50() {
		when(userSessionService.getCurrentUser())
			.thenReturn(new UserSession("bob", new Date()));
		
		payCommand.pay("alice", "50");

		Double accountBal = accountBalanceInquiryService.processTransaction(new BankTransaction(0,"","bob"));
		
		assertEquals(30d, accountBal);
	}

	
	public void test6_Bob_Pay_Alice_100() {

		when(userSessionService.getCurrentUser())
			.thenReturn(new UserSession("bob", new Date()));

		payCommand.pay("alice", "100");

		Double accountBal = accountBalanceInquiryService.processTransaction(new BankTransaction(0,"","bob"));
		
		assertEquals(0d, accountBal);
	}

	
	public void test7_Bob_Topup_30() {

		when(userSessionService.getCurrentUser())
			.thenReturn(new UserSession("bob", new Date()));
		
		topUpCommand.topup("30");
		
		Double accountBal = accountBalanceInquiryService.processTransaction(new BankTransaction(0,"","bob"));
		
		assertEquals(0d, accountBal);
	}
	
	
	public void test8_Alice_2nd_Login() {
		when(userSessionService.getCurrentUser())
			.thenReturn(new UserSession("alice", new Date()));
		
		loginCommand.login("alice");
		
		Double accountBal = accountBalanceInquiryService.processTransaction(new BankTransaction(0,"","alice"));
		
		assertEquals(210d, accountBal);
	}
	
	
	public void test9_Alice_Pay_Bob_30() {

		when(userSessionService.getCurrentUser())
			.thenReturn(new UserSession("alice", new Date()));

		payCommand.pay("bob", "30");
		
		Double accountBal = accountBalanceInquiryService.processTransaction(new BankTransaction(0,"","alice"));
		
		assertEquals(210d, accountBal);
	}
	
	
	public void test10_Bob_2nd_Login() {
		when(userSessionService.getCurrentUser())
			.thenReturn(new UserSession("bob", new Date()));

		loginCommand.login("bob");
		
		Double accountBal = accountBalanceInquiryService.processTransaction(new BankTransaction(0,"","bob"));
		
		assertEquals(0d, accountBal);
	}
	
	
	public void test11_Bob_Topup_100() {

		when(userSessionService.getCurrentUser())
			.thenReturn(new UserSession("bob", new Date()));
		
		topUpCommand.topup("100");
		
		Double accountBal = accountBalanceInquiryService.processTransaction(new BankTransaction(0,"","bob"));
		
		assertEquals(90d, accountBal);
	}	
}
