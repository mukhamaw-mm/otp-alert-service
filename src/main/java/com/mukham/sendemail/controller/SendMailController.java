package com.mukham.sendemail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import sendemail.sendemail.Repository.MailRepository;
import com.mukham.sendemail.service.RandomNoService;

import java.text.ParseException;

@RestController
public class SendMailController {
    @Autowired
    RandomNoService randomNoService;

    @GetMapping("/getUserEmail")
    public ResponseEntity getUserEmail(@RequestParam String email) {
        if (email == null || email.trim().equals("")) {
            return new ResponseEntity("email is null or empty", HttpStatus.BAD_REQUEST);
        }

        String status = randomNoService.getUserEmail(email);
        if (status.equals("success"))
            return new ResponseEntity("Successfully Sent!", HttpStatus.OK);
        else
            return new ResponseEntity("Fail", HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @GetMapping("/getOTPCode")
    public String getOTP(@RequestParam String otp) throws ParseException {
        return randomNoService.getOtp(otp);

    }


}
