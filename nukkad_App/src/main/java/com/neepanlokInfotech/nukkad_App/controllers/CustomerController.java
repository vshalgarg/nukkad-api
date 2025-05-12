package com.neepanlokInfotech.nukkad_App.controllers;

import com.neepanlokInfotech.nukkad_App.dto.CustomerRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.CustomerResponseDTO;
import com.neepanlokInfotech.nukkad_App.services.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.neepanlokInfotech.nukkad_App.constants.UrlConstants.*;

@RestController
@RequestMapping( CUSTOMER)
public class CustomerController {

    @Autowired
    private CustomerService customerService;
/*

*Create Customer Profile

 */
    @PostMapping(CREATE_CUSTOMER)
    public ResponseEntity<CustomerResponseDTO> saveCustomerData(@Valid @RequestBody CustomerRequestDTO customerRequestDTO){
        CustomerResponseDTO customerResponseDTO = customerService.saveCustomerData(customerRequestDTO);
        return new ResponseEntity<>(customerResponseDTO, HttpStatus.CREATED);
    }
    /*

     *Update Customer Profile

     */
    @PutMapping(UPDATE_CUSTOMER)
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@Valid @PathVariable Long id, @RequestBody CustomerRequestDTO customerRequestDTO) {

        CustomerResponseDTO updatedCustomer = customerService.updateCustomer(id, customerRequestDTO);
        return ResponseEntity.ok(updatedCustomer);
    }

}
