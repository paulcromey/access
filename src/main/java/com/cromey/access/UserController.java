package com.cromey.access;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	/* Maps to all HTTP actions by default (GET,POST,..) */
	@RequestMapping("/users")
	public @ResponseBody String getUsers() {
		return "{\"users\":[{\"firstname\":\"Paul\", \"lastname\":\"Cromey\"}]}";
	}
}