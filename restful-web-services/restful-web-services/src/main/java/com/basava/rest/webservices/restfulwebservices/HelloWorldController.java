package com.basava.rest.webservices.restfulwebservices;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.basava.rest.webservices.restfulwebservices.model.HelloWorldBean;

@RestController
public class HelloWorldController {

	
	@Autowired
	private MessageSource messageSoruce;
	
	//@RequestMapping(method=RequestMethod.GET, path="/")
	@GetMapping("/hello-world")
	public String helloWorld() {
		return "Hello World";
	}
	
	@GetMapping("/hello-world-bean")
	public HelloWorldBean helloWorldBean() {
		return new HelloWorldBean("Hello World");
	}

	@GetMapping("/hello-world-pathVar/{name}")
	public HelloWorldBean helloWorldPathVar(
			@PathVariable String name
			) {
		return new HelloWorldBean(String.format("Hello World , %s", name));
	}
	
	
	@GetMapping("/hello-world-internationalized")
	public String helloWorldi18n(
		//	@RequestHeader(name="Accept-Language",required=false) Locale locale
			) {
		return messageSoruce.getMessage("good.morning.message", null, "Default msg", LocaleContextHolder.getLocale());
	}

}
