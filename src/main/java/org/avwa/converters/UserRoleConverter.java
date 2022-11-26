package org.avwa.converters;

import org.avwa.enums.UserRoleEnum;

import jakarta.faces.convert.EnumConverter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value="userRoleConverter")
public class UserRoleConverter extends EnumConverter{
    public UserRoleConverter(){
        super(UserRoleEnum.class);
    }
}
