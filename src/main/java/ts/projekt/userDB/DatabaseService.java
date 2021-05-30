package ts.projekt.userDB;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class DatabaseService {
    @GetMapping(path = "users/{id}")
    public User getUser(@PathVariable int id) {
        User user = new User();
        try {
            user = Database.database.getUser(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }
}
