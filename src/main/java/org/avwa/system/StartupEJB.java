package org.avwa.system;

import org.avwa.jpaUtils.EntitiesService;
import org.slf4j.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;

@Singleton
@Startup
public class StartupEJB {

    @Inject
    Logger log;

    @Inject
    EntitiesService entitiesService;

    @PostConstruct
    public void init() {

        log.info("LOG ----------------");

        /*ApplicationProperties appP = new ApplicationProperties();
        appP.setName(AppPropNamesEnum.ALLOW_SOCIAL_LOGINS);
        appP.setValue("true");
        entitiesService.merge(appP);

        appP = new ApplicationProperties();
        appP.setName(AppPropNamesEnum.ALLOW_SOCIAL_LOGIN);
        appP.setParameter(OAuthProviderType.FACEBOOK.name());
        appP.setValue("true");
        entitiesService.merge(appP);

        appP = new ApplicationProperties();
        appP.setName(AppPropNamesEnum.ALLOW_SOCIAL_LOGIN);
        appP.setParameter(OAuthProviderType.GOOGLE.name());
        appP.setValue("true");
        entitiesService.merge(appP);*/

    }
}
