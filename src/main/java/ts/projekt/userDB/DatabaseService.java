package ts.projekt.userDB;

import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class DatabaseService {
    @GetMapping(path = "users/{id}")
    public User getUser(@PathVariable int id) {
        User user = new User();
        try {
            user = Database.database.getUser(new User(id));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }
    @GetMapping(path = "users")
    public User getUser(@RequestParam(value = "username") String login) {
        User user = new User();
        try {
            user = Database.database.getUser(new User(login));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }
    @PostMapping(path = "register")
    public User register(@RequestBody User user) throws SQLException {
        int id = Database.database.addUser(user);
        if (id == 0) {
            return null;
        }
        else return Database.database.getUser(new User(id));
    }
}
