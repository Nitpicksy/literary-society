package nitpicksy.literarysociety.config;

import nitpicksy.literarysociety.security.RestAuthenticationEntryPoint;
import nitpicksy.literarysociety.security.TokenAuthenticationFilter;
import nitpicksy.literarysociety.serviceimpl.UserServiceImpl;
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

                .authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/readers/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/writers/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/process/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/books/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/payments/confirm").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST,"/api/merchants/{name}/payment-data").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/merchants/payment-data")
                .hasAuthority("SUPPORT_PAYMENT_METHODS")

                .anyRequest().authenticated().and()
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