public class Record {
    private String site;
    String url;
    String username;
    String password;

    //constructors
    public Record(String site, String url, String username, String password) {
        this.site = site;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    // getter/setter functions for the private variables
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
}