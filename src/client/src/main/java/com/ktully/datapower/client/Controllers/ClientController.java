package com.ktully.datapower.client.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
//
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import reactor.core.publisher.Mono;

@RestController
public class ClientController {
	
	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
	
	@Value("${datapower.host}")
	private String datapowerHost;
	
	@Value("${datapower.port}")
	private String datapowerPort;
	
	@GetMapping("/")
	public Mono<String> get() {
		
		String datapowerURL = "http://" + datapowerHost + ":" + datapowerPort;
		
		// Logging URL Components
		logger.info("datapowerHost = " + datapowerHost);
		logger.info("datapowerPort = " + datapowerPort);
		logger.info("Full datapowerURL = " + datapowerURL);
		
		// Make the call to DataPower
		Builder webClientBuilder = WebClient.builder();
		WebClient webClient = webClientBuilder.baseUrl(datapowerURL).build();
		
		logger.info("Making GET request to: " + datapowerURL);
		Mono<String> result = webClient.get().retrieve().bodyToMono(String.class);
		
		return result;
	}
}
