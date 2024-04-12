package com.phishing.authservice.component.passport;

import com.phishing.authservice.domain.User;
import org.springframework.stereotype.Component;

@Component
public class PassportGenerator {
        public Passport generatePassport(User user){
            return new Passport(
                    user.getId(),
                    user.getEmail(),
                    user.getNickname(),
                    user.getRole().toString()
            );
        }
}
