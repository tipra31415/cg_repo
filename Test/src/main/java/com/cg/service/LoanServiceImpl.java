package com.cg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.Loan;
import com.cg.exception.*;
import com.cg.repository.LoanRepository;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository repo;

    @Override
    public Loan createLoan(Loan loan) {

        if (loan.getLoanAmount() <= 0 || loan.getLoanAmount() > 5000000) {
            throw new InvalidLoanAmountException(
                "Loan amount must be between 1 and 5000000"
            );
        }

        repo.findByApplicantNameAndStatus(loan.getApplicantName(), "PENDING").ifPresent(l -> {
        	throw new DuplicateLoanApplicationException("User already has a PENDING loan");
            });

        loan.setStatus("PENDING");
        return repo.save(loan);
    }

    @Override
    public List<Loan> getAllLoans() {
        return repo.findAll();
    }

    @Override
    public Loan getLoanById(Long id) {

        Optional<Loan> opt = repo.findById(id);

        if (opt.isPresent()) {
            return opt.get();
        } else {
            throw new LoanNotFoundException("Loan not found with id: " + id);
        }
    }
    
    
    @Override
    public Loan updateLoanStatus(Long id, String status) {

        if (!status.equalsIgnoreCase("APPROVED") &&
            !status.equalsIgnoreCase("REJECTED")) {
            throw new InvalidLoanAmountException("Status must be APPROVED or REJECTED");
        }

        Loan loan = getLoanById(id);
        loan.setStatus(status.toUpperCase());
        return repo.save(loan);
    }
}