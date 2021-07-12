package nl.rabobank.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import nl.rabobank.authorizations.Authorization;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class GrantDTO {

    @NotEmpty(message = "granteeAccountNumber should not be empty")
    private String granteeAccountNumber;
    @NotEmpty(message = "grantorAccountNumber should not be empty")
    private String grantorAccountNumber;
    @NotNull(message = "authorization should be provided")
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
