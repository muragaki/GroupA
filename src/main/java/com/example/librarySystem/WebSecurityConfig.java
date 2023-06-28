package com.example.librarySystem;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{ 
		http.formLogin( login -> 
			login
			.loginProcessingUrl("/login")		
			.loginPage("/login")			//入力画面
			.defaultSuccessUrl("/goods", true)	//ログイン成功時に以降するURL
			.failureUrl("/login?error")		//ログイン失敗時に移行するURL
			.permitAll()
		).logout(logout ->
			logout
			.logoutSuccessUrl("/login")		//ログアウト成功時に移行するURL
		).authorizeHttpRequests(authz ->
			authz
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
			.requestMatchers("/").permitAll()
			.requestMatchers("/goods").permitAll()
			.requestMatchers("/signup").permitAll()
			.requestMatchers("/admin/**").hasRole("ADMIN")
			.anyRequest().authenticated()
		);
		return http.build();
	}

}
