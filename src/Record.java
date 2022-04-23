public class Record {
    private String site;
    private String url;
    private String username;
    private String password;

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
}