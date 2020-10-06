package nl.femalert.femserver.model.helper.security;

public class JWTokenInfo {

    public static final String KEY = "tokenInfo";

    private String userId;
    private String username;
    private boolean dungeonMaster;
    private boolean admin;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isDungeonMaster() {
        return dungeonMaster;
    }

    public void setDungeonMaster(boolean dungeonMaster) {
        this.dungeonMaster = dungeonMaster;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
