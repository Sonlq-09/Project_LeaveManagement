/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Leave;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LTSon
 */

public class LeaveDBContext extends DBContext<Leave> {
    @Override
    public ArrayList<Leave> list() {
        ArrayList<Leave> leaves = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn == null) {
                Logger.getLogger(LeaveDBContext.class.getName()).log(Level.SEVERE, "Không thể tạo kết nối cho list()");
                return leaves;
            }

            String sql = "SELECT Id, UserId, LeaveType, StartDate, EndDate, Reason, Status, RejectReason, SubmittedAt FROM Leaves";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Leave leave = new Leave();
                leave.setId(rs.getInt("Id"));
                leave.setUserId(rs.getInt("UserId"));
                leave.setLeaveType(rs.getString("LeaveType"));
                leave.setStartDate(rs.getDate("StartDate"));
                leave.setEndDate(rs.getDate("EndDate"));
                leave.setReason(rs.getString("Reason"));
                leave.setStatus(rs.getString("Status"));
                leave.setRejectReason(rs.getString("RejectReason"));
                leave.setSubmittedAt(rs.getTimestamp("SubmittedAt"));
                leaves.add(leave);
            }
        } catch (SQLException e) {
            Logger.getLogger(LeaveDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi truy vấn danh sách nghỉ phép: ", e);
            System.out.println("SQLException in list: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        return leaves;
    }

    @Override
    public Leave get(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn == null) {
                Logger.getLogger(LeaveDBContext.class.getName()).log(Level.SEVERE, "Không thể tạo kết nối cho get(" + id + ")");
                return null;
            }

            String sql = "SELECT Id, UserId, LeaveType, StartDate, EndDate, Reason, Status, RejectReason, SubmittedAt FROM Leaves WHERE Id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Leave leave = new Leave();
                leave.setId(rs.getInt("Id"));
                leave.setUserId(rs.getInt("UserId"));
                leave.setLeaveType(rs.getString("LeaveType"));
                leave.setStartDate(rs.getDate("StartDate"));
                leave.setEndDate(rs.getDate("EndDate"));
                leave.setReason(rs.getString("Reason"));
                leave.setStatus(rs.getString("Status"));
                leave.setRejectReason(rs.getString("RejectReason"));
                leave.setSubmittedAt(rs.getTimestamp("SubmittedAt"));
                return leave;
            }
        } catch (SQLException e) {
            Logger.getLogger(LeaveDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi lấy thông tin nghỉ phép với ID " + id + ": ", e);
        } finally {
            closeResources(conn, stmt, rs);
        }
        return null;
    }

    @Override
    public void insert(Leave model) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            if (conn == null) {
                Logger.getLogger(LeaveDBContext.class.getName()).log(Level.SEVERE, "Không thể tạo kết nối cho insert()");
                throw new SQLException("Không thể tạo kết nối đến database");
            }

            System.out.println("Inserting leave with data: UserId=" + model.getUserId() + 
                              ", LeaveType=" + model.getLeaveType() + 
                              ", StartDate=" + model.getStartDate() + 
                              ", EndDate=" + model.getEndDate() + 
                              ", Reason=" + model.getReason() + 
                              ", Status=" + model.getStatus() + 
                              ", SubmittedAt=" + model.getSubmittedAt());

            String sql = "INSERT INTO Leaves (UserId, LeaveType, StartDate, EndDate, Reason, Status, SubmittedAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, model.getUserId());
            stmt.setString(2, model.getLeaveType());
            stmt.setDate(3, new java.sql.Date(model.getStartDate().getTime()));
            stmt.setDate(4, new java.sql.Date(model.getEndDate().getTime()));
            stmt.setString(5, model.getReason());
            stmt.setString(6, model.getStatus());
            stmt.setTimestamp(7, new java.sql.Timestamp(model.getSubmittedAt().getTime()));
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected by insert: " + rowsAffected);
        } catch (SQLException e) {
            Logger.getLogger(LeaveDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi chèn thông tin nghỉ phép: ", e);
            System.out.println("SQLException in insert: " + e.getMessage());
            throw new RuntimeException("Lỗi khi chèn thông tin nghỉ phép: " + e.getMessage(), e);
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    @Override
    public void update(Leave model) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            if (conn == null) {
                Logger.getLogger(LeaveDBContext.class.getName()).log(Level.SEVERE, "Không thể tạo kết nối cho update(" + model.getId() + ")");
                return;
            }

            String sql = "UPDATE Leaves SET UserId = ?, LeaveType = ?, StartDate = ?, EndDate = ?, Reason = ?, Status = ?, RejectReason = ?, SubmittedAt = ? WHERE Id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, model.getUserId());
            stmt.setString(2, model.getLeaveType());
            stmt.setDate(3, new java.sql.Date(model.getStartDate().getTime()));
            stmt.setDate(4, new java.sql.Date(model.getEndDate().getTime()));
            stmt.setString(5, model.getReason());
            stmt.setString(6, model.getStatus());
            stmt.setString(7, model.getRejectReason());
            stmt.setTimestamp(8, model.getSubmittedAt() != null ? new java.sql.Timestamp(model.getSubmittedAt().getTime()) : null);
            stmt.setInt(9, model.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(LeaveDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi cập nhật thông tin nghỉ phép với ID " + model.getId() + ": ", e);
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    @Override
    public void delete(Leave model) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            if (conn == null) {
                Logger.getLogger(LeaveDBContext.class.getName()).log(Level.SEVERE, "Không thể tạo kết nối cho delete(" + model.getId() + ")");
                return;
            }

            String sql = "DELETE FROM Leaves WHERE Id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, model.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(LeaveDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi xóa thông tin nghỉ phép với ID " + model.getId() + ": ", e);
        } finally {
            closeResources(conn, stmt, null);
        }
    }

    /**
     * Lấy danh sách các đơn nghỉ phép của một người dùng cụ thể dựa trên UserId.
     * @param userId ID của người dùng.
     * @return ArrayList<Leave> chứa danh sách các đơn nghỉ phép.
     */
    public ArrayList<Leave> listByUserId(int userId) {
        ArrayList<Leave> leaves = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn == null) {
                Logger.getLogger(LeaveDBContext.class.getName()).log(Level.SEVERE, "Không thể tạo kết nối cho listByUserId(" + userId + ")");
                return leaves;
            }

            String sql = "SELECT Id, UserId, LeaveType, StartDate, EndDate, Reason, Status, RejectReason, SubmittedAt FROM Leaves WHERE UserId = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Leave leave = new Leave();
                leave.setId(rs.getInt("Id"));
                leave.setUserId(rs.getInt("UserId"));
                leave.setLeaveType(rs.getString("LeaveType"));
                leave.setStartDate(rs.getDate("StartDate"));
                leave.setEndDate(rs.getDate("EndDate"));
                leave.setReason(rs.getString("Reason"));
                leave.setStatus(rs.getString("Status"));
                leave.setRejectReason(rs.getString("RejectReason"));
                leave.setSubmittedAt(rs.getTimestamp("SubmittedAt"));
                leaves.add(leave);
            }
        } catch (SQLException e) {
            Logger.getLogger(LeaveDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi truy vấn danh sách nghỉ phép cho UserId " + userId + ": ", e);
            System.out.println("SQLException in listByUserId: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        return leaves;
    }

    private void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            Logger.getLogger(LeaveDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi đóng tài nguyên: ", e);
        }
    }
}