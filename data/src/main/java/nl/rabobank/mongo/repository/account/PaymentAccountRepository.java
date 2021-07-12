package nl.rabobank.mongo.repository.account;

import nl.rabobank.account.Account;
import nl.rabobank.account.PaymentAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentAccountRepository extends MongoRepository<PaymentAccount, String> {
    Account findByAccountNumber(String accountNumber);
}
