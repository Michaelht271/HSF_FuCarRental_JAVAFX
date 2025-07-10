package com.michael.fu.hsf301assigment2.controller.customer;

import com.michael.fu.hsf301assigment2.session.CustomerSession;
import com.michael.fu.hsf301assigment2.controller.BaseController;
import com.michael.fu.hsf301assigment2.entity.Customer;
import com.michael.fu.hsf301assigment2.service.impl.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController extends BaseController {

    private final CustomerService customerService;
    private final CustomerSession customerSession;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String homePage(Model model) {
        Customer customer = customerSession.getCustomer();
        if (customer == null) {
            return "redirect:/login";
        }
        model.addAttribute("customer", customer);
        return "customer/home";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Customer customer = customerSession.getCustomer();
        if (customer == null) {
            return "redirect:/login";
        }
        model.addAttribute("customer", customer);
        return "customer/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("customer") Customer updatedCustomer, Model model) {
        Customer currentCustomer = customerSession.getCustomer();
        if (currentCustomer == null) return "redirect:/login";

        // Cập nhật tất cả các trường từ biểu mẫu
        currentCustomer.setCustomerName(updatedCustomer.getCustomerName());
        currentCustomer.setEmail(updatedCustomer.getEmail());
        currentCustomer.setMobile(updatedCustomer.getMobile());
        currentCustomer.setBirthday(updatedCustomer.getBirthday());
        currentCustomer.setIdentifyCard(updatedCustomer.getIdentifyCard());
        currentCustomer.setLicenceNumber(updatedCustomer.getLicenceNumber());
        currentCustomer.setLicenceDate(updatedCustomer.getLicenceDate());

        customerService.save(currentCustomer);
        model.addAttribute("success", "Cập nhật thông tin thành công!");
        return "redirect:/api/v1/customers/profile";
    }
    @PostMapping("/profile/change-password")
    public String changePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model
    ) {

        Customer customer = customerSession.getCustomer();
        if (customer == null) return "redirect:/login";

        if (!passwordEncoder.matches(currentPassword, customer.getPassword())) {
            model.addAttribute("error", "Mật khẩu hiện tại không đúng.");
            return "customer/profile";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu mới không khớp.");
            return "customer/profile";
        }

        customer.setPassword(passwordEncoder.encode(newPassword));
        customerService.save(customer);
        model.addAttribute("success", "Đổi mật khẩu thành công!");
        return "redirect:/api/v1/customers/profile";
    }
}
