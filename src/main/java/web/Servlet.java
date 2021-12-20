package web;

import dao.Dao;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/")
public class Servlet extends HttpServlet {
        private static final long serialVersionUID = 1L;
        private dao.Dao Dao;

        public void init() throws ServletException {
           Dao = new Dao();
            super.init();
        }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
           doGet(request,response);
    }

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

            String action = request.getServletPath();
            switch (action) {
                case "/new":
                    showRegistrationPage(request,response);
                    break;
                case "/insert":
                    insertUser(request,response);
                    break;
                case "/delete":
                deleteUser(request,response);
                    break;
                case "/edit":
                    editUser(request, response);
                    break;
                case "/update":
                    updateUser(request, response);
                    break;
                default:
                    allUsers(request,response);
                    break;
            }
        }

        private void showRegistrationPage(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            RequestDispatcher dispatcher = request.getRequestDispatcher("registration.jsp");
            dispatcher.forward(request,response);
        }
    private void insertUser (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            String name = request.getParameter("name");
            String login = request.getParameter("login");
            String password = request.getParameter("password");

         User user = new User (login,name, password);
         User user1 = Dao.loginCheck(user.getLogin());


            List<String> errors = new ArrayList<>();
            if (name.equals("") || login.equals("") || password.equals("")){
                errors.add("Please fill in all fields");
                response.sendRedirect("registration.jsp");
            }
            if (user1 != null) {
                errors.add("User already exists");
                response.sendRedirect("registration.jsp");
            } else {

                try {
                    Dao.register(user);
                    response.sendRedirect("allUsers");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        private void deleteUser (HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException{
            int id = Integer.parseInt(request.getParameter("id"));
            Dao.delete(id);
            response.sendRedirect("allUsers");
        }

    private void editUser (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        int id = Integer.parseInt(request.getParameter("id"));
       User selectedUser =  Dao.selectUserByID(id);
       RequestDispatcher dispatcher = request.getRequestDispatcher("registration.jsp");
       request.setAttribute("user", selectedUser);
        dispatcher.forward(request, response);
    }

    private void updateUser (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
       /* String role = request.getParameter("role");*/
        User userUpdate  = new User(id, login,name,password);

        Dao.updateUser(userUpdate);
        response.sendRedirect("allUsers");

    }

    private void allUsers (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        List <User> allUsers = Dao.select();
        request.setAttribute("allUsers", allUsers);
        RequestDispatcher dispatcher = request.getRequestDispatcher("allUsers.jsp");
        dispatcher.forward(request,response);

    }
}

