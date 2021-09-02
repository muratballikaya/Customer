package com.getir.rig.service;

import com.getir.rig.client.KeycloakClient;
import com.getir.rig.dto.CredentialDto;
import com.getir.rig.dto.CustomerDto;
import com.getir.rig.exception.LoginFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

   @Autowired
   KeycloakClient keycloakClient;

    public int save(CustomerDto customerDto) {
        return keycloakClient.createUserInKeyCloak(customerDto);
    }

    public String login(CredentialDto credentialDto)  {
            return keycloakClient.getToken(credentialDto.getUserName(), credentialDto.getPassword());
    }
}
