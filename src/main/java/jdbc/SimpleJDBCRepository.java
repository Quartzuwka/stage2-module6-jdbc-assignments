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

    // ИСПРАВЛЕНИЕ: Инициализируем dataSource через Singleton по умолчанию.
    // Теперь, если тест создает new SimpleJDBCRepository(), это поле не будет null.
    private CustomDataSource dataSource = CustomDataSource.getInstance();

    // SQL запросы
    private static final String createUserSQL = "INSERT INTO myfirstdb.public.myusers (firstName, lastName, age) VALUES (?, ?, ?)";
    private static final String updateUserSQL = "UPDATE myfirstdb.public.myusers SET firstName=?, lastName=?, age=? WHERE id=?";
    private static final String deleteUserSQL = "DELETE FROM myfirstdb.public.myusers WHERE id=?";
    private static final String findUserByIdSQL = "SELECT * FROM myfirstdb.public.myusers WHERE id=?";
    private static final String findUserByNameSQL = "SELECT * FROM myfirstdb.public.myusers WHERE firstName || ' ' || lastName = ?";
    private static final String findAllUserSQL = "SELECT * FROM myfirstdb.public.myusers";

    // Поля Connection, PreparedStatement, Statement удалены, так как они должны быть локальными.

    public Long createUser(User user) {
        // Используем try-with-resources для автоматического закрытия ресурсов
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(createUserSQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            throw new SQLException("Creating user failed, no ID obtained.");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findUserById(Long userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(findUserByIdSQL)) {

            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User findUserByName(String userName) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(findUserByNameSQL)) {

            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> findAllUser() {
        List<User> users = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(findAllUserSQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void updateUser(User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(updateUserSQL)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.setLong(4, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(Long userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteUserSQL)) {

            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Вспомогательный метод для маппинга (убирает дублирование кода)
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String firstName = rs.getString("firstName");
        String lastName = rs.getString("lastName");
        int age = rs.getInt("age");
        return new User(id, firstName, lastName, age);
    }
}
