<%-- 
    Document   : create-leave
    Created on : Mar 16, 2025, 4:34:32 PM
    Author     : LTSon
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<% 
    if (session.getAttribute("user_id") == null) {
        response.sendRedirect("login.html");
        return;
    }
    String fullName = (String) session.getAttribute("full_name");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tạo đơn xin nghỉ phép</title>
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
            max-width: 600px; 
            padding: 20px; 
            background-color: white; 
            border-radius: 8px; 
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); 
        }
        .header { 
            text-align: center; 
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
            display: block; 
            margin-bottom: 5px; 
            font-weight: bold; 
        }
        input[type="text"], input[type="date"], select { 
            width: 100%; 
            padding: 10px; 
            border: 1px solid #ccc; 
            border-radius: 4px; 
            box-sizing: border-box; 
        }
        button { 
            width: 100%; 
            padding: 10px; 
            background-color: #4CAF50; 
            color: white; 
            border: none; 
            border-radius: 4px; 
            cursor: pointer; 
        }
        button:hover { 
            background-color: #45a049; 
        }
        .back-link { 
            display: block; 
            text-align: center; 
            margin-top: 20px; 
            color: #4CAF50; 
            text-decoration: none; 
        }
        .error { 
            color: red; 
            display: none; 
            margin-top: 10px; 
            text-align: center; 
        }
        .server-error { 
            color: red; 
            text-align: center; 
            margin-bottom: 20px; 
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>X Company</h1>
            <p>Xin chào, <%= fullName %></p>
        </div>
        <h2>Tạo đơn xin nghỉ phép</h2>
        <% if ("true".equals(request.getParameter("error"))) { %>
            <p class="server-error">
                <%= request.getParameter("message") != null ? request.getParameter("message") : "Lỗi không xác định" %>
            </p>
        <% } %>
        <form action="create-leave" method="post" accept-charset="UTF-8" onsubmit="return validateForm()">
            <div class="form-group">
                <label for="leaveType">Loại nghỉ phép:</label>
                <select name="leaveType" id="leaveType" required>
                    <option value="Nghỉ có phép">Nghỉ có phép</option>
                    <option value="Nghỉ ốm">Nghỉ ốm</option>
                    <option value="Nghỉ không lương">Nghỉ không lương</option>
                </select>
            </div>
            <div class="form-group">
                <label for="startDate">Ngày bắt đầu:</label>
                <input type="date" name="startDate" id="startDate" required>
            </div>
            <div class="form-group">
                <label for="endDate">Ngày kết thúc:</label>
                <input type="date" name="endDate" id="endDate" required>
            </div>
            <div class="form-group">
                <label for="reason">Lý do:</label>
                <input type="text" name="reason" id="reason" required maxlength="200" placeholder="Tối đa 200 ký tự">
            </div>
            <button type="submit">Gửi đơn</button>
        </form>
        <a href="dashboard.jsp" class="back-link">Quay lại trang chủ</a>
        <div id="errorMessage" class="error"></div>
    </div>

    <script>
        function validateForm() {
            var startDateInput = document.getElementById("startDate").value;
            var endDateInput = document.getElementById("endDate").value;
            var reasonInput = document.getElementById("reason").value.trim();
            var errorMessage = document.getElementById("errorMessage");

        if (!startDateInput || !endDateInput || !reasonInput) {
                errorMessage.textContent = "Vui lòng điền đầy đủ thông tin!";
                errorMessage.style.display = "block";
                return false;
            }

            if (reasonInput.length > 200) {
                errorMessage.textContent = "Lý do quá dài (tối đa 200 ký tự)!";
                errorMessage.style.display = "block";
                return false;
            }

            var startDate = new Date(startDateInput);
            var endDate = new Date(endDateInput);
            var today = new Date();
            today.setHours(0, 0, 0, 0);

            if (isNaN(startDate.getTime()) || isNaN(endDate.getTime())) {
                errorMessage.textContent = "Ngày không hợp lệ!";
                errorMessage.style.display = "block";
                return false;
            }

            if (startDate < today) {
                errorMessage.textContent = "Ngày bắt đầu phải từ hôm nay trở đi!";
                errorMessage.style.display = "block";
                return false;
            }

            if (endDate < startDate) {
                errorMessage.textContent = "Ngày kết thúc phải sau ngày bắt đầu!";
                errorMessage.style.display = "block";
                return false;
            }

            errorMessage.style.display = "none";
            return true;
        }
    </script>
</body>
</html>