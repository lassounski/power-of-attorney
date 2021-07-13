package nl.rabobank.service;

import lombok.RequiredArgsConstructor;
import nl.rabobank.account.Account;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.exception.AccountAlreadyGrantedException;
import nl.rabobank.exception.AccountNotExistentException;
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
        Account granteeAccount = accountRepository.findByAccountNumber(granteeAccountNumber)
            .orElseThrow(() -> new AccountNotExistentException(granteeAccountNumber));
        Account grantorAccount = accountRepository.findByAccountNumber(grantorAccountNumber)
            .orElseThrow(() -> new AccountNotExistentException(grantorAccountNumber));

        validateAccounts(granteeAccount, grantorAccount);

        List<PowerOfAttorney> grants = powerOfAttorneyRepository
            .findAllByGranteeNameAndGrantorName(granteeAccount.getAccountHolderName(), grantorAccount
                .getAccountHolderName());
        boolean alreadyGranted = grants.stream()
            .anyMatch(powerOfAttorney -> {
                if (powerOfAttorney.getGranteeName().equals(granteeAccount.getAccountHolderName()) &&
                    powerOfAttorney.getGrantorName().equals(grantorAccount.getAccountHolderName()) &&
                    powerOfAttorney.getAccount().getAccountNumber().equals(grantorAccountNumber)
                ) {
                    return true;
                }
                return false;
            });
        if(alreadyGranted){
            throw new AccountAlreadyGrantedException(
                grantorAccount.getAccountHolderName(),
                grantorAccountNumber,
                granteeAccount.getAccountHolderName());
        }

        PowerOfAttorney powerOfAttorney = PowerOfAttorney.builder()
            .granteeName(granteeAccount.getAccountHolderName())
            .grantorName(grantorAccount.getAccountHolderName())
            .authorization(authorization)
            .account(grantorAccount)
            .build();

        powerOfAttorneyRepository.save(powerOfAttorney);
    }

    private void validateAccounts(Account granteeAccount, Account grantorAccount) {
        if (granteeAccount.getAccountHolderName().equals(grantorAccount.getAccountHolderName())) {
            throw new InvalidAccountException(
                String
                    .format("granteeAccount holder [%s] should be different from grantorAccount holder", granteeAccount
                        .getAccountHolderName()));
        }
    }

    public List<PowerOfAttorney> findByGranteeName(String granteeName) {
        return powerOfAttorneyRepository.findAllByGranteeName(granteeName);
    }
}
