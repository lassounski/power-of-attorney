package nl.rabobank.config;

import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.mongo.repository.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Configuration
@Profile("fakeData")
public class FakeDataConfiguration {

    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    public void loadData(){
        accountRepository.save(SavingsAccount.builder()
            .id("1")
            .accountHolderName("Kirill Lassounski")
            .accountNumber("000000001")
            .type("savingsAccount")
            .balance(2000.00)
            .build());

        accountRepository.save(PaymentAccount.builder()
            .id("2")
            .accountHolderName("Kirill Lassounski")
            .accountNumber("000000002")
            .type("paymentsAccount")
            .balance(5000.00)
            .build());

        accountRepository.save(SavingsAccount.builder()
            .id("3")
            .accountHolderName("Freddy Kruger")
            .accountNumber("000000003")
            .type("savingsAccount")
            .balance(1000.00)
            .build());
    }
}
