/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Leave;

/**
 *
 * @author LTSon
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Class UserDBContext quản lý các thao tác (Create, Read, Update, Delete) với bảng Users trong database.
 */
public class UserDBContext extends DBContext<User> {

    /*
    Lấy danh sách tất cả người dùng từ bảng Users.
    @return ArrayList<User> chứa danh sách người dùng.
     */
    @Override
    public ArrayList<User> list() {
        ArrayList<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn == null) {
                Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Không thể tạo kết nối cho list()");
                return users;
            }

            String sql = "SELECT Id, Username, Password, FullName, Role FROM Users";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                    rs.getInt("Id"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getString("FullName"),
                    rs.getString("Role")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi truy vấn danh sách người dùng: ", e);
        } finally {
            closeResources(conn, stmt, rs);
        }
        return users;
    }

    /*
    Lấy thông tin người dùng dựa trên ID.
    @param id ID của người dùng cần lấy.
    @return Đối tượng User nếu tìm thấy, ngược lại trả về null.
     */
    @Override
    public User get(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn == null) {
                Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Không thể tạo kết nối cho get(" + id + ")");
                return null;
            }

            String sql = "SELECT Id, Username, Password, FullName, Role FROM Users WHERE Id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                    rs.getInt("Id"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getString("FullName"),
                    rs.getString("Role")
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi lấy người dùng với ID " + id + ": ", e);
        } finally {
            closeResources(conn, stmt, rs);
        }
        return null;
    }

    /*
    Thêm một người dùng mới vào bảng Users.
    @param model Đối tượng User cần thêm.
     */
    @Override
    public void insert(User model) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            if (conn == null) {
                Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Không thể tạo kết nối cho insert()");
                return;
            }

            String sql = "INSERT INTO Users (Username, Password, FullName, Role) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, model.getUsername());
            stmt.setString(2, model.getPassword());
            stmt.setString(3, model.getFullName());
            stmt.setString(4, model.getRole());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi chèn người dùng: ", e);
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /*
    Cập nhật thông tin của một người dùng trong bảng Users.
    @param model Đối tượng User chứa thông tin cần cập nhật.
     */
    @Override
    public void update(User model) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            if (conn == null) {
                Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Không thể tạo kết nối cho update(" + model.getId() + ")");
                return;
            }

            String sql = "UPDATE Users SET Username = ?, Password = ?, FullName = ?, Role = ? WHERE Id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, model.getUsername());
            stmt.setString(2, model.getPassword());
            stmt.setString(3, model.getFullName());
            stmt.setString(4, model.getRole());
            stmt.setInt(5, model.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi cập nhật người dùng với ID " + model.getId() + ": ", e);
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /*
    Xóa một người dùng khỏi bảng Users dựa trên ID.
    @param model Đối tượng User cần xóa.
     */
    @Override
    public void delete(User model) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            if (conn == null) {
                Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Không thể tạo kết nối cho delete(" + model.getId() + ")");
                return;
            }

            String sql = "DELETE FROM Users WHERE Id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, model.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi xóa người dùng với ID " + model.getId() + ": ", e);
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /*
    Xử lý đăng nhập người dùng dựa trên username và password.
    @return Đối tượng User nếu đăng nhập thành công, ngược lại trả về null.
     */
    public User login(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Tên người dùng hoặc mật khẩu không hợp lệ: username=" + username + ", password=" + password);
            return null;
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn == null) {
                Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Không thể tạo kết nối cho login(" + username + ")");
                return null;
            }

            String sql = "SELECT Id, FullName, Role FROM Users WHERE Username = ? AND Password = ?";
            stmt = conn.prepareStatement(sql);
            System.out.println("Setting username parameter: " + username);
            stmt.setString(1, username);
            System.out.println("Setting password parameter: " + password);
            stmt.setString(2, password);

            rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Found user in database - ID: " + rs.getInt("Id"));
                return new User(
                    rs.getInt("Id"),
                    username,
                    password,
                    rs.getString("FullName"),
                    rs.getString("Role")
                );
            } else {
                System.out.println("No user found for Username: " + username);
            }
        } catch (SQLException e) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi đăng nhập với username " + username + ": ", e);
            System.out.println("SQLException in login: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        return null;
    }

    /*
     Đóng các tài nguyên database (Connection, PreparedStatement, ResultSet) để tránh vấn đề rò rỉ.
     */
    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi đóng tài nguyên: ", e);
        }
    }
}
