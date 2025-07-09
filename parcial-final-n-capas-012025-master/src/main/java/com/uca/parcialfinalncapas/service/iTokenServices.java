package com.uca.parcialfinalncapas.service;


import com.uca.parcialfinalncapas.entities.Token;
import com.uca.parcialfinalncapas.entities.User;

public interface iTokenServices {

    Token generateToken(User user);
}