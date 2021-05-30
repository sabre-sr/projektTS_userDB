package ts.projekt.userDB;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.sql.*;

public class Database {
    public static Database database = new Database();
    private static Connection conn;

    private Database() {
        Connection conn = null;
        String path = "jdbc:mysql://localhost:3306/users";
        try {
            conn = DriverManager.getConnection(path, "root", "");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (conn != null)
            Database.conn = conn;
        try {
            if (!checkIfDbExists())
                createDb();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addUser(AuthUser user) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                        INSERT INTO users(username, email, passwordhash, salt) 
                        VALUES(?, ?, ?, ?); 
                """);
        statement.setString(1, user.getUser().getUsername());
        statement.setString(2, user.getUser().getEmail());
        statement.setString(3, user.getHash());
        statement.setBytes(4, user.getSalt());
        statement.execute();
        statement.close();
    }

    public boolean checkIfDbExists() throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                    SELECT *
                    FROM information_schema.tables
                    WHERE table_schema = 'users' 
                        AND table_name = 'users'
                    LIMIT 1;
                """);
        ResultSet result = statement.executeQuery();
        boolean exists = result.next();
        result.close();
        statement.close();
        return exists;
    }

    private void createDb() throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                CREATE TABLE `users` (
                  `id` int(11) NOT NULL AUTO_INCREMENT,
                  `username` varchar(32) NOT NULL,
                  `email` varchar(64) NOT NULL,
                  `passwordhash` varchar(512) NOT NULL,
                  `salt` binary(16) NOT NULL,
                  PRIMARY KEY (`id`),
                  UNIQUE KEY `users_email_uindex` (`email`),
                  UNIQUE KEY `users_id_uindex` (`id`),
                  UNIQUE KEY `users_username_uindex` (`username`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User database';
                """);
        statement.execute();
    }

    public User getUser(int id) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                SELECT id, username, email FROM users u WHERE u.id = ?
                """);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        User newUser = null;
        if (result.next()){
            newUser = new User(result.getString("username"), result.getInt("id"), result.getString("email"));
        }
        statement.close();
        result.close();
        return newUser;
    }

    public ImmutablePair<String, byte[]> getPassword(int id) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("""
                SELECT u.passwordhash, u.salt FROM users u WHERE u.id = ?
                """);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        ImmutablePair<String, byte[]> authData = null;
        if (result.next()) {
            authData = new ImmutablePair<>(result.getString("passwordhash"), result.getBytes("salt"));
        }
        result.close();
        statement.close();
        return authData;
    }

    public static void main(String[] args) {

    }
}
