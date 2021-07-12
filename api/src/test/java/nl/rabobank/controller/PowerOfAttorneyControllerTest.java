package nl.rabobank.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.helper.AccountTestData;
import nl.rabobank.model.GrantDTO;
import nl.rabobank.mongo.repository.PowerOfAttorneyRepository;
import nl.rabobank.mongo.repository.account.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PowerOfAttorneyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PowerOfAttorneyRepository powerOfAttorneyRepository;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setUp(){
        accountRepository.deleteAll();
        accountRepository.save(AccountTestData.KIRILL_PAYMENTS);
        accountRepository.save(AccountTestData.KIRILL_SAVINGS);
        accountRepository.save(AccountTestData.FREDDY_SAVINGS);

        powerOfAttorneyRepository.deleteAll();
    }

    @Test
    public void shouldGrantAccess() throws Exception {
        GrantDTO grantDTO = GrantDTO.builder()
            .granteeAccountNumber("000000003")
            .grantorAccountNumber("000000002")
            .authorization(Authorization.READ)
            .build();

        this.mockMvc.perform(
            post("/grant")
                .content(objectMapper.writeValueAsString(grantDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        assertThat(powerOfAttorneyRepository.findAll()).hasSize(1);
    }

    @Test
    void shouldReturn404WhenAccountNumberNotPresent() throws Exception {
        GrantDTO grantDTO = GrantDTO.builder()
            .granteeAccountNumber("000000003")
            .grantorAccountNumber("000000007")
            .authorization(Authorization.READ)
            .build();

        this.mockMvc.perform(
            post("/grant")
                .content(objectMapper.writeValueAsString(grantDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound())
            .andExpect(jsonPath("$.timestamp", notNullValue()))
            .andExpect(jsonPath("$.message", equalTo("Provided account [000000007] does not exist")));
    }

    @Test
    void shouldValidateGrantDTOInput() throws Exception{
        GrantDTO grantDTO = GrantDTO.builder()
            .granteeAccountNumber("000000003")
            .grantorAccountNumber(null)
            .authorization(null)
            .build();

        this.mockMvc.perform(
            post("/grant")
                .content(objectMapper.writeValueAsString(grantDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.timestamp", notNullValue()))
            .andExpect(jsonPath("$.errors", hasItem("authorization should be provided")))
            .andExpect(jsonPath("$.errors", hasItem("grantorAccountNumber should not be empty")))
            .andExpect(jsonPath("$.errors", hasSize(2)));
    }

    @Test
    void shouldRetrieveGrantedAccounts() throws Exception {
        GrantDTO grantDTO = GrantDTO.builder()
            .granteeAccountNumber("000000003")
            .grantorAccountNumber("000000002")
            .authorization(Authorization.READ)
            .build();

        this.mockMvc.perform(
            post("/grant")
                .content(objectMapper.writeValueAsString(grantDTO))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        String json = this.mockMvc.perform(MockMvcRequestBuilders.get("/grantedAccounts/Freddy Kruger"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        List<PowerOfAttorney> grantedAccounts = objectMapper
            .readValue(json, new TypeReference<List<PowerOfAttorney>>() {
            });

        assertThat(grantedAccounts).hasSize(1);
    }

    @Test
    void shouldReturnEmptyListWhenGranteeNotPresent() throws Exception{
        String json = this.mockMvc.perform(MockMvcRequestBuilders.get("/grantedAccounts/Frank Sinatra"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        List<PowerOfAttorney> grantedAccounts = objectMapper
            .readValue(json, new TypeReference<List<PowerOfAttorney>>() {
            });

        assertThat(grantedAccounts).isEmpty();
    }

}