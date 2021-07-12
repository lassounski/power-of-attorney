package nl.rabobank.mongo.repository.account;

import nl.rabobank.account.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountRepository extends MongoRepository<Account, String> {
    Optional<Account> findByAccountNumber(String accountNumber);
}
