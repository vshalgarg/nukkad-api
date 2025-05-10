package com.neepanlokInfotech.nukkad_App.controllers;

import com.neepanlokInfotech.nukkad_App.dto.SendOtpRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.SendOtpResponseDTO;
import com.neepanlokInfotech.nukkad_App.services.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;


    // to sent otp
    @PostMapping("/send")
    public ResponseEntity<SendOtpResponseDTO> sendOtp(@RequestBody SendOtpRequestDTO sendOtpRequestDTO){
       SendOtpResponseDTO sendOtpResponseDTO  = otpService.sendOtp(sendOtpRequestDTO);
       return ResponseEntity.ok(sendOtpResponseDTO);
    }

}
