package org.avwa.system.authorization;

import org.avwa.system.SessionEJB;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("authz")
@Stateless
public class Authorization{

   @Inject
    SessionEJB sessionEJB;


}
