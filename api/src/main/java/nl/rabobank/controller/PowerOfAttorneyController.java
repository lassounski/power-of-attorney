package nl.rabobank.controller;

import lombok.RequiredArgsConstructor;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.model.GrantDTO;
import nl.rabobank.service.PowerOfAttorneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PowerOfAttorneyController {

    @Autowired
    private PowerOfAttorneyService powerOfAttorneyService;

    @PostMapping(path = "/grant", consumes = "application/json")
    public void grantAuthorization(@RequestBody GrantDTO grantDTO){
        powerOfAttorneyService.grantAccess(grantDTO.getGrantorAccountNumber(),
            grantDTO.getGranteeAccountNumber(),
            grantDTO.getAuthorization());
    }

    @GetMapping(path = "/grantedAccounts/{granteeName}", produces = "application/json")
    public List<PowerOfAttorney> getGrantedAccounts(@PathVariable String granteeName){
        return powerOfAttorneyService.findByGranteeName(granteeName);
    }
}
