package com.vishal.user_service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {

    private static final String SECRET_KEY="iam vishalkumarsawiamfromjharkhandworkinginRebit";
    public static final String USER_CLAIM="USER";
    public static final String ROLE_CLAIM="ROLE";
    public static final String USERID_CLAIM="USERID";

    public String CreateToken(String username, Collection<? extends GrantedAuthority> authorities, UUID userId){
        String[] role=new String[authorities.size()];
        int i=0;
        for(GrantedAuthority a:authorities){
            role[i]=a.getAuthority();
            i++;
        }
        String token= JWT.create()
                .withClaim(USER_CLAIM,username)
                .withArrayClaim(ROLE_CLAIM, role)
                .withClaim(USERID_CLAIM, userId.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + 9000000))
                .sign(Algorithm.HMAC256(SECRET_KEY));
        return token;
    }

    public String validateToken(String token){
        String validToken = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token)
                .getPayload();
        return new String(Base64.getDecoder().decode(validToken));
    }
}
