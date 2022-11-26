package org.avwa.system.authentication.oAuth.accountInfo;

public class OAccountInfo {
    private String id = null;
    private String name = null;
    private String email = null;
    //linkedin
    private String emailAddress = null;
    private String firstName = null;
    private String lastName = null;
    //foursuqre
    private Response response = null;
    //instagram
    public Data data = null;

    public String getId() {
        if (id != null) {
            return id;
        } else {
            if (response != null) {
                return response.getUser().getId();
            }else{
                return data.getId();
            }
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if (name != null) {
            return name;
        } else {
            if ((firstName != null) || (lastName != null)) {
                String grazinimas = "";
                if (firstName != null) grazinimas = firstName;
                if (lastName != null) grazinimas = grazinimas + ((grazinimas.length() > 0) ? " " : "") + lastName;
                return grazinimas;
            } else {
                if (response != null) {
                    String grazinimas = "";
                    if (response.getUser().getFirstName() != null) grazinimas = response.getUser().getFirstName();
                    if (response.getUser().getLastName() != null)
                        grazinimas = grazinimas + ((grazinimas.length() > 0) ? " " : "") + response.getUser().getLastName();
                    return grazinimas;
                }else{
                    return data.getFull_name();
                }
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        if (email != null) {
            return email;
        } else {
            if (emailAddress != null) {
                return emailAddress;
            } else {
                if (response != null) {
                    return response.getUser().getContact().get("email");
                }
            }
        }
        return null;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
