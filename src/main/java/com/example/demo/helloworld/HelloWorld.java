package com.example.demo.helloworld;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

@RestController
public class HelloWorld {

	@Autowired
	private MessageSource messageSource;
	
	@GetMapping(path="/hello-world")
	public String HelloWorld() {
		return "hello world";
	}
	@GetMapping(path="/hello-world-bean")
	public HelloWorldBean HelloWorld2() {
		return new  HelloWorldBean("hello world");
	}
	
	
	@GetMapping(path="/hello-world-bean/path-variable/{name}")
	public HelloWorldBean HelloWorldPathVariable(@PathVariable String name) {
		return new  HelloWorldBean(String.format("hello world %s",name));
	}
	
	@GetMapping(path="/hello-world-internationalize")
	public String HelloWorldInternationalization() {
		return messageSource.getMessage("good.morning.message",null, LocaleContextHolder.getLocale());
	}

}
