package com.michael.fu.hsf301assigment1.controller.admin;

import com.michael.fu.hsf301assigment1.entity.Customer;
import com.michael.fu.hsf301assigment1.service.impl.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/admin/customers")
public class CustomerManagementController {

    private final CustomerService customerService;

    @Autowired
     public CustomerManagementController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("customer", new Customer());
        return "admin/customers"; // Thymeleaf view: src/main/resources/templates/admin/customers.html
    }

    @PostMapping("/add")
    public String addCustomer(@ModelAttribute("customer") Customer customer) {
        // Set a default password for new customers
        // You might want to use a more secure default password
        customerService.saveCustomerByAdmin(customer);
        return "redirect:/api/v1/admin/customers";
    }

    @PostMapping("/update")
    public String updateCustomer(@ModelAttribute("customer") Customer customer) {
        // Get existing customer to preserve the password
        Customer existingCustomer = customerService.findById(customer.getCustomerId());
        customer.setPassword(existingCustomer.getPassword());
        customerService.update(customer);
        return "redirect:/api/v1/admin/customers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id) {
        customerService.deleteById(id);
        return "redirect:/api/v1/admin/customers";
    }

//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable Long id, Model model) {
//        Customer customer = customerService.findById(id);
//        model.addAttribute("editCustomer", customer);
//        model.addAttribute("customers", customerService.findAll());
//        return "admin/customers";
//    }
}
