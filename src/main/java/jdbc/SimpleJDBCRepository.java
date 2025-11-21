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

    // FIX 1: Use escaped quotes (\"...\") to match H2 schema case-sensitivity
    private static final String createUserSQL = "INSERT INTO myusers (\"first_name\", \"last_name\", \"age\") VALUES (?,?,?)";
    private static final String updateUserSQL = "UPDATE myusers SET \"first_name\"=?, \"last_name\"=?, \"age\"=? WHERE \"id\" = ?";
    private static final String deleteUser = "DELETE FROM myusers WHERE \"id\" = ?";
    private static final String findUserByIdSQL = "SELECT * FROM myusers WHERE \"id\" = ?";
    private static final String findUserByNameSQL = "SELECT * FROM myusers WHERE \"first_name\" = ?";
    private static final String findAllUserSQL = "SELECT * FROM myusers";

    public Long createUser(User argUser) {
        try (Connection connection1 = CustomConnector.getConnection(CustomDataSource.PropertiesUtil.getByKey("h2.url"), CustomDataSource.PropertiesUtil.getByKey("h2.name"), CustomDataSource.PropertiesUtil.getByKey("h2.password"))) {

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
        try (Connection connection1 = CustomConnector.getConnection(CustomDataSource.PropertiesUtil.getByKey("h2.url"), CustomDataSource.PropertiesUtil.getByKey("h2.name"), CustomDataSource.PropertiesUtil.getByKey("h2.password"))) {

            PreparedStatement ps = connection1.prepareStatement(findUserByIdSQL);
            ps.setLong(1, userId);
            ResultSet resultSet = ps.executeQuery();
            User user = null;

            while (resultSet.next()) {
                // Use exact column names if necessary, or standard getters usually work
                user = User.builder()
                        .id(resultSet.getLong("id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .age(resultSet.getInt("age"))
                        .build();
            }
            return user;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User findUserByName(String userName) {
        try (Connection connection1 = CustomConnector.getConnection(CustomDataSource.PropertiesUtil.getByKey("h2.url"), CustomDataSource.PropertiesUtil.getByKey("h2.name"), CustomDataSource.PropertiesUtil.getByKey("h2.password"))) {

            PreparedStatement ps = connection1.prepareStatement(findUserByNameSQL);
            ps.setString(1, userName);
            ResultSet resultSet = ps.executeQuery();
            User user = null;

            while (resultSet.next()) {
                user = User.builder()
                        .id(resultSet.getLong("id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .age(resultSet.getInt("age"))
                        .build();
            }
            return user;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> findAllUser() {
        try (Connection connection1 = CustomConnector.getConnection(CustomDataSource.PropertiesUtil.getByKey("h2.url"), CustomDataSource.PropertiesUtil.getByKey("h2.name"), CustomDataSource.PropertiesUtil.getByKey("h2.password"))) {

            PreparedStatement ps = connection1.prepareStatement(findAllUserSQL);
            ResultSet resultSet = ps.executeQuery();
            ArrayList<User> arr = new ArrayList<>();
            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getLong("id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .age(resultSet.getInt("age"))
                        .build();
                arr.add(user);
            }
            return arr;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(User argUser) {
        try (Connection connection1 = CustomConnector.getConnection(CustomDataSource.PropertiesUtil.getByKey("h2.url"), CustomDataSource.PropertiesUtil.getByKey("h2.name"), CustomDataSource.PropertiesUtil.getByKey("h2.password"))) {

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
        try (Connection connection1 = CustomConnector.getConnection(CustomDataSource.PropertiesUtil.getByKey("h2.url"), CustomDataSource.PropertiesUtil.getByKey("h2.name"), CustomDataSource.PropertiesUtil.getByKey("h2.password"))) {

            PreparedStatement ps = connection1.prepareStatement(deleteUser);
            ps.setLong(1, userId);
            // FIX 2: Use executeUpdate() for DELETE
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
