package nl.rabobank.account;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;

@Value
@Builder(toBuilder = true)
public class PaymentAccount implements Account
{
    @Id
    private String id;
    private String accountNumber;
    private String accountHolderName;
    private Double balance;
    private String type;

    @JsonCreator
    public PaymentAccount(@JsonProperty("id") String id,
                          @JsonProperty("accountNumber") String accountNumber,
                          @JsonProperty("accountHolderName") String accountHolderName,
                          @JsonProperty("balance") Double balance,
                          @JsonProperty("type") String type) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.type = type;
    }
}
