package eu.tsystems.mms.tic.testframework.mobile.cloud.api;

import org.apache.commons.codec.binary.Base64;

/**
 * Class holding all necessary data for a connection to Cloud API.
 * 
 * @author mibu
 */
public class CloudApiConfiguration {

    /** Cloud API server */
    private String server;
    /** Cloud API user */
    private String user;
    /** Cloud API password */
    private String password;
    /** base 64 auth header */
    private String authStringEnc;

    /**
     * Constructor filling all fields.
     * 
     * @param server Cloud API server
     * @param user Cloud API user
     * @param password Cloud API password
     */
    public CloudApiConfiguration(final String server, final String user, final String password) {
        this.server = server.trim();
        if (!this.server.endsWith("/")) {
            this.server = this.server + "/";
        }
        if (!this.server.endsWith("api/v1/")) {
            this.server = this.server + "api/v1/";
        }
        this.user = user.trim();
        this.password = password.trim();
        createAuthString();
    }

    private void createAuthString() {
        String authString = getUser() + ":" + getPassword();
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        this.authStringEnc = new String(authEncBytes);
    }

    /**
     * @return the server
     */
    public String getServer() {
        return server;
    }

    /**
     * @param server the server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
        createAuthString();
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
        createAuthString();
    }

    /**
     * @return the authStringEnc
     */
    public String getAuthStringEnc() {
        return authStringEnc;
    }
}
