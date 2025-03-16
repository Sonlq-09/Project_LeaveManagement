/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Leave;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author LTSon
 */
@WebServlet("/manage-director-leave")
public class ManageDirectorLeaveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String role = (String) request.getSession().getAttribute("role");
        if (request.getSession().getAttribute("user_id") == null || !"director".equalsIgnoreCase(role)) {
            response.sendRedirect("login.html");
            return;
        }

        int leaveId = Integer.parseInt(request.getParameter("leave_id"));
        String action = request.getParameter("action");

        Leave leave = DBUtil.getLeaveDB().get(leaveId);
        User user = DBUtil.getUserDB().get(leave.getUserId());
        if (leave != null && "Pending".equals(leave.getStatus()) && user != null && "manager".equalsIgnoreCase(user.getRole())) {
            if ("approve".equals(action)) {
                leave.setStatus("Approved");
            } else if ("reject".equals(action)) {
                String rejectReason = request.getParameter("reject_reason");
                leave.setStatus("Rejected");
                leave.setRejectReason(rejectReason != null ? rejectReason : "Request rejected by Director");
            }
            DBUtil.getLeaveDB().update(leave);
        }
        response.sendRedirect("director-leaves.jsp");
    }
}