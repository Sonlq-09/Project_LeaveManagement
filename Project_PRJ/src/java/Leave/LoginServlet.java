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
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Kiểm tra giá trị đầu vào
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            System.out.println("Invalid input - Username: " + username + ", Password: " + password);
            response.sendRedirect("login.html?error=invalid");
            return;
        }

        System.out.println("Login attempt - Username: " + username + ", Password: " + password);

        User user = DBUtil.getUserDB().login(username, password);
        System.out.println("LoginServlet - Username: " + username + ", Password: " + password + ", User: " + (user != null ? user.getFullName() : "null"));

        if (user != null) {
            request.getSession().setAttribute("user_id", user.getId());
            request.getSession().setAttribute("full_name", user.getFullName());
            request.getSession().setAttribute("role", user.getRole());
            System.out.println("Login successful - User: " + user.getFullName() + ", Role: " + user.getRole());
            response.sendRedirect("dashboard.jsp");
        } else {
            System.out.println("Login failed - Invalid credentials for Username: " + username);
            response.sendRedirect("login.html?error=invalid");
        }
    }
}