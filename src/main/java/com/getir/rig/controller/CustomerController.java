package com.getir.rig.controller;


import com.getir.rig.dto.CredentialDto;
import com.getir.rig.dto.CustomerDto;
import com.getir.rig.model.Customer;
import com.getir.rig.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/customer")
@Api(value = "Customer Api documentation")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping("/create")
    @ApiOperation(value = "New customer")
    public ResponseEntity<Integer> saveCustomer(@RequestBody @Validated CustomerDto customerDto){
        return new ResponseEntity<Integer>(customerService.save(customerDto), HttpStatus.CREATED) ;
    }

    @PostMapping("/login")
    @ApiOperation(value = "Log customer in")
    public ResponseEntity<String> login(@RequestBody @Validated CredentialDto credentialDto){
        return new ResponseEntity<String>(customerService.login(credentialDto), HttpStatus.CREATED) ;
    }

    private Customer convertToEntity(CustomerDto customerDto) {
        return new Customer(customerDto.getId(),customerDto.getUserName(),customerDto.getFirstName(),customerDto.getLastname(), customerDto.getEmail(),customerDto.getPassword());
    }
}
