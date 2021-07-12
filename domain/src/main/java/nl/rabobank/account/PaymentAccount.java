package nl.rabobank.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PaymentAccount implements Account
{
    private String accountNumber;
    private String accountHolderName;
    private Double balance;
    private String type;

    @JsonCreator
    public PaymentAccount(@JsonProperty("accountNumber") String accountNumber,
                          @JsonProperty("accountHolderName") String accountHolderName,
                          @JsonProperty("balance") Double balance,
                          @JsonProperty("type") String type) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.type = type;
    }
}
