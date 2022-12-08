package org.avwa.system;

import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.avwa.entities.User;

public class HttpSessionInfo {

    private final String dateAndTimeFormat = "yyyy MM dd HH:mm";
    SimpleDateFormat format = new SimpleDateFormat(dateAndTimeFormat);

    private String id;
    private User user;
    private String ip;

    private long created;

    public HttpSessionInfo(String id, String ip, long created) {
        this.id = id;
        this.ip = ip;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getUserName() {
        if (user != null)
            if (StringUtils.isNoneBlank(user.getName()))
                return user.getName();
            else if (StringUtils.isNoneBlank(user.getEmail())) {
                return user.getEmail();
            }
        return "Neregistruotas vartotojas";
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getCreated() {
        return created;
    }

    public String getCreatedHRF() {
        return format.format(created);
    }

}
