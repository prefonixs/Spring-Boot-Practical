package com.sid.validation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sid.validation.service.TwilioSmsService;

@RestController
@RequestMapping("/sms")
public class SmsController {

	@Autowired
    private TwilioSmsService smsService;

    @PostMapping("/send")
    public String sendSms(@RequestParam String to, @RequestParam String message) {
    	System.out.println("SMS");
    	System.out.println(to);
    	System.out.println(message);
        smsService.sendSms("+"+to, message);
        return "SMS sent successfully!";
    }
}
