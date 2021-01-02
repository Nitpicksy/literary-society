package nitpicksy.paymentgateway.config;

import nitpicksy.paymentgateway.security.RestAuthenticationEntryPoint;
import nitpicksy.paymentgateway.security.TokenAuthenticationFilter;
import nitpicksy.paymentgateway.serviceimpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.ws.rs.GET;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl jwtUserDetailsService;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(jwtUserDetailsService.passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()

                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/payment-methods")
                .hasAuthority("MANAGE_PAYMENT_METHODS").and()
                .authorizeRequests().antMatchers(HttpMethod.PUT, "/api/payment-methods/{id}")
                .hasAuthority("MANAGE_PAYMENT_METHODS").and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/companies")
                .hasAuthority("MANAGE_COMPANIES").and()
                .authorizeRequests().antMatchers(HttpMethod.PUT, "/api/companies/{id}")
                .hasAuthority("MANAGE_COMPANIES").and()

                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/orders")
                .hasAuthority("CREATE_ORDER").and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/merchants/{name}/payment-data")
                .hasAuthority("CREATE_ORDER")

                .anyRequest().permitAll().and()
                .cors().and()
                .addFilterBefore(new TokenAuthenticationFilter(jwtUserDetailsService.tokenUtils,
                        jwtUserDetailsService), BasicAuthenticationFilter.class);
        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "/favicon.ico", "/favicon.png", "/**/*.html",
                "/**/*.css", "/**/*.js", "/assets/**", "/*.jpg");
    }

}