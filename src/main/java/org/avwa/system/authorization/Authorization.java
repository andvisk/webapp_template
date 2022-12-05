package org.avwa.system.authorization;

import java.util.Arrays;
import java.util.List;

import org.avwa.system.SessionEJB;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("authz")
@Stateless
public class Authorization {

    @Inject
    SessionEJB sessionEJB;

    private List<String> restfulEndPooints;


    @PostConstruct
    public void init() {
        restfulEndPooints = Arrays.asList("socialauth");
    }

    public List<String> getRestfulEndPooints() {
        return restfulEndPooints;
    }

}
