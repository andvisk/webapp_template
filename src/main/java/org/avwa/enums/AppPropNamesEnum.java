package org.avwa.enums;

public enum AppPropNamesEnum {
    ALLOW_SOCIAL_LOGINS("Leisti prisijungti soc.tinklų vartotojams"),
    ALLOW_SOCIAL_LOGIN("Leisti prisijungti soc. tinklo vartotojams"),
    ALLOW_REGISTERING_USERS("Leisti vartotojams registruotis");

    private String description;

    AppPropNamesEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
