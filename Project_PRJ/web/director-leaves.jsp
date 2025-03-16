<%-- 
    Document   : director-leaves
    Created on : Mar 16, 2025, 4:41:47 PM
    Author     : LTSon
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Leave.DBUtil, Leave.Leave, Leave.User, java.util.*, java.text.SimpleDateFormat" %>
<% 
    if (session.getAttribute("user_id") == null || !"director".equalsIgnoreCase((String) session.getAttribute("role"))) {
        response.sendRedirect("login.html");
        return;
    }

    String fullName = (String) session.getAttribute("full_name");
    String role = (String) session.getAttribute("role");

    ArrayList<Leave> managerLeaves = new ArrayList<>();
    try {
        ArrayList<Leave> allLeaves = DBUtil.getLeaveDB().list();
        for (Leave leave : allLeaves) {
            User user = DBUtil.getUserDB().get(leave.getUserId());
            if (user != null && "manager".equalsIgnoreCase(user.getRole())) {
                managerLeaves.add(leave);
            }
        }
    } catch (Exception e) {
        System.out.println("Error fetching leaves in director-leaves.jsp: " + e.getMessage());
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý đơn nghỉ phép của Manager</title>
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
            max-width: 1000px; 
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
        table { 
            width: 100%; 
            border-collapse: collapse; 
            margin-top: 20px; 
        }
        th, td { 
            border: 1px solid #ddd; 
            padding: 10px; 
            text-align: center; 
        }
        th { 
            background-color: #f2f2f2; 
        }
        .action-buttons form { 
            display: inline; 
        }
        .action-buttons button { 
            padding: 5px 10px; 
            border: none; 
            border-radius: 4px; 
            cursor: pointer; 
            margin-right: 5px; 
        }
        .approve-btn { 
            background-color: #4CAF50; 
            color: white; 
        }
        .approve-btn:hover { 
            background-color: #45a049; 
        }
        .reject-btn { 
            background-color: #ff4444; 
            color: white; 
        }
        .reject-btn:hover { 
            background-color: #cc0000; 
        }
        .back-link { 
            display: block; 
            text-align: center; 
            margin-top: 20px; 
            color: #4CAF50; 
            text-decoration: none; 
        }
        .reject-reason { 
            display: none; 
            margin-top: 5px; 
        }
        .reject-reason input { 
            width: 70%; 
            padding: 5px; 
            border: 1px solid #ccc; 
            border-radius: 4px; 
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>X Company</h1>
            <p>Xin chào, <%= fullName %> (Vai trò: <%= role %>)</p>
        </div>
        <h2>Quản lý đơn nghỉ phép của Manager</h2>
        <table>
            <tr>
                <th>Tên Manager</th>
                <th>Loại nghỉ</th>
                <th>Thời gian</th>
                <th>Lý do</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
            </tr>
            <% 
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                for (Leave leave : managerLeaves) {
                    User user = DBUtil.getUserDB().get(leave.getUserId());
                    String managerName = (user != null && user.getFullName() != null) ? user.getFullName() : "Không xác định";
            %>
                <tr>
                    <td><%= managerName %></td>
                    <td><%= leave.getLeaveType() != null ? leave.getLeaveType() : "Chưa xác định" %></td>
                    <td><%= sdf.format(leave.getStartDate()) %> - <%= sdf.format(leave.getEndDate()) %></td>
                    <td><%= leave.getReason() != null ? leave.getReason() : "Không có lý do" %></td>
                    <td>
                        <%= 
                            leave.getStatus() != null ? 
                            (leave.getStatus().equals("Pending") ? "Đang chờ" : 
                             leave.getStatus().equals("Approved") ? "Đã duyệt" : 
                             leave.getStatus().equals("Rejected") ? "Bị từ chối" : "Chưa xử lý") : "Chưa xử lý" 
                        %>
                        <%= "Rejected".equals(leave.getStatus()) && leave.getRejectReason() != null ? " (" + leave.getRejectReason() + ")" : "" %>
                    </td>
                    <td class="action-buttons">
                        <% if ("Pending".equals(leave.getStatus())) { %>
                            <form action="manage-director-leave" method="post">
                                <input type="hidden" name="leave_id" value="<%= leave.getId() %>">
                                <input type="hidden" name="action" value="approve">
                                <button type="submit" class="approve-btn">Duyệt</button>
                            </form>
                            <button class="reject-btn" onclick="document.getElementById('reject-<%= leave.getId() %>').style.display='block'">Từ chối</button>
                            <div class="reject-reason" id="reject-<%= leave.getId() %>">
                                <form action="manage-director-leave" method="post">
                                    <input type="hidden" name="leave_id" value="<%= leave.getId() %>">
                                    <input type="hidden" name="action" value="reject">
                                    <input type="text" name="reject_reason" placeholder="Lý do từ chối" required>
                                    <button type="submit">Xác nhận</button>
                                </form>
                            </div>
                        <% } else { %>
                            Đã xử lý
                        <% } %>
                    </td>
                </tr>
            <% } %>
            <% if (managerLeaves.isEmpty()) { %>
                <tr><td colspan="6">Chưa có đơn nào từ Manager.</td></tr>
            <% } %>
        </table>
        <a href="dashboard.jsp" class="back-link">Quay lại trang chủ</a>
    </div>
</body>
</html>
