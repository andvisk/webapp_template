package org.avwa.system;

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

    @PostConstruct
    public void init() {

        log.info("LOG ----------------");

    }
}
