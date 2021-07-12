package nl.rabobank.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import nl.rabobank.authorizations.Authorization;

@Value
@Builder
public class GrantDTO {

    private String granteeAccountNumber;
    private String grantorAccountNumber;
    private Authorization authorization;

    @JsonCreator
    GrantDTO(@JsonProperty("granteeAccountNumber") String granteeAccountNumber,
             @JsonProperty("grantorAccountNumber") String grantorAccountNumber,
             @JsonProperty("authorization") Authorization authorization){
        this.granteeAccountNumber = granteeAccountNumber;
        this.grantorAccountNumber = grantorAccountNumber;
        this.authorization = authorization;
    }
}
