package com.ceiba.jenkins.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class IndexController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
	public String index()
	{
		return "Love beautiful code? We do too";
	}
}