package com.ABH.Auction;

import com.cloudinary.Cloudinary;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class AuctionApplication {

	@Value("${cloud.name}")
	private String cloudName;
	@Value("${cloud.key}")
	private String apiKey;
	@Value("${cloud.secret}")
	private String apiSecret;

	public static void main(String[] args) {
		SpringApplication.run(AuctionApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Auction").version("v1").description("Auction ABH")
						.termsOfService("http://swagger.io/terms/")
						.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}

	@Bean
	public Cloudinary cloudinaryConfig() {
		Cloudinary cloudinary;
		Map<String, String> config = new HashMap<>();
		config.put("cloud_name", cloudName);
		config.put("api_key", apiKey);
		config.put("api_secret", apiSecret);
		cloudinary = new Cloudinary(config);
		return cloudinary;
	}
}
