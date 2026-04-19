package owner.mysafefloridahome;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AppProperties appProperties) throws Exception {
        boolean adminConfigured = adminConfigured(appProperties);
        http
                .authorizeHttpRequests(authorize -> {
                    if (adminConfigured) {
                        authorize.requestMatchers("/admin/**").authenticated();
                    } else {
                        authorize.requestMatchers("/admin/**").denyAll();
                    }
                    authorize.anyRequest().permitAll();
                })
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        "/api/leads/**",
                        "/api/contact/**",
                        "/tools/opening-protection/quote-prep-brief/**",
                        "/vendor-handoffs/**",
                        "/vendor-presets/**"));
        if (adminConfigured) {
            http.httpBasic(withDefaults());
        }
        return http.build();
    }

    @Bean
    UserDetailsService userDetailsService(AppProperties appProperties, PasswordEncoder passwordEncoder) {
        if (!adminConfigured(appProperties)) {
            return new InMemoryUserDetailsManager();
        }
        return new InMemoryUserDetailsManager(User.withUsername(appProperties.getAdmin().getUsername())
                .password(passwordEncoder.encode(appProperties.getAdmin().getPassword()))
                .roles("ADMIN")
                .build());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private boolean adminConfigured(AppProperties appProperties) {
        return StringUtils.hasText(appProperties.getAdmin().getUsername())
                && StringUtils.hasText(appProperties.getAdmin().getPassword());
    }
}
