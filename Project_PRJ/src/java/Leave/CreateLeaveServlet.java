/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Leave;

import java.io.IOException;
import java.text.SimpleDateFormat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 *
 * @author LTSon
 */
@WebServlet("/create-leave")
public class CreateLeaveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user_id") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int userId = (Integer) request.getSession().getAttribute("user_id");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String leaveType = request.getParameter("leaveType");
        String reason = request.getParameter("reason");

        if (startDateStr == null || startDateStr.isEmpty() || endDateStr == null || endDateStr.isEmpty() || 
            leaveType == null || leaveType.isEmpty() || reason == null || reason.isEmpty()) {
            System.out.println("Invalid input data: startDate=" + startDateStr + ", endDate=" + endDateStr + ", leaveType=" + leaveType + ", reason=" + reason);
            response.sendRedirect("create-leave.jsp?error=true&message=" + java.net.URLEncoder.encode("Vui lòng điền đầy đủ thông tin", "UTF-8"));
            return;
        }

        if (reason.length() > 200) {
            response.sendRedirect("create-leave.jsp?error=true&message=" + java.net.URLEncoder.encode("Lý do quá dài (tối đa 200 ký tự)", "UTF-8"));
            return;
        }

        System.out.println("Received form data - userId: " + userId + ", startDate: " + startDateStr + ", endDate: " + endDateStr + ", leaveType: " + leaveType + ", reason: " + reason);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            java.util.Date startDate = sdf.parse(startDateStr);
            java.util.Date endDate = sdf.parse(endDateStr);

            if (endDate.before(startDate)) {
                System.out.println("End date is before start date: startDate=" + startDateStr + ", endDate=" + endDateStr);
                response.sendRedirect("create-leave.jsp?error=true&message=" + java.net.URLEncoder.encode("Ngày kết thúc phải sau ngày bắt đầu", "UTF-8"));
                return;
            }

            Leave leave = new Leave();
            leave.setUserId(userId);
            leave.setLeaveType(leaveType);
            leave.setStartDate(startDate);
            leave.setEndDate(endDate);
            leave.setReason(reason);
            leave.setStatus("Pending"); 
            leave.setSubmittedAt(new java.util.Date());

            System.out.println("Inserting leave for userId: " + userId + ", StartDate: " + sdf.format(startDate) + ", EndDate: " + sdf.format(endDate));
            DBUtil.getLeaveDB().insert(leave);
            System.out.println("Leave inserted successfully for userId: " + userId);
            response.sendRedirect("dashboard.jsp?success=true");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error inserting leave: " + e.getMessage());
            response.sendRedirect("create-leave.jsp?error=true&message=" + java.net.URLEncoder.encode("Lỗi khi tạo đơn: " + e.getMessage(), "UTF-8"));
        }
    }
}
