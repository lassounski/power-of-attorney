package nl.rabobank.exception;

import lombok.Value;

@Value
public class InvalidAccountException extends RuntimeException {
    private String accountNumber;
}
