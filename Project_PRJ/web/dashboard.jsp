<%-- 
    Document   : dashboard
    Created on : Mar 16, 2025, 4:20:02 PM
    Author     : LTSon
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Leave.DBUtil, Leave.Leave, java.util.*, java.text.SimpleDateFormat" %>
<% 
    if (session.getAttribute("user_id") == null) {
        response.sendRedirect("login.html");
        return;
    }

    String fullName = (String) session.getAttribute("full_name");
    String role = (String) session.getAttribute("role");
    int userId = (Integer) session.getAttribute("user_id");

    ArrayList<Leave> leaves = new ArrayList<>();
    if ("employee".equals(role) || "manager".equals(role)) {
        try {
            leaves = DBUtil.getLeaveDB().listByUserId(userId);
            System.out.println("Leaves fetched for userId " + userId + ": " + leaves.size() + " records");
        } catch (Exception e) {
            System.out.println("Error fetching leaves in dashboard.jsp: " + e.getMessage());
        }
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Leave Management</title>
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
        .success { 
            color: green; 
            text-align: center; 
            margin-bottom: 20px; 
        }
        .create-leave, .view-agenda, .manage-leaves { 
            display: inline-block; 
            margin: 10px; 
            padding: 10px 20px; 
            background-color: #4CAF50; 
            color: white; 
            text-decoration: none; 
            border-radius: 4px; 
        }
        .create-leave:hover, .view-agenda:hover, .manage-leaves:hover { 
            background-color: #45a049; 
        }
        .logout { 
            display: block; 
            text-align: right; 
            color: red; 
            text-decoration: none; 
            margin-top: 20px; 
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>X Company</h1>
            <p>Xin chào, <%= fullName %> (Vai trò: <%= role %>)</p>
        </div>
        <% if ("employee".equals(role) || "manager".equals(role)) { %>
            <a href="create-leave.jsp" class="create-leave">Tạo đơn nghỉ phép mới</a>
        <% } %>
        <% if ("manager".equals(role) || "director".equals(role)) { %>
            <a href="agenda.jsp" class="view-agenda">Xem agenda</a>
        <% } %>
        <% if ("manager".equals(role)) { %>
            <a href="manage-leave.jsp" class="manage-leaves">Quản lý đơn nghỉ phép</a>
        <% } else if ("director".equals(role)) { %>
            <a href="director-leaves.jsp" class="manage-leaves">Quản lý đơn nghỉ phép của Manager</a>
        <% } %>
        <% if ("employee".equals(role) || "manager".equals(role)) { %>
            <h2>Các đơn đã nộp gần đây</h2>
            <% if ("true".equals(request.getParameter("success"))) { %>
                <p class="success">Tạo đơn nghỉ phép thành công!</p>
            <% } %>
            <% if (leaves == null || leaves.isEmpty()) { %>
                <p class="no-data">Chưa có đơn nào.</p>
            <% } else { %>
                <table>
                    <tr>
                        <th>Ngày bắt đầu</th>
                        <th>Loại nghỉ</th>
                        <th>Thời gian</th>
                        <th>Lý do</th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                    </tr>
                    <% 
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        for (Leave leave : leaves) {
                    %>
                        <tr>
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
                            <td><a href="deleteLeave?id=<%= leave.getId() %>">Xóa</a></td>
                        </tr>
                    <% } %>
                </table>
            <% } %>
        <% } %>
        <a href="logout" class="logout">Đăng xuất</a>
    </div>
</body>
</html>