package eu.europa.ec.fisheries.uvms.message;

/**
 * Created by sanera on 13/04/2017.
 */

public class ConnectionProperties {

    private String providerURL;
    private String username;
    private String password;

    public ConnectionProperties(){

    }
    public ConnectionProperties(String providerURL, String username, String password) {
        this.providerURL = providerURL;
        this.username = username;
        this.password = password;
    }

    public String getProviderURL() {
        return providerURL;
    }

    public void setProviderURL(String providerURL) {
        this.providerURL = providerURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ConnectionProperties{" +
                "providerURL='" + providerURL + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
