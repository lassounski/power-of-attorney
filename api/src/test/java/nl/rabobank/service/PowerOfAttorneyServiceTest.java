package nl.rabobank.service;

import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.exception.AccountAlreadyGrantedException;
import nl.rabobank.exception.AccountNotExistentException;
import nl.rabobank.exception.InvalidAccountException;
import nl.rabobank.mongo.repository.PowerOfAttorneyRepository;
import nl.rabobank.mongo.repository.account.AccountRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static nl.rabobank.helper.AccountTestData.FREDDY_SAVINGS;
import static nl.rabobank.helper.AccountTestData.KIRILL_PAYMENTS;
import static nl.rabobank.helper.AccountTestData.KIRILL_SAVINGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PowerOfAttorneyServiceTest {

    @Mock
    private AccountRepository accountRepositoryMock;
    @Mock
    private PowerOfAttorneyRepository powerOfAttorneyRepositoryMock;
    @InjectMocks
    private PowerOfAttorneyService powerOfAttorneyService;

    @Test
    void shouldGrantPermission() {
        when(accountRepositoryMock.findByAccountNumber("000000001")).thenReturn(Optional.of(KIRILL_SAVINGS));
        when(accountRepositoryMock.findByAccountNumber("000000003")).thenReturn(Optional.of(FREDDY_SAVINGS));

        powerOfAttorneyService.grantAccess("000000001", "000000003", Authorization.READ);

        ArgumentCaptor<PowerOfAttorney> powerOfAttorneyArgumentCaptor = ArgumentCaptor.forClass(PowerOfAttorney.class);
        verify(powerOfAttorneyRepositoryMock).save(powerOfAttorneyArgumentCaptor.capture());

        PowerOfAttorney savedPowerOfAttorney = powerOfAttorneyArgumentCaptor.getValue();

        assertThat(savedPowerOfAttorney.getAuthorization()).isEqualTo(Authorization.READ);
        assertThat(savedPowerOfAttorney.getGranteeName()).isEqualTo("Freddy Kruger");
        assertThat(savedPowerOfAttorney.getGrantorName()).isEqualTo("Kirill Lassounski");
        assertThat(savedPowerOfAttorney.getAccount().getAccountNumber()).isEqualTo("000000001");
    }

    @Test
    void shouldThrowWhenAccountNotFound() {
        assertThatThrownBy(() -> powerOfAttorneyService.grantAccess("000000001", "000000003", Authorization.READ))
            .isInstanceOf(AccountNotExistentException.class);
    }

    @Test
    void shouldNotAllowSameGranteeAndGrantor() {
        when(accountRepositoryMock.findByAccountNumber("000000001")).thenReturn(Optional.of(KIRILL_SAVINGS));
        when(accountRepositoryMock.findByAccountNumber("000000002")).thenReturn(Optional.of(KIRILL_PAYMENTS));

        assertThatThrownBy(() -> powerOfAttorneyService.grantAccess("000000001", "000000002", Authorization.READ))
            .isInstanceOf(InvalidAccountException.class)
            .hasMessage("granteeAccount holder [Kirill Lassounski] should be different from grantorAccount holder");
    }

    @Test
    void shouldNotAllowGranteeToGrantSameAccountToSameGrantorTwice() {
        when(accountRepositoryMock.findByAccountNumber("000000001")).thenReturn(Optional.of(KIRILL_SAVINGS));
        when(accountRepositoryMock.findByAccountNumber("000000003")).thenReturn(Optional.of(FREDDY_SAVINGS));

        powerOfAttorneyService.grantAccess("000000001", "000000003", Authorization.READ);

        ArgumentCaptor<PowerOfAttorney> powerOfAttorneyArgumentCaptor = ArgumentCaptor.forClass(PowerOfAttorney.class);
        verify(powerOfAttorneyRepositoryMock).save(powerOfAttorneyArgumentCaptor.capture());

        PowerOfAttorney savedPowerOfAttorney = powerOfAttorneyArgumentCaptor.getValue();

        when(powerOfAttorneyRepositoryMock.findAllByGranteeNameAndGrantorName(eq("Freddy Kruger"), eq("Kirill Lassounski")))
            .thenReturn(Lists.newArrayList(savedPowerOfAttorney));

        assertThatThrownBy(() -> powerOfAttorneyService.grantAccess("000000001", "000000003", Authorization.READ))
            .isInstanceOf(AccountAlreadyGrantedException.class)
            .hasFieldOrPropertyWithValue("grantorName","Kirill Lassounski")
            .hasFieldOrPropertyWithValue("grantorAccountNumber","000000001")
            .hasFieldOrPropertyWithValue("granteeName","Freddy Kruger");
//            .hasMessage("grantor [Kirill Lassounski] already granted account [000000001] to [Freddy Kruger]");
    }
}
