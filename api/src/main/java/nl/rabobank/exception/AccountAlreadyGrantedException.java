package nl.rabobank.exception;

import lombok.Value;

@Value
public class AccountAlreadyGrantedException extends RuntimeException {
    private String grantorName;
    private String grantorAccountNumber;
    private String granteeName;
}
