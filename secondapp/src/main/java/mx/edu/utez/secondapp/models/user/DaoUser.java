package mx.edu.utez.secondapp.models.user;

import mx.edu.utez.secondapp.models.role.Role;
import mx.edu.utez.secondapp.utils.MySQLConnection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoUser {
    private Connection conn;
    private PreparedStatement ps;
    private CallableStatement cs;
    private ResultSet rs;

    public User loadUserByUsernameAndPassword(String username,
                                              String password) {
        try {
            conn = new MySQLConnection().connect();
            String query = "SELECT u.id, u.username," +
                    "p.name, p.surname, ifnull(p.lastname, '') as lastname," +
                    "    r.description  FROM users u " +
                    "INNER JOIN people p ON p.id = u.people_id " +
                    "    INNER JOIN roles r ON r.id = u.roles_id " +
                    "WHERE u.username = ? AND u.password = ? AND u.status = 1;";
            ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                Person person = new Person();
                person.setName(rs.getString("name"));
                person.setSurname(rs.getString("surname"));
                person.setLastname(rs.getString("lastname"));
                user.setPerson(person);
                Role role = new Role();
                role.setDescription(rs.getString("description"));
                user.setRole(role);
                return user;
            }
        } catch (SQLException e) {
            Logger.getLogger(DaoUser.class.getName())
                    .log(Level.SEVERE,
                            "Credentials mismatch: " + e.getMessage());
        } finally {
            close();
        }
        return null;
    }

    public void close() {
        try {
            if (conn != null)
                conn.close();
            if (ps != null)
                ps.close();
            if (cs != null)
                cs.close();
            if (rs != null)
                rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
