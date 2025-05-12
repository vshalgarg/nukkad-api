package com.neepanlokInfotech.nukkad_App.services;

import com.neepanlokInfotech.nukkad_App.dto.SendOtpRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.SendOtpResponseDTO;
import com.neepanlokInfotech.nukkad_App.dto.VerifyRequestDTO;
import com.neepanlokInfotech.nukkad_App.dto.VerifyResponseDTO;
import com.neepanlokInfotech.nukkad_App.entities.OtpEntity;
import com.neepanlokInfotech.nukkad_App.mapper.OtpMapper;
import com.neepanlokInfotech.nukkad_App.repositories.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import static org.hibernate.annotations.UuidGenerator.Style.RANDOM;

@Service
public class OtpService {

    private static final Random RANDOM = new Random();
private static final  long Expiry= 5 * 60;
    @Autowired
    private OtpRepository otpRepository;



    public SendOtpResponseDTO sendOtp(SendOtpRequestDTO sendOtpRequestDTO){
        //call rest API
        // response me milega OTP SENT SUCCESS / FAILED
       //send to frontend
        String otpCode = String.valueOf(RANDOM.nextInt(900000)+100000);
        LocalDateTime createdAt = LocalDateTime.now();

        OtpEntity otpEntity = new OtpEntity();
        otpEntity.setMobileNumber(sendOtpRequestDTO.getMobileNumber());
        otpEntity.setOtp(otpCode);
        otpEntity.setCreatedAt(createdAt);
        otpEntity.setMessage("OTP sent successfully");

        otpRepository.save(otpEntity);

       return OtpMapper.otpResponseDTODto(otpEntity);

    }
    public VerifyResponseDTO verifyDTO(VerifyRequestDTO dto)
    {
        Optional<OtpEntity> optional= otpRepository.findTopByMobileNumberOrderByCreatedAtDesc(dto.getMobileNumber());
        if(optional.isEmpty()){
            return new VerifyResponseDTO("Fail","Mobile Number is not found");
        }
        OtpEntity otpStored= optional.get();
        LocalDateTime now= LocalDateTime.now();
        if(otpStored.getCreatedAt().plusSeconds(Expiry).isBefore(now))
        {
            return new VerifyResponseDTO("Fail","Otp is Expired");
        }
        if(!otpStored.getOtp().equals(dto.getOtp()))
        {
            return new VerifyResponseDTO("Fail","Invaild otp");
        }
        otpStored.setVerified(true);
        otpStored.setMessage("Otp Verified");
        otpRepository.save(otpStored);
        return new VerifyResponseDTO("Success","Otp verified successfully");
    }
}

