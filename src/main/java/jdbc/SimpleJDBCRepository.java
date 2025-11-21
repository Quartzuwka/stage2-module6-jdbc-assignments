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

    private static final String createUserSQL = "INSERT INTO myusers (FIRST_NAME, LAST_NAME, AGE) VALUES (?,?,?)";
    private static final String updateUserSQL = "UPDATE myusers SET FIRST_NAME=?, LAST_NAME=?, AGE=? WHERE ID = ?";
    private static final String deleteUser = "DELETE FROM myusers WHERE ID = ?";
    private static final String findUserByIdSQL = "SELECT * FROM myusers WHERE ID = ?";
    private static final String findUserByNameSQL = "SELECT * FROM myusers WHERE FIRST_NAME = ?";
    private static final String findAllUserSQL = "SELECT * FROM myusers";

    public Long createUser(User argUser) {


        try (Connection connection1 = CustomConnector.getConnection(CustomDataSource.PropertiesUtil.getByKey("postgres.url"), CustomDataSource.PropertiesUtil.getByKey("postgres.name"), CustomDataSource.PropertiesUtil.getByKey("postgres.password"))) {

            PreparedStatement ps = connection1.prepareStatement(createUserSQL, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, argUser.getFirstName());
            ps.setString(2, argUser.getLastName());
            ps.setInt(3, argUser.getAge());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getLong(1);
            } else {
                throw new SQLException("ID not generated");
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public User findUserById(Long userId) {

        try (Connection connection1 = CustomConnector.getConnection(CustomDataSource.PropertiesUtil.getByKey("postgres.url"), CustomDataSource.PropertiesUtil.getByKey("postgres.name"), CustomDataSource.PropertiesUtil.getByKey("postgres.password"))) {

            PreparedStatement ps = connection1.prepareStatement(findUserByIdSQL);
            ps.setLong(1, userId);
            ResultSet resultSet = ps.executeQuery();
            User user = null;

            while (resultSet.next()) {
                user = new User(resultSet.getLong("ID"), resultSet.getString("FIRST_NAME"), resultSet.getString("LAST_NAME"), resultSet.getInt("AGE")

                );
            }
            return user;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public User findUserByName(String userName) {

        try (Connection connection1 = CustomConnector.getConnection(CustomDataSource.PropertiesUtil.getByKey("postgres.url"), CustomDataSource.PropertiesUtil.getByKey("postgres.name"), CustomDataSource.PropertiesUtil.getByKey("postgres.password"))) {

            PreparedStatement ps = connection1.prepareStatement(findUserByNameSQL);
            ps.setString(1, userName);
            ResultSet resultSet = ps.executeQuery();
            User user = null;

            while (resultSet.next()) {
                user = new User(resultSet.getLong("id"), resultSet.getString("FIRST_NAME"), resultSet.getString("LAST_NAME"), resultSet.getInt("AGE")

                );
            }
            return user;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<User> findAllUser() {
        try (Connection connection1 = CustomConnector.getConnection(CustomDataSource.PropertiesUtil.getByKey("postgres.url"), CustomDataSource.PropertiesUtil.getByKey("postgres.name"), CustomDataSource.PropertiesUtil.getByKey("postgres.password"))) {

            PreparedStatement ps = connection1.prepareStatement(findAllUserSQL);
            ResultSet resultSet = ps.executeQuery();
            User user = null;
            ArrayList<User> arr = new ArrayList<>();
            while (resultSet.next()) {
                user = new User(resultSet.getLong("ID"), resultSet.getString("FIRST_NAME"), resultSet.getString("LAST_NAME"), resultSet.getInt("AGE")

                );
                arr.add(user);
            }
            return arr;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(User argUser) {

        try (Connection connection1 = CustomConnector.getConnection(CustomDataSource.PropertiesUtil.getByKey("postgres.url"), CustomDataSource.PropertiesUtil.getByKey("postgres.name"), CustomDataSource.PropertiesUtil.getByKey("postgres.password"))) {

            PreparedStatement ps = connection1.prepareStatement(updateUserSQL);

            ps.setString(1, argUser.getFirstName());
            ps.setString(2, argUser.getLastName());
            ps.setInt(3, argUser.getAge());
            ps.setLong(4, argUser.getId());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteUser(Long userId) {
        try (Connection connection1 = CustomConnector.getConnection(CustomDataSource.PropertiesUtil.getByKey("postgres.url"), CustomDataSource.PropertiesUtil.getByKey("postgres.name"), CustomDataSource.PropertiesUtil.getByKey("postgres.password"))) {

            PreparedStatement ps = connection1.prepareStatement(deleteUser);
            ps.setLong(1, userId);
            ResultSet resultSet = ps.executeQuery();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
