package com.example.demo2.service;
import com.example.demo2.model.User;
import com.example.demo2.repo.RoleRepository;
import com.example.demo2.repo.UserRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public Map<String, String> processOAuthPostLogin(OAuth2User oauthUser, String provider) {

        Object subAttr = oauthUser.getAttribute("sub");
        if (subAttr == null) {
            throw new RuntimeException("Google 'sub' attribute is missing");
        }

        String providerId = subAttr.toString();
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("given_name");
        String surname = oauthUser.getAttribute("family_name");

        String username = email != null ? email : providerId;

        User user = userRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> {
                    User u = new User();
                    u.setUsername(username);
                    u.setProvider(provider);
                    u.setProviderId(providerId);
                    u.setEmail(email);
                    u.setName(name);
                    u.setSurname(surname);

                    var role = roleRepository.findByName("USER")
                            .orElseThrow(() -> new RuntimeException("USER role not found"));

                    u.setRoles(Set.of(role));
                    return userRepository.save(u);
                });

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getUsername());

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }
}