<%-- 
    Document   : view-agenda
    Created on : Mar 16, 2025, 4:43:53 PM
    Author     : LTSon
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Leave.DBUtil, Leave.Leave, Leave.User, java.util.*, java.text.SimpleDateFormat" %>
<% 
    if (session.getAttribute("user_id") == null) {
        response.sendRedirect("login.html");
        return;
    }

    String role = (String) session.getAttribute("role");
    if (!"manager".equals(role)) {
        response.sendRedirect("dashboard.jsp");
        return;
    }

    String fullName = (String) session.getAttribute("full_name");
    ArrayList<Leave> employeeLeaves = new ArrayList<>();
    try {
        ArrayList<Leave> allLeaves = DBUtil.getLeaveDB().list();
        for (Leave leave : allLeaves) {
            User user = DBUtil.getUserDB().get(leave.getUserId());
            if (user != null && "employee".equalsIgnoreCase(user.getRole())) {
                employeeLeaves.add(leave);
            }
        }
        System.out.println("Employee leaves fetched: " + employeeLeaves.size() + " records");
    } catch (Exception e) {
        System.out.println("Error fetching employee leaves in view-agenda.jsp: " + e.getMessage());
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách tất cả đơn nghỉ phép</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            margin: 0; 
            background-color: #f0f0f0; 
            display: flex; 
            justify-content: center; 
            align-items: center; 
            min-height: 100vh; 
        }
        .container { 
            max-width: 1200px; 
            padding: 20px; 
            background-color: white; 
            border-radius: 8px; 
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); 
            text-align: center; 
        }
        .header { 
            margin-bottom: 20px; 
        }
        h1 { 
            font-size: 28px; 
            margin: 0; 
        }
        h2 { 
            font-size: 24px; 
            margin: 20px 0; 
        }
        p { 
            font-size: 16px; 
            color: #555; 
        }
        table { 
            width: 100%; 
            border-collapse: collapse; 
            margin-top: 20px; 
        }
        th, td { 
            border: 1px solid #ddd; 
            padding: 8px; 
            text-align: center; 
        }
        th { 
            background-color: #f2f2f2; 
        }
        .no-data { 
            text-align: center; 
            padding: 20px; 
            color: #888; 
        }
        .back-link { 
            display: block; 
            text-align: center; 
            margin-top: 20px; 
            color: #4CAF50; 
            text-decoration: none; 
        }
        .action-btn { 
            color: #4CAF50; 
            text-decoration: none; 
        }
        .action-btn.reject { 
            color: red; 
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>X Company</h1>
            <p>Xin chào, <%= fullName %> (Vai trò: <%= role %>)</p>
        </div>
        <h2>Danh sách tất cả đơn nghỉ phép</h2>
        <% if (employeeLeaves == null || employeeLeaves.isEmpty()) { %>
            <p class="no-data">Chưa có đơn nào từ nhân viên.</p>
        <% } else { %>
            <table>
                <tr>
                    <th>Tên nhân viên</th>
                    <th>Ngày bắt đầu</th>
                    <th>Loại nghỉ</th>
                    <th>Thời gian</th>
                    <th>Lý do</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
                <% 
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    for (Leave leave : employeeLeaves) {
                        User user = DBUtil.getUserDB().get(leave.getUserId());
                        String employeeName = (user != null && user.getFullName() != null) ? user.getFullName() : "Không xác định";
                %>
                    <tr>
                        <td><%= employeeName %></td>
                        <td><%= sdf.format(leave.getStartDate()) %></td>
                        <td><%= leave.getLeaveType() != null ? leave.getLeaveType() : "Chưa xác định" %></td>
                        <td><%= sdf.format(leave.getEndDate()) %></td>
                        <td><%= leave.getReason() != null ? leave.getReason() : "Không có lý do" %></td>
                        <td>
                            <%= 
                                leave.getStatus() != null ? 
                                (leave.getStatus().equals("Pending") ? "Đang chờ" : 
                                 leave.getStatus().equals("Approved") ? "Đã duyệt" : 
                                 leave.getStatus().equals("Rejected") ? "Bị từ chối" : "Chưa xử lý") : "Chưa xử lý" 
                            %>
                        </td>
                        <td>
                            <% if ("Pending".equals(leave.getStatus())) { %>
                                <a href="approveLeave?id=<%= leave.getId() %>" class="action-btn">Duyệt</a> | 
                                <a href="rejectLeave?id=<%= leave.getId() %>" class="action-btn reject">Từ chối</a>
                            <% } %>
                        </td>
                    </tr>
                <% } %>
            </table>
        <% } %>
        <a href="dashboard.jsp" class="back-link">Quay lại trang chủ</a>
    </div>
</body>
</html>