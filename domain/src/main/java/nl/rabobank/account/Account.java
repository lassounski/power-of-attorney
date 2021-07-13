package nl.rabobank.account;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    defaultImpl = PaymentAccount.class
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PaymentAccount.class, name = "paymentAccount"),
    @JsonSubTypes.Type(value = SavingsAccount.class, name = "savingsAccount")
})
public interface Account
{
    String getId();
    String getAccountNumber();
    String getAccountHolderName();
    Double getBalance();
    String getType();
}
