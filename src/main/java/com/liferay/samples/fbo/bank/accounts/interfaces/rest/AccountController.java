package com.liferay.samples.fbo.bank.accounts.interfaces.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account/api/v1")
public class AccountController {

    private static Logger LOG = LoggerFactory.getLogger(AccountController.class);
    
    /*
     * This method can be called anonymously
     */
    @GetMapping("/ping")
	public String ping() {
		return "pong";
	}

    /*
     * This method can only be called if a valid JWT token was provided
     * It returns the userId from the token
     */
	@GetMapping("/whoami")
	public String index(@AuthenticationPrincipal Jwt jwt, @CurrentSecurityContext SecurityContext context) {
		return jwt.getClaims().get("sub").toString();
	}

}