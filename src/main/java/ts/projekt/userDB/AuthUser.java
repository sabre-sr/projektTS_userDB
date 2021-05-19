package ts.projekt.userDB;

public class AuthUser {
    private User user;
    private String hash;
    private byte[] salt;

    public AuthUser(User user, String hash, byte[] salt) {
        this.user = user;
        this.hash = hash;
        this.salt = salt;
    }

    public User getUser() {
        return user;
    }

    public String getHash() {
        return hash;
    }

    public byte[] getSalt() {
        return salt;
    }
}
