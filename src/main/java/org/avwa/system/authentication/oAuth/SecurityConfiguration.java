package org.avwa.system.authentication.oAuth;

import java.util.List;

import org.avwa.entities.OAuthProperties;
import org.avwa.enums.OAuthPropertiesNamesEnum;
import org.avwa.utils.AnnotationsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class SecurityConfiguration {

    private static Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    public static OAuthProvider getProvider(EntityManager em, OAuthProviderType providerType) {

        TypedQuery<OAuthProperties> query = em.createQuery(
                "SELECT e FROM " + AnnotationsUtils.getEntityName(OAuthProperties.class)
                        + " e WHERE e.provider = :provider",
                OAuthProperties.class);
        List<OAuthProperties> list = query.setParameter("provider", providerType).getResultList();

        String clientId = list.stream().filter(i -> i.getName() == OAuthPropertiesNamesEnum.CLIENT_ID).findAny().get()
                .getValue();
        String clientSecret = list.stream().filter(i -> i.getName() == OAuthPropertiesNamesEnum.CLIENT_SECRET).findAny()
                .get().getValue();
        String returnUrl = list.stream().filter(i -> i.getName() == OAuthPropertiesNamesEnum.REDIRECT_URI).findAny()
                .get().getValue();
        String accessInfoUrl = list.stream().filter(i -> i.getName() == OAuthPropertiesNamesEnum.ACCESS_INFO_URI)
                .findAny().get().getValue();

        OAuthProvider oAuthProvider = new OAuthProvider(clientId, clientSecret, returnUrl, accessInfoUrl);

        return oAuthProvider;
    }
}
