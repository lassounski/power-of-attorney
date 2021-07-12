package nl.rabobank.mongo.repository.account;

import nl.rabobank.account.Account;
import nl.rabobank.account.SavingsAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SavingsAccountRepository extends MongoRepository<SavingsAccount, String> {
    Account findByAccountNumber(String accountNumber);
}
