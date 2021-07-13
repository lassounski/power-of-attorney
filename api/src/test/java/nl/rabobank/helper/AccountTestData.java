package nl.rabobank.helper;

import nl.rabobank.account.Account;
import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;

public class AccountTestData {

    public static Account KIRILL_SAVINGS = SavingsAccount.builder()
        .id("1")
        .accountHolderName("Kirill Lassounski")
        .accountNumber("000000001")
        .type("savingsAccount")
        .balance(2000.00)
        .build();

    public static Account KIRILL_PAYMENTS = PaymentAccount.builder()
        .id("2")
        .accountHolderName("Kirill Lassounski")
        .accountNumber("000000002")
        .type("paymentsAccount")
        .balance(5000.00)
        .build();

    public static Account FREDDY_SAVINGS = SavingsAccount.builder()
        .id("3")
        .accountHolderName("Freddy Kruger")
        .accountNumber("000000003")
        .type("savingsAccount")
        .balance(1000.00)
        .build();
}
