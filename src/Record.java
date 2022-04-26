import java.io.Serializable;

/*
    Record class which holds the site's name, its url, the username
    and the password to show them to the user in the password manager
 */
public class Record implements Serializable {
    private String site;
    private String url;
    private String username;
    private String password;

    public Record(){
        site = "";
        url = "";
        username = "";
        password = "";
    }
    public Record(String site, String url, String username, String password) {
        this.site = site;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getSite() {
        return site;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public String toString() { return "Website: " + site + " url: "+ url + " username: " + username + " password: " + password;}
}