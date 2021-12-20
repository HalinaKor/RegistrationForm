package dao;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Dao {

    private String databaseUrl ="jdbc:mysql://localhost:3306/user?serverTimezone=Europe/Moscow";
    private String databaseLogin ="root";
    private String databasePassword ="04051995";
    private static final String SQL_SELECT = "SELECT * FROM users WHERE Login LIKE ? AND Password LIKE ?";
        private static final String SQL_SELECT_USER = "SELECT * FROM users";
        private static final String SQL_INSERT = "INSERT INTO users (Name, Login, Password, Role) Value (?,?,?,?)";
        private static final String SQL_LOGIN_CHECK = "SELECT * FROM users WHERE Login LIKE ?";
        private static final String SQL_DELETE = "DELETE FROM users WHERE Id LIKE ?";
        private static final String SQL_SELECT_USER_BY_ID = "SELECT Id, Name, Login, Password, Role FROM" +
                " users WHERE Id LIKE ?";
    private static final String SQL_UPDATE_USER = "UPDATE users set Name = ?, Login=?, Password =? WHERE Id = ?";


        private static Connection connection;
        private static PreparedStatement preparedStatement;
        private static ResultSet resultSet1;

        // creating the connection with our Database
        public boolean connect() {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
               connection = DriverManager.getConnection(databaseUrl, databaseLogin, databasePassword);
                System.out.println("Connected to DB successfully");
                return true;
            } catch (SQLException e) {
                System.out.println("Connection to DB is failed");
                e.printStackTrace();
                return false;
            } catch (ClassNotFoundException e) {
                System.out.println("Connection to DB is failed");
                e.printStackTrace();
                return false;
            }
        }

    /*    public boolean disconnect()  {
            try {
                connection.close();
                System.out.println("Disconnected from DB");
                return true;
            } catch (NullPointerException | SQLException e) {
                System.out.println("Disconnection failure");
                return false;
            }
        }*/


       /* public User singIn(String login, String password) {
            try {
                preparedStatement = connection.prepareStatement(SQL_SELECT);
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, password);
                resultSet1 = preparedStatement.executeQuery();

                while (resultSet1.next()){
                    int id = resultSet1.getInt("id");
                    String name = resultSet1.getString("name");
                    String role = resultSet1.getString("role");

                    User obj = new User();
                    obj.setId(id);
                    obj.setLogin(login);
                    obj.setPassword(password);
                    obj.setRole(role);
                    obj.setName(name);

                    return obj;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }*/

// inserting users into the Database
        public void register(User user) {
            try {
                /*connect();*/
                System.out.println("User is checked");
                preparedStatement = connection.prepareStatement(SQL_INSERT);
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getLogin());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setString(4, "user");

                preparedStatement.executeUpdate();
                System.out.println("User added successfully");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    // updating users
    public void updateUser(User user) {
        try {
            connect();

            preparedStatement = connection.prepareStatement(SQL_UPDATE_USER);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getId());

            preparedStatement.executeUpdate();
            System.out.println("User updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // deleting user by ID
    public void delete(Integer id) {
        try {
            connect();
            preparedStatement = connection.prepareStatement(SQL_DELETE);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("User deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // selecting user by ID
    public User selectUserByID (Integer id) {
        connect();
       /* List<User> result1 = new ArrayList<>();*/
        User user = null;
        try {
            preparedStatement = connection.prepareStatement(SQL_SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            System.out.println("User is selected");
            resultSet1 = preparedStatement.executeQuery();

            while (resultSet1.next()) {

                String name = resultSet1.getString("name");
                String login = resultSet1.getString("login");
                String password = resultSet1.getString("password");
                String role = resultSet1.getString("role");

                user = new User(id, login, name, password);


                /*user.setId(id);
                user.setLogin(login);
                user.setPassword(password);
                user.setRole(role);
                user.setName(name);

                result1.add(user);*/
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // selecting all users
        public List<User> select(){
           connect();
            List<User> result1 = new ArrayList<>();
            try {
                preparedStatement =connection.prepareStatement(SQL_SELECT_USER);
                resultSet1 = preparedStatement.executeQuery();

                while (resultSet1.next()){
                    int id = resultSet1.getInt("id");
                    String name = resultSet1.getString("name");
                    String login = resultSet1.getString("login");
                    String password = resultSet1.getString("password");
                    String role = resultSet1.getString("role");

                    User obj = new User();
                    obj.setId(id);
                    obj.setLogin(login);
                    obj.setPassword(password);
                    obj.setRole(role);
                    obj.setName(name);

                    result1.add(obj);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result1;
        }


        public User loginCheck(String login) {
            try {
                connect();
                preparedStatement = connection.prepareStatement(SQL_LOGIN_CHECK);
                preparedStatement.setString(1, login);

                resultSet1 = preparedStatement.executeQuery();

                while (resultSet1.next()){
                    int id = resultSet1.getInt("id");
                    String name = resultSet1.getString("name");
                    String role = resultSet1.getString("role");
                    String password = resultSet1.getString("password");

                    User obj = new User();
                    obj.setId(id);
                    obj.setLogin(login);
                    obj.setPassword(password);
                    obj.setRole(role);
                    obj.setName(name);

                    return obj;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

