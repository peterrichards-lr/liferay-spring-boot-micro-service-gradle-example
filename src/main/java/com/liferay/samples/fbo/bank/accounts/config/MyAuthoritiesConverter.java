package com.liferay.samples.fbo.bank.accounts.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

/*
 * This class reads the claims from the Liferay JWT Access Token
 * It extracts the scopes
 */
public class MyAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(final Jwt jwt) {

        List<GrantedAuthority> scopesCollection =
        		new ArrayList<String>(
        				Arrays.asList(
        						((String) jwt.getClaim("scope")).split(" ")
        						)
        				).stream()
                .map(scopeName -> "SCOPE_" + scopeName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        List<GrantedAuthority> grantedAuthorities = scopesCollection;

        return grantedAuthorities;
    }
    
}
