package com.fad.fad.model;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class TokenModel extends AbstractAuthenticationToken {

    private String token;

    public TokenModel(String token) {
        super(null);
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getPrincipal() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getToken() {
        return token;
    }
}
