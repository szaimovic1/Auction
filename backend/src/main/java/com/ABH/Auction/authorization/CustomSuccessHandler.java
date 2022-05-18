package com.ABH.Auction.authorization;

import com.ABH.Auction.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;

    @Value("${frontend.path}")
    private String frontend;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomOAuth2User attributes = new CustomOAuth2User((CustomOAuth2User) authentication.getPrincipal());
        Map<String, String> tokenName = userService.loginOAuth2(attributes);
        String url = frontend;
        if(tokenName.get("user") == null) {
            url = UriComponentsBuilder.fromUriString(frontend + "/oAuth2-login")
                    .queryParam("token", tokenName.get("token"))
                    .queryParam("name", tokenName.get("name"))
                    .build().toUriString();
        }
        getRedirectStrategy().sendRedirect(request, response, url);
    }
}
