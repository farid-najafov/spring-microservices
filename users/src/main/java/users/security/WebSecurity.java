package users.security;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import users.service.UserService;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurity extends WebSecurityConfigurerAdapter {
    
    private final Environment env;
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests().antMatchers("/**")
                .hasIpAddress(env.getProperty("gateway.ip"))
                .and().addFilter(getAuthFilter());
        
        http.headers().frameOptions().disable();
    }
    
    @SneakyThrows
    private AuthenticationFilter getAuthFilter() {
        AuthenticationFilter authFilter = new AuthenticationFilter(userService, env, authenticationManager());
        authFilter.setFilterProcessesUrl(env.getProperty("login.url.path"));
        return authFilter;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(encoder);
    }
}
