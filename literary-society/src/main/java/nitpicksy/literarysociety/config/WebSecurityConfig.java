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

                .antMatchers(HttpMethod.POST, "/api/merchants/{name}/payment-data").permitAll()
                .antMatchers(HttpMethod.GET, "/api/merchants/payment-data").hasAuthority("SUPPORT_PAYMENT_METHODS")

                .antMatchers(HttpMethod.GET, "/api/users").hasAnyAuthority("MANAGE_EDITORS", "MANAGE_LECTURERS")
                .antMatchers(HttpMethod.PUT, "/api/users/{id}").hasAnyAuthority("MANAGE_EDITORS", "MANAGE_LECTURERS")
                .antMatchers(HttpMethod.POST, "/api/users").permitAll()

                .antMatchers(HttpMethod.GET, "/api/books").permitAll()
                .antMatchers(HttpMethod.GET, "/api/books/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/books/start-publishing").hasAuthority("MANAGE_PUBLICATION_REQUESTS")
                .antMatchers(HttpMethod.GET, "/api/books/publication-request-form").hasAuthority("MANAGE_PUBLICATION_REQUESTS")
                .antMatchers(HttpMethod.GET, "/api/books/{id}/opinions-of-beta-readers").hasAuthority("MANAGE_PUBLICATION_REQUESTS")
                .antMatchers(HttpMethod.GET, "/api/books/publication-requests").hasAuthority("MANAGE_PUBLICATION_REQUESTS")

                .antMatchers(HttpMethod.POST, "/api/process/{taskId}").permitAll()

                .antMatchers(HttpMethod.POST, "/api/genres").permitAll()

                .antMatchers(HttpMethod.POST, "/api/payments/pay").permitAll()
                .antMatchers(HttpMethod.POST, "/api/payments/confirm").permitAll()

                .antMatchers(HttpMethod.GET, "/api/readers/start-registration").permitAll()
                .antMatchers(HttpMethod.GET, "/api/readers/registration-form").permitAll()
                .antMatchers(HttpMethod.GET, "/api/readers/beta/choose-genres").permitAll()

                .antMatchers(HttpMethod.GET, "/api/tasks").hasAuthority("MANAGE_TASKS")
                .antMatchers(HttpMethod.GET, "/api/tasks/{taskId}").hasAuthority("MANAGE_TASKS")
                .antMatchers(HttpMethod.GET, "/api/tasks/{taskId}/complete-and-download").hasAuthority("DOWNLOAD_BOOK_AND_COMPLETE_TASK")
                .antMatchers(HttpMethod.GET, "/api/tasks/{taskId}/complete-and-upload").hasAuthority("UPLOAD_BOOK_AND_COMPLETE_TASK")

                .antMatchers(HttpMethod.GET, "/api/tasks/{taskId}/submit-form-and-upload-image").hasAuthority("SUBMIT_FORM_AND_UPLOAD_IMAGE")

                .antMatchers(HttpMethod.GET, "/api/transactions/{id}").permitAll()

                .antMatchers(HttpMethod.GET, "/api/writers/start-registration").permitAll()
                .antMatchers(HttpMethod.GET, "/api/writers/registration-form").permitAll()

                .antMatchers(HttpMethod.GET, "/api/subscriptions/create-plans").permitAll()

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