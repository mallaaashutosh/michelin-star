//GET and post request and validataing the user input and call the user dao.register() to save to database and shows the success or error mesage
package com.michelian-star.controller;

import com.learninglogs.dao.UserDao;
import com.learninglogs.dao.UserDaoImpl;
import com.learninglogs.entity.User;
import com.learninglogs.utils.PasswordUtil;
import com.learninglogs.utils.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")

public class RegisterServlet extends HttpServlet{
    private final UserDAO userDAO=new UserDAOImpl();

    @Override
            protected void doGet(HttpServletRequest request,
            HttpServletResponse response)//httpServletRequest resquest is the data from the client requeesting toward the server and httpServletResponse response is used to send the data back to the client
            throws ServletException, IOException {
                request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                        .forward(request, response);
            }

        // the getRequestDispatcher("/WEB-INF/views/register.jsp") locates the file register.jsp and the  forward(request,response sends the request internally to the jsp without changing the url in the browser)
    @Override
            protected void doPost(HttpServletRequest request,
                HttpServletResponse response)
            throws ServletException, IOException {
    String name =request.getParameter("name");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("cpassword");

    StringBuilder errors = new StringBuilder();

            if (ValidationUtil.isNullOrEmpty(name)
                    || !ValidationUtil.isAlphanumericStartingWithLetter(name)
                    || name.length() < 4) {
                errors.append("name must be alphanumeric, start with a letter, and be at least 4 characters. ");
            }
            if (!ValidationUtil.isValidEmail(email)) {
                errors.append("Invalid email format. ");
            }
            if (!ValidationUtil.isValidPassword(password)) {
                errors.append("Password must be 8+ characters with uppercase, number, and symbol. ");
            }
            if (!ValidationUtil.doPasswordsMatch(password, confirmPassword)) {
                errors.append("Passwords do not match. ");
            }

            if (!errors.isEmpty()) {
                request.setAttribute("error", errors.toString().trim());
                request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                        .forward(request, response);
                return;
            }

            String hashedPassword = PasswordUtil.getHashPassword(password);
            User user = new User(name, email, hashedPassword);

            boolean success = userDao.insertUser(user);

            if (!success) {
                request.setAttribute("error", "Username or email already exists.");
                request.getRequestDispatcher("/WEB-INF/views/register.jsp")
                        .forward(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/login");
        }

        }
    }
