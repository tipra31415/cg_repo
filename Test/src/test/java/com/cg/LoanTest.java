package com.cg;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.cg.entity.Loan;
import com.cg.exception.*;
import com.cg.repository.LoanRepository;
import com.cg.service.LoanService;

@SpringBootTest
public class LoanTest {

	private Optional<Loan> optLoan1, optLoan2, optEmpty;

	@MockitoBean
	private LoanRepository repo;

	@Autowired
	private LoanService service;

	@BeforeEach
	public void beforeEach() {
		Loan loan1 = new Loan("Arpit", 100000, "PENDING");
		loan1.setId(1L);

		Loan loan2 = new Loan("Arpit", 200000, "APPROVED");
		loan2.setId(2L);

		optLoan1 = Optional.of(loan1);
		optLoan2 = Optional.of(loan2);
		optEmpty = Optional.empty();
	}

	@Test
	public void testCreateLoanSuccess() {

		Loan loan = new Loan("Arpit", 100000, null);

		Mockito.when(repo.findByApplicantNameAndStatus("Arpit", "PENDING")).thenReturn(optEmpty);

		Mockito.when(repo.save(Mockito.any(Loan.class))).thenReturn(new Loan("Arpit", 100000, "PENDING"));

		Loan result = service.createLoan(loan);

		assertNotNull(result);
		assertEquals("PENDING", result.getStatus());

		Mockito.verify(repo).save(Mockito.any(Loan.class));
	}

	@Test
	public void testCreateLoanInvalidAmount() {

		Loan loan = new Loan("Arpit", 0, null);

		assertThrows(InvalidLoanAmountException.class, () -> service.createLoan(loan));
	}

	@Test
	public void testCreateLoanDuplicate() {

		Loan loan = new Loan("Arpit", 100000, null);

		Mockito.when(repo.findByApplicantNameAndStatus("Arpit", "PENDING")).thenReturn(optLoan1);

		assertThrows(DuplicateLoanApplicationException.class, () -> service.createLoan(loan));
	}
	
	
	
}
