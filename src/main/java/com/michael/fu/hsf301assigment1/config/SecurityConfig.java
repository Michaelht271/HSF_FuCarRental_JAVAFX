package com.michael.fu.hsf301assigment1.config;

import com.michael.fu.hsf301assigment1.service.impl.CustomerService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@AllArgsConstructor // Tự động tạo constructor cho các final fields.
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    // Dịch vụ người dùng tùy chỉnh, dùng để truy vấn người dùng từ DB.
    private final CustomerService appUserService;

    /**
     * Bean UserDetailsService dùng để Spring gọi khi xác thực người dùng.
     * Ở đây trả về CustomerService đã implement UserDetailsService.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        logger.info("Khởi tạo Bean UserDetailsService với CustomerService.");
        return appUserService;
    }

    /**
     * Bean PasswordEncoder sử dụng thuật toán BCrypt để mã hóa mật khẩu.
     * Được sử dụng trong quá trình xác thực (so sánh mật khẩu).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Khởi tạo Bean PasswordEncoder với BCrypt.");
        return new BCryptPasswordEncoder();
    }

    /**
     * Cấu hình AuthenticationManager bằng cách kết hợp UserDetailsService và PasswordEncoder.
     * Đây là nơi xử lý xác thực người dùng bằng dữ liệu từ DB.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        logger.info("Đang cấu hình AuthenticationManager.");

        // Lấy builder để cấu hình xác thực
        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        // Cung cấp UserDetailsService và PasswordEncoder để xác thực
        authenticationManagerBuilder
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());

        logger.info("Hoàn tất cấu hình AuthenticationManager.");
        return authenticationManagerBuilder.build();
    }

    /**
     * Bean xử lý sau khi xác thực thành công.
     * Tùy theo role của người dùng, sẽ redirect đến trang khác nhau.
     */
    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        logger.info("Khởi tạo Bean AuthenticationSuccessHandler để điều hướng theo Role.");

        return (request, response, authentication) -> {
            // Duyệt qua danh sách role của người dùng đã đăng nhập
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                String role = authority.getAuthority();

                // Nếu là admin thì chuyển hướng đến dashboard admin
                if ("ROLE_ADMIN".equals(role)) {
                    response.sendRedirect("/api/v1/admin/dashboard");
                    return;
                }
                // Nếu là user thì chuyển đến trang khách hàng
                else if ("ROLE_USER".equals(role)) {
                    response.sendRedirect("/api/v1/customers/home");
                    return;
                }
            }

            // Nếu không có role nào phù hợp, chuyển về trang public
            logger.warn("Không tìm thấy Role phù hợp, chuyển hướng về trang public.");
            response.sendRedirect("/api/v1/public/");
        };
    }

    /**
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
                        .successHandler(myAuthenticationSuccessHandler()) // Xử lý sau login thành công
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
