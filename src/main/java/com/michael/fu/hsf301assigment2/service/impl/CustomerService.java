package com.michael.fu.hsf301assigment2.service.impl;

import com.michael.fu.hsf301assigment2.entity.Customer;
import com.michael.fu.hsf301assigment2.repository.CustomerRepository;
import com.michael.fu.hsf301assigment2.service.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * CustomerService chịu trách nhiệm xử lý nghiệp vụ liên quan đến khách hàng,
 * đồng thời cung cấp chức năng xác thực người dùng cho Spring Security.

 * Lớp này implement interface UserDetailsService, cho phép tuỳ biến cách lấy thông tin người dùng (UserDetails)
 * từ cơ sở dữ liệu dựa trên email. Khi người dùng đăng nhập, Spring Security sẽ gọi phương thức loadUserByUsername
 * để lấy thông tin tài khoản khách hàng tương ứng.

 * Sử dụng CustomerRepository để truy vấn thông tin khách hàng từ database theo email.
 * Ghi log các thao tác thành công và lỗi nhằm hỗ trợ kiểm tra và theo dõi hệ thống.

 * Lưu ý: Lớp này chỉ cung cấp các thông tin cần thiết cho xác thực, chưa xử lý phân quyền (roles/authorities).
 */
@Service // customerService
public class CustomerService extends BaseService implements UserDetailsService  {

    private final CustomerRepository customerRepository;


    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("Attempting to load user by email: {}", email);
        Customer customer = customerRepository.findByEmail(email);
        if(customer != null){

            logger.info("loadUserByEmail: {}", email);
            return User.builder()  // User o day la class cung cấp cho spring security, khong phai User trong entity
                    .username(customer.getEmail())  // set username(o day la email) cho spring security
                    .password(customer.getPassword()) // set password cho spring security
                    .authorities("ROLE_"+ customer.getAccount().getRole().name())
                    .build();

        } else {
            logger.error("loadUserByEmail: {} not found", email);
            throw new UsernameNotFoundException("User not found");

        }
    }

    public Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public void saveCustomer(Customer customerRegister) {
        customerRepository.save(customerRegister);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public void saveCustomerByAdmin(Customer customer) {
        customer.setPassword("123456");
        customer.setIsEmailVerified(true); // Mặc định là chưa xác thực email
        save(customer);
    }
    public void save(Customer customer) {
       // Mặc định là chưa xác thực email
        customerRepository.save(customer);
        logger.info("Customer saved: {}", customer.getEmail());
    }

    public void update(Customer customer) {
        Customer existingCustomer = customerRepository.findByEmail(customer.getEmail());
        if(existingCustomer == null) {
            logger.warn("Customer with ID {} not found for update", customer.getCustomerId());
            return;
        } else {
            logger.info("Updating customer: {}", customer.getEmail());
            existingCustomer.setCustomerName(customer.getCustomerName());
            existingCustomer.setEmail(customer.getEmail());
            existingCustomer.setMobile(customer.getMobile());
            existingCustomer.setBirthday(customer.getBirthday());
            existingCustomer.setIdentifyCard(customer.getIdentifyCard());
            existingCustomer.setLicenceNumber(customer.getLicenceNumber());
            existingCustomer.setLicenceDate(customer.getLicenceDate());
        }
        customerRepository.save(existingCustomer);
        logger.info("Customer updated: {}", customer.getEmail());
    }

    public void deleteById(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            logger.info("Customer with ID {} deleted successfully", id);
        } else {
            logger.warn("Customer with ID {} not found for deletion", id);
        }
    }

    public Customer findById(Long id) {
        logger.info("Finding customer by ID: {}", id);
        return customerRepository.findById(id).orElse(null);
    }



    public long countAll() {
        return customerRepository.count();
    }

    public String generateOTP(int length) {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

}
