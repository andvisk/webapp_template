package org.avwa.converters;

import org.avwa.system.authentication.oAuth.OAuthProviderType;

import jakarta.faces.convert.EnumConverter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value="oAuthProviderTypeConverter")
public class OAuthProviderTypeConverter extends EnumConverter{
    public OAuthProviderTypeConverter(){
        super(OAuthProviderType.class);
    }
}
