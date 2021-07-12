package nl.rabobank.exception;

import lombok.Value;

@Value
public class AccountNotExistentException extends RuntimeException {
    private String accountNumber;
}
