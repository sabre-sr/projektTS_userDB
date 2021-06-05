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
                        INSERT INTO users(username, email) 
                        VALUES(?, ?,); 
                """);
        statement.setString(1, user.getUser().getUsername());
        statement.setString(2, user.getUser().getEmail());
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
                CREATE TABLE users (
                  id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY UNIQUE,
                  username varchar(32) NOT NULL UNIQUE,
                  email varchar(64) NOT NULL UNIQUE,
                )COMMENT='User database';
                """);
        statement.execute();
    }

    public User getUser(User user) throws SQLException {
        PreparedStatement statement;
        if (user.getId() != 0){
            statement = conn.prepareStatement("""
                    SELECT id, username, email FROM users u WHERE u.id = ?
                    """);
            statement.setInt(1, user.getId());
        }
        else {
            statement = conn.prepareStatement("""
                    SELECT id, username, email FROM users u WHERE u.username = ?
                    """);
            statement.setString(1, user.getUsername());
        }
        ResultSet result = statement.executeQuery();
        User newUser = null;
        if (result.next()){
            newUser = new User(result.getString("username"), result.getInt("id"), result.getString("email"));
        }
        statement.close();
        result.close();
        return newUser;
    }

    public static void main(String[] args) {

    }

}
