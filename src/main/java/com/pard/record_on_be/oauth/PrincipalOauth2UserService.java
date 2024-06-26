package com.pard.record_on_be.oauth;


import com.pard.record_on_be.oauth.dto.OAuthAttributes;
import com.pard.record_on_be.oauth.dto.SessionUser;
import com.pard.record_on_be.user.entity.User;
import com.pard.record_on_be.user.repo.UserRepo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final UserRepo userRepo;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest)
            throws OAuth2AuthenticationException {
        log.info("📍 google userRequest: "+oAuth2UserRequest );

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        log.info("📍 oauth : "+ oAuth2User.getAttributes());

        //// 객체에서 다른 정보(클라이언트 ID, 클라이언트 시크릿 등)를 가져오는 데 사용하지만, 여기선 사용 X
        // String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        //// userNameAttributeName : 사용자의 고유 식별자 속성 이름, 업체마다 이게 다름.
        // ex) Google -> sub , Facebook -> id
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes attributes = OAuthAttributes.of(userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user));

        return super.loadUser(oAuth2UserRequest);
    }
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepo.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName()))
                .orElse(attributes.toEntity());

        return userRepo.save(user);
    }
}
