package io.fervo.takecost.projectestimation.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserDetailsServiceConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .roles(Role.ADMIN.name())
                .build();

        UserDetails manager = User.builder()
                .username("manager")
                .password(passwordEncoder.encode("password"))
                .roles(Role.PROJECT_MANAGER.name())
                .build();

        UserDetails teamMember = User.builder()
                .username("member")
                .password(passwordEncoder.encode("password"))
                .roles(Role.TEAM_MEMBER.name())
                .build();

        return new InMemoryUserDetailsManager(admin, manager, teamMember);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // TODO: Replace with something stronger, e.g. Argon2PasswordEncoder
    }
}
