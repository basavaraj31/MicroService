package com.basava.microservices.limitsservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.basava.microservices.limitsservice.config.Configuration;
import com.basava.microservices.limitsservice.model.Limits;

@RestController
public class LimitsController {

	@Autowired
	private Configuration configuration;
	
	
	@GetMapping("/limits")
	public Limits retrievLimits() {
		return new Limits(configuration.getMinimum()
				,configuration.getMaximum());
	}
}
