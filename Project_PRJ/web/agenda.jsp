<%-- 
    Document   : agenda
    Created on : Mar 16, 2025, 4:24:55 PM
    Author     : LTSon
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Leave.DBUtil, Leave.User, Leave.Leave, java.util.*, java.text.SimpleDateFormat" %>
<% 
    if (session.getAttribute("user_id") == null || !("manager".equalsIgnoreCase((String) session.getAttribute("role")) || "director".equalsIgnoreCase((String) session.getAttribute("role")))) {
        response.sendRedirect("login.html");
        return;
    }

    String fullName = (String) session.getAttribute("full_name");
    String role = (String) session.getAttribute("role");

    String startDateStr = request.getParameter("startDate");
    String endDateStr = request.getParameter("endDate");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM");
    sdf.setLenient(false);

    java.util.Date startDate = null;
    java.util.Date endDate = null;
    List<Date> dateRange = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<Leave> leaves = new ArrayList<>();

    try {
        users = DBUtil.getUserDB().list();
        leaves = DBUtil.getLeaveDB().list();
        System.out.println("Users fetched: " + users.size() + " records");
        System.out.println("Leaves fetched: " + leaves.size() + " records");
    } catch (Exception e) {
        System.out.println("Error fetching users or leaves in agenda.jsp: " + e.getMessage());
    }

    if (startDateStr != null && endDateStr != null && !startDateStr.isEmpty() && !endDateStr.isEmpty()) {
        try {
            startDate = sdf.parse(startDateStr);
            endDate = sdf.parse(endDateStr);

            if (endDate.before(startDate)) {
                throw new Exception("Ngày kết thúc phải sau ngày bắt đầu");
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            while (!cal.getTime().after(endDate)) {
                dateRange.add(cal.getTime());
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            System.out.println("Error parsing dates in agenda.jsp: " + e.getMessage());
            request.setAttribute("error", "Vui lòng chọn khoảng thời gian hợp lệ: " + e.getMessage());
        }
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agenda - Tình hình lao động</title>
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
        .form-group { 
            margin-bottom: 15px; 
        }
        label { 
            margin-right: 10px; 
            font-weight: bold; 
        }
        input[type="date"] { 
            padding: 5px; 
            border: 1px solid #ccc; 
            border-radius: 4px; 
        }
        button { 
            padding: 5px 15px; 
            background-color: #4CAF50; 
            color: white; 
            border: none; 
            border-radius: 4px; 
            cursor: pointer; 
        }
        button:hover { 
            background-color: #45a049; 
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
            font-size: 12px; 
        }
        .working { 
            background-color: #4CAF50; 
        }
        .on-leave { 
            background-color: #ff4444; 
        }
        .error { 
            color: red; 
            text-align: center; 
            margin-bottom: 20px; 
        }
        .back-link { 
            display: block; 
            text-align: center; 
            margin-top: 20px; 
            color: #4CAF50; 
            text-decoration: none; 
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>X Company</h1>
            <p>Xin chào, <%= fullName %> (Vai trò: <%= role %>)</p>
        </div>
        <h2>Agenda - Tình hình lao động</h2>
        <% if (request.getAttribute("error") != null) { %>
            <p class="error"><%= request.getAttribute("error") %></p>
        <% } %>
        <form action="agenda.jsp" method="get" onsubmit="return validateForm()">
            <div class="form-group">
                <label for="startDate">Từ ngày:</label>
                <input type="date" name="startDate" id="startDate" value="<%= startDateStr != null ? startDateStr : "" %>" required>
                <label for="endDate">Đến ngày:</label>
                <input type="date" name="endDate" id="endDate" value="<%= endDateStr != null ? endDateStr : "" %>" required>
                <button type="submit">Xem</button>
            </div>
        </form>

        <% if (startDate != null && endDate != null && !dateRange.isEmpty()) { %>
            <table>
                <thead>
                    <tr>
                        <th>Nhân viên</th>
                        <% for (Date date : dateRange) { %>
                            <th><%= sdfDisplay.format(date) %></th>
                        <% } %>
                    </tr>
                </thead>
                <tbody>
                    <% for (User user : users) { %>
                        <tr>
                            <td><%= user.getFullName() != null ? user.getFullName() : "Không xác định" %></td>
                            <% for (Date date : dateRange) { 
                                boolean onLeave = false;
                                for (Leave leave : leaves) {
                                    if (leave.getUserId() == user.getId() && 
                                        !date.before(leave.getStartDate()) && 
                                        !date.after(leave.getEndDate()) &&
                                        ("Approved".equals(leave.getStatus()) || "Pending".equals(leave.getStatus()))) {
                                        onLeave = true;
                                        break;
                                    }
                                }
                            %>
                                <td class="<%= onLeave ? "on-leave" : "working" %>"></td>
                            <% } %>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        <% } else { %>
            <p style="text-align: center;">Vui lòng chọn khoảng thời gian để xem tình hình lao động.</p>
        <% } %>

        <a href="dashboard.jsp" class="back-link">Quay lại trang chủ</a>
    </div>

    <script>
        function validateForm() {
            var startDateInput = document.getElementById("startDate").value;
            var endDateInput = document.getElementById("endDate").value;

            if (!startDateInput || !endDateInput) {
                alert("Vui lòng chọn cả ngày bắt đầu và ngày kết thúc!");
                return false;
            }

            var startDate = new Date(startDateInput);
            var endDate = new Date(endDateInput);
            var today = new Date();
            today.setHours(0, 0, 0, 0);

            if (isNaN(startDate.getTime()) || isNaN(endDate.getTime())) {
                alert("Ngày không hợp lệ!");
                return false;
            }

            if (endDate < startDate) {
                alert("Ngày kết thúc phải sau ngày bắt đầu!");
                return false;
            }

            return true;
        }
    </script>
</body>
</html>