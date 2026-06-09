package com.task.Anything;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Base64;

@SpringBootApplication
public class AnythingApplication {

	public static void main(String[] args) {
		var key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		String secretBase64 = Base64.getEncoder().encodeToString(key.getEncoded());
		System.out.println("Copy this secret into application.properties:\n" + secretBase64);

		SpringApplication.run(AnythingApplication.class, args);
	}

}
