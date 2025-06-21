package com.michael.fu.hsf301assigment1.controller.authentication;

import com.michael.fu.hsf301assigment1.controller.BaseController;
import com.michael.fu.hsf301assigment1.entity.Customer;
import com.michael.fu.hsf301assigment1.service.impl.CustomerService;
import com.michael.fu.hsf301assigment1.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerificationController extends BaseController {
    private final CustomerService customerService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public VerificationController(CustomerService customerService, JwtTokenUtil jwtTokenUtil) {
        this.customerService = customerService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/api/v1/public/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token){
        logger.info("Received email verification request with token: {}", token);

        String email = jwtTokenUtil.getEmail(token);
        Customer customer = customerService.findCustomerByEmail(email);

        if(customer == null) {
            logger.warn("Verification failed: No customer found for email extracted from token: {}", email);
            return ResponseEntity.badRequest().body("Invalid token");
        }
        if(customer.getGenerationToken() == null || !customer.getGenerationToken().equals(token)){
            logger.warn("Verification failed: Customer's stored generation token does not match provided token for email: {}", email);
            return ResponseEntity.badRequest().body("Invalid token");
        }
        if(!jwtTokenUtil.validateToken(token)){
            logger.warn("Verification failed: Token expired for email: {}", email);
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Token");
        }
        customer.setGenerationToken(null);
        customer.setIsEmailVerified(true);
        customerService.saveCustomer(customer);
        logger.info("Email verified successfully for customer with email: {}", email);
        return new ResponseEntity("User verified successfully", HttpStatus.OK);

    }
}