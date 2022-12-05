package org.avwa.system;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.avwa.entities.ChangePasswdTokens;
import org.avwa.jpaUtils.EntitiesService;
import org.avwa.utils.AnnotationsUtils;

import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class SystemTimer {

    @Inject
    EntitiesService entService;

    @Schedule(hour = "*", minute = "*/10", persistent = false)
    public void markUnusedChangePasswordTokens() {
        String jpqlQuery = "UPDATE " + AnnotationsUtils.getEntityName(ChangePasswdTokens.class)
                + " AS t SET t.active = false WHERE t.active = true AND t.valid_until_dateTime < :valid_until_dateTime";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("valid_until_dateTime", LocalDateTime.now());

        entService.executeUpdate(jpqlQuery, parameters);
    }
}
