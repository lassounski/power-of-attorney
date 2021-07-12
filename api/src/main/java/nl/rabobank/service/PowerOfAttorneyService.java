package nl.rabobank.service;

import lombok.RequiredArgsConstructor;
import nl.rabobank.account.Account;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.exception.InvalidAccountException;
import nl.rabobank.mongo.repository.PowerOfAttorneyRepository;
import nl.rabobank.mongo.repository.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PowerOfAttorneyService {

    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final PowerOfAttorneyRepository powerOfAttorneyRepository;

    public void grantAccess(String grantorAccountNumber, String granteeAccountNumber, Authorization authorization) {
        Account granteeAccount = accountRepository.findByAccountNumber(granteeAccountNumber).orElseThrow(() -> new InvalidAccountException(granteeAccountNumber));
        Account grantorAccount = accountRepository.findByAccountNumber(grantorAccountNumber).orElseThrow(() -> new InvalidAccountException(grantorAccountNumber));

        PowerOfAttorney powerOfAttorney = PowerOfAttorney.builder()
            .granteeName(granteeAccount.getAccountHolderName())
            .grantorName(grantorAccount.getAccountHolderName())
            .authorization(authorization)
            .account(grantorAccount)
            .build();

        powerOfAttorneyRepository.save(powerOfAttorney);
    }

    public List<PowerOfAttorney> findByGranteeName(String granteeName) {
        return powerOfAttorneyRepository.findAllByGranteeName(granteeName);
    }
}
