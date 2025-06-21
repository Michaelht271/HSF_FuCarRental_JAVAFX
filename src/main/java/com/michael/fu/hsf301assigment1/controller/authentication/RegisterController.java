package com.michael.fu.hsf301assigment1.controller.authentication;

import com.michael.fu.hsf301assigment1.controller.BaseController;
import com.michael.fu.hsf301assigment1.entity.Account;
import com.michael.fu.hsf301assigment1.entity.Customer;
import com.michael.fu.hsf301assigment1.entity.Role;
import com.michael.fu.hsf301assigment1.service.impl.CustomerService;
import com.michael.fu.hsf301assigment1.service.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.michael.fu.hsf301assigment1.util.JwtTokenUtil;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;

@Controller
public class RegisterController extends BaseController {

    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService ;

    @Autowired
    public RegisterController(CustomerService customerService, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.customerService = customerService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(java.util.Date.class, new org.springframework.beans.propertyeditors.CustomDateEditor(dateFormat, true));
    }


    // Đăng ký tài khoản
    // Method nay chiu trach nhiem viec xử lý đăng ký tài khoản
    // Gom cac buoc thuc hien sau:
    // 1. Nhan customerResgister tu client
    // 2. Kiem tra xem customerRegister da ton tai trong database
    //    2.1 Nếu customerRegister da ton tai bs
    //      2.1.1 Da xac thuc tai khoan thi tra ve loi
    //      2.1.2 Chua xac thuc tai khoan thi gan thong tin tai customerRegister vao customer
    //            2.1.2.1 Lay Email , tao token
    //            2.1.2.2 Gan thong tin vao customer
    //            2.1.2.3 Luu thong tin customer vao database
    //            2.1.2.4 Gui mail xac thuc tai khoan
    //    2.2 Neu CustomerRegister chua ton tai
    //      2.2.1 Lay Email , tao token va gan thong tin vao customer
    //      2.2.2 Luu thong tin customer vao database
    //      2.2.3 Gui mail xac thuc tai khoan
    //  3 tra ve ket qua



    @PostMapping(value="/api/v1/public/register")
    public ResponseEntity<String> createCustomer(@ModelAttribute("customer") Customer customerRegister    ){
        Customer customer = customerService.findCustomerByEmail(customerRegister.getEmail());

        if(customer != null){
            if(customer.getGenerationToken() != null){
                logger.info("Customer is already registered");
                return new ResponseEntity<>("Customer is already registered", HttpStatus.BAD_REQUEST);
            } else {
                logger.info("Customer already registered, but not verified yet");

                String email = customerRegister.getEmail();
                String token = JwtTokenUtil.generateToken(email);

                emailService.sendVerification(email, token);
                logger.info("Verification email sent to {}", customer);
                return new ResponseEntity<>("Customer already registered, but not verified yet", HttpStatus.OK);
            }
        } else {
            logger.info("Customer not registered yet");
            customerRegister.setPassword(passwordEncoder.encode(customerRegister.getPassword()));
            String token = JwtTokenUtil.generateToken(customerRegister.getEmail());
            String email = customerRegister.getEmail();
            Account account = new Account();
            account.setRole(Role.USER);
            account.setCustomer(customerRegister);
            account.setAccountName(customerRegister.getCustomerName());
            customerRegister.setAccount(account);
            customerRegister.setGenerationToken(token);
            customerService.saveCustomer(customerRegister);
            emailService.sendVerification(email, token);
            logger.info("Verification email sent to  {}: ", email);
            return new ResponseEntity<>("Customer not registered yet, check Your Email to verify account", HttpStatus.OK);
        }

    }


}