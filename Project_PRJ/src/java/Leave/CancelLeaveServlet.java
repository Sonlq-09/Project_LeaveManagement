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


@WebServlet("/cancel-leave")
public class CancelLeaveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user_id") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int userId = (Integer) request.getSession().getAttribute("user_id");
        int leaveId = Integer.parseInt(request.getParameter("leave_id"));

        DBUtil dbUtil = new DBUtil();
        Leave leave = dbUtil.getLeaveDB().get(leaveId);
        if (leave != null && leave.getUserId() == userId && "Chờ duyệt".equals(leave.getStatus())) {
            dbUtil.getLeaveDB().delete(leave);
        }
        response.sendRedirect("dashboard.jsp");
    }
}