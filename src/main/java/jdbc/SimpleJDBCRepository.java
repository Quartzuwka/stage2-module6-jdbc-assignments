package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "insert into myusers (firstname, lastname, age) values ((?), (?), (?))";
    private static final String updateUserSQL = "update myusers set id = (?), firstname = (?), lastname = (?), age = (?) where id = (?)";
    private static final String deleteUser = "delete from myusers where id = (?)";
    private static final String findUserByIdSQL = "select * from myusers where id = (?)";
    private static final String findUserByNameSQL = "select * from myusers where firstname = (?)";
    private static final String findAllUserSQL = "select * from myusers";

    public Long createUser(User user) {
        try (Connection conn = CustomDataSource.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(createUserSQL)) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setInt(3, user.getAge());
            return (long) statement.executeUpdate(createUserSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findUserById(Long userId) {
        try (Connection conn = CustomDataSource.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(findUserByIdSQL)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() ? new User(resultSet.getLong("id"), resultSet.getString("firstName"),
                    resultSet.getString("lastName"), resultSet.getInt("age")) : null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findUserByName(String userName) {
        try (Connection conn = CustomDataSource.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(findUserByIdSQL)) {
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() ? new User(resultSet.getLong("id"), resultSet.getString("firstName"),
                    resultSet.getString("lastName"), resultSet.getInt("age")) : null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> findAllUser() {
        try (Connection conn = CustomDataSource.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(findAllUserSQL)) {
            ResultSet resultSet = statement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new User(resultSet.getLong("id"), resultSet.getString("firstName"),
                        resultSet.getString("lastName"), resultSet.getInt("age")));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User updateUser(User user) {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(updateUserSQL)) {
            ps.setLong(1, user.getId());
            ps.setLong(5, user.getId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setInt(4, user.getAge());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void deleteUser(Long userId) {
        try (Connection connection = CustomDataSource.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteUser)) {
            ps.setLong(1,userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}