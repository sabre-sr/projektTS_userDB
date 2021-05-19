package ts.projekt.userDB;

import java.sql.*;

public class Database {
    public static Database bazaDanych = new Database();
    private static Connection conn;
    private PreparedStatement statement;
    private ResultSet result;

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
        statement = conn.prepareStatement("""
        INSERT INTO users(username, email, passwordhash, salt) 
        VALUES(?, ?, ?, ?); 
""");
        statement.setString(1, user.getUser().getUsername());
        statement.setString(2, user.getUser().getEmail());
        statement.setString(3, user.getHash());
        statement.setBytes(4, user.getSalt());
        statement.execute();
    }

    public boolean checkIfDbExists() throws SQLException {
        statement = conn.prepareStatement("""
            SELECT *
            FROM information_schema.tables
            WHERE table_schema = 'users' 
                AND table_name = 'users'
            LIMIT 1;
        """);
        result = statement.executeQuery();
        return result.next();
    }

    private void createDb() throws SQLException {
        statement = conn.prepareStatement("""
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
    }
    public static void main(String[] args){

    }
}
