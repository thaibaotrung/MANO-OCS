package org.acme.Model;

public class AuthenticationResponse {
    private long expiredIn;
    private String accessToken;

    public AuthenticationResponse(long expiredIn, String accessToken) {
        this.expiredIn = expiredIn;
        this.accessToken = accessToken;
    }

    public long getExpiredIn() {
        return expiredIn;
    }

    public void setExpiredIn(long expiredIn) {
        this.expiredIn = expiredIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
