package nl.rabobank.mongo.repository.account;

import nl.rabobank.account.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> {
    Account findByAccountNumber(String accountNumber);
}
