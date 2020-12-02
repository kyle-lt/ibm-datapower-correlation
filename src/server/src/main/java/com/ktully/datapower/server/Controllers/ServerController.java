package com.ktully.datapower.server.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {
	
	@GetMapping("/")
	public String get() {
		return "Server Response!";
	}

}
