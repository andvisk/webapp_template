package org.avwa.system;

import org.slf4j.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Singleton
@Startup
public class StartupEJB {

    @Inject
    Logger log;

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void init() {

        log.info("LOG ----------------");

    }
}
