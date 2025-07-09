package com.uca.parcialfinalncapas.service.impl;



import com.uca.parcialfinalncapas.entities.Token;
import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.repository.iTokenRepository;
import com.uca.parcialfinalncapas.service.iTokenServices;
import com.uca.parcialfinalncapas.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements iTokenServices {

    private final TokenUtils tokenUtils;
    private final iTokenRepository TokenRepository;

    @Override
    public Token generateToken(User user) {
        Token token = new Token();
        token.setToken(tokenUtils.generateToken(user));
        token.setExpiresIn(tokenUtils.getExpiresIn());

        user.addToken(token);
        TokenRepository.save(token);
        return token;
    }
}