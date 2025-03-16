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


@WebServlet("/approveLeave")
public class ApproveLeaveServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("user_id") == null || !"manager".equals((String) request.getSession().getAttribute("role"))) {
            response.sendRedirect("dashboard.jsp");
            return;
        }

        String idStr = request.getParameter("id");
        try {
            int id = Integer.parseInt(idStr);
            Leave leave = DBUtil.getLeaveDB().get(id);
            User user = DBUtil.getUserDB().get(leave.getUserId());
            if (leave != null && "Pending".equals(leave.getStatus()) && user != null && "employee".equalsIgnoreCase(user.getRole())) {
                leave.setStatus("Approved");
                DBUtil.getLeaveDB().update(leave);
                System.out.println("Leave ID " + id + " approved successfully");
            }
            response.sendRedirect("view-agenda.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("view-agenda.jsp?error=true&message=" + java.net.URLEncoder.encode("Lỗi khi duyệt đơn: " + e.getMessage(), "UTF-8"));
        }
    }
}