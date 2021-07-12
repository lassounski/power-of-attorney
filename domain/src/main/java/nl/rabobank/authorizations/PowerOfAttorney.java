package nl.rabobank.authorizations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import nl.rabobank.account.Account;

@Value
@Builder(toBuilder = true)
public class PowerOfAttorney
{
    private String granteeName;
    private String grantorName;
    private Account account;
    private Authorization authorization;

    @JsonCreator
    public PowerOfAttorney(@JsonProperty("granteeName") String granteeName,
                           @JsonProperty("grantorName") String grantorName,
                           @JsonProperty("account") Account account,
                           @JsonProperty("authorization") Authorization authorization) {
        this.granteeName = granteeName;
        this.grantorName = grantorName;
        this.account = account;
        this.authorization = authorization;
    }
}
