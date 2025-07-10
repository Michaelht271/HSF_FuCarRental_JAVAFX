package com.michael.fu.hsf301assigment2.config;

import com.michael.fu.hsf301assigment2.service.impl.CustomerService;
import com.michael.fu.hsf301assigment2.session.CustomerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


/**
 * SecurityConfig chịu trách nhiệm cấu hình bảo mật cho ứng dụng.
 * - Cấu hình AuthenticationManager bằng AuthenticationManagerBuilder.
 * - Cấu hình phân quyền (authorizeHttpRequests).
 * - Cấu hình formLogin, logout và CSRF.
 */
@Configuration // Đánh dấu đây là một lớp cấu hình Spring.
@EnableWebSecurity // Bật tính năng bảo mật Web trong Spring Security.
// Tự động tạo constructor cho các final fields.
public class SecurityConfig extends  BaseConfig{

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final CustomerService appUserService;
    private final CustomerSession customerSession;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(CustomerService appUserService,
                          CustomerSession customerSession,
                          UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.customerSession = customerSession;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new MyAuthenticationSuccessHandler(appUserService, customerSession);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        logger.info("Đang cấu hình AuthenticationManager.");

        // Lấy builder để cấu hình xác thực
        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        // Cung cấp UserDetailsService và PasswordEncoder để xác thực
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        logger.info("Hoàn tất cấu hình AuthenticationManager.");
        return authenticationManagerBuilder.build();
    }

    /**
     * Bean xử lý sau khi xác thực thành công.
     * Tùy theo role của người dùng, sẽ redirect đến trang khác nhau.
     * Bean cấu hình bộ lọc bảo mật chính.
     * Bao gồm cấu hình phân quyền, login, logout và CSRF.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Bắt đầu cấu hình SecurityFilterChain.");

        http
                // Đăng ký AuthenticationManager đã build thủ công
                .authenticationManager(authenticationManager(http))

                // Cấu hình phân quyền cho từng URL
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/public/login",          // Hiển thị form login (GET)
                                "/api/v1/public/login?error",    // Khi nhập sai mật khẩu
                                "/api/v1/public/logout",         // Đăng xuất
                                "/api/v1/public/register",
                                "/api/v1/public/verify",
                                "/api/v1/public/",               // Trang chủ public
                                "/css/**", "/js/**", "/images/**", "/static/**", "/favicon.ico"
                        ).permitAll()

                        .requestMatchers("/api/v1/customers/**").hasRole("USER")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                // Cấu hình trang login tùy chỉnh
                .formLogin(login -> login
                        .loginPage("/api/v1/public/login") // URL GET trang login
                        .loginProcessingUrl("/api/v1/public/process-login") // URL POST xử lý login
                        .successHandler(authenticationSuccessHandler()) // Xử lý sau login thành công
                        .failureUrl("/api/v1/public/login?error")
                        .permitAll()
                )

                // Cấu hình logout
                .logout(logout -> logout
                        .logoutUrl("/api/v1/public/logout") // URL xử lý logout
                        .logoutSuccessUrl("/api/v1/public/login?logout") // Chuyển về login sau khi logout
                        .invalidateHttpSession(true) // Hủy session
                        .clearAuthentication(true) // Xóa xác thực
                        .permitAll()
                )

                // Vô hiệu hóa CSRF (hữu ích khi làm việc với REST API hoặc khi xử lý đơn giản)
                .csrf(AbstractHttpConfigurer::disable);

        logger.info("Hoàn tất cấu hình SecurityFilterChain.");
        return http.build(); // Trả về chuỗi filter đã cấu hình
    }
}
