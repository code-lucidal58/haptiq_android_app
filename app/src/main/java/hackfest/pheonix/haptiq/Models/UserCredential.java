package hackfest.pheonix.haptiq.Models;

/**
 * Created by aanisha
 */

public class UserCredential {
    private int id;
    private String username;
    private String password;
    private String url;

    //empty constructor
    public UserCredential() {
    }

    public UserCredential(int id, String username, String password, String url) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public UserCredential(String username, String password, String url) {
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getUrl() {
        return url;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
