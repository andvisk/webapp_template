package org.avwa.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;

public class LoggerProducer {
    @Produces
    public Logger getLogger(InjectionPoint p) {
    
        return LoggerFactory.getLogger(p.getMember().getDeclaringClass());

    }
}
