package nl.rabobank.service;

import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.exception.InvalidAccountException;
import nl.rabobank.mongo.repository.PowerOfAttorneyRepository;
import nl.rabobank.mongo.repository.account.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static nl.rabobank.helper.AccountTestData.FREDDY_SAVINGS;
import static nl.rabobank.helper.AccountTestData.KIRILL_SAVINGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    void shouldGrantPermission(){
        when(accountRepositoryMock.findByAccountNumber("000000001")).thenReturn(Optional.of(KIRILL_SAVINGS));
        when(accountRepositoryMock.findByAccountNumber("000000003")).thenReturn(Optional.of(FREDDY_SAVINGS));

        powerOfAttorneyService.grantAccess("000000001","000000003", Authorization.READ);

        ArgumentCaptor<PowerOfAttorney> powerOfAttorneyArgumentCaptor = ArgumentCaptor.forClass(PowerOfAttorney.class);
        verify(powerOfAttorneyRepositoryMock).save(powerOfAttorneyArgumentCaptor.capture());

        PowerOfAttorney savedPowerOfAttorney = powerOfAttorneyArgumentCaptor.getValue();

        assertThat(savedPowerOfAttorney.getAuthorization()).isEqualTo(Authorization.READ);
        assertThat(savedPowerOfAttorney.getGranteeName()).isEqualTo("Freddy Kruger");
        assertThat(savedPowerOfAttorney.getGrantorName()).isEqualTo("Kirill Lassounski");
        assertThat(savedPowerOfAttorney.getAccount().getAccountNumber()).isEqualTo("000000001");
    }

    @Test
    void shouldThrowWhenAccountNotFound(){
        assertThatThrownBy(() -> powerOfAttorneyService.grantAccess("000000001","000000003", Authorization.READ))
            .isInstanceOf(InvalidAccountException.class);
    }
}
