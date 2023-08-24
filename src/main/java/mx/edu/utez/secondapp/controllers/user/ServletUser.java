package mx.edu.utez.secondapp.controllers.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.edu.utez.secondapp.models.user.DaoUser;
import mx.edu.utez.secondapp.models.user.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "ServletUser",
        urlPatterns = {"/api/auth",
                "/api/auth/logout",
                "/api/user/all",
                "/api/user/one",
                "/api/user/save",
                "/api/user/create",
                "/api/user/modify",
                "/api/user/update",
                "/api/user/enable-disabled"
        })
public class ServletUser extends HttpServlet {
    String action, redirect = "/api/auth";
    User user;
    HttpSession session;
    String id, username, password, status, roleId;
    String personId, name, surname, lastname, birhtday,
            curp, personStatus;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        action = req.getServletPath();
        switch (action) {
            case "/api/auth":
                redirect = "/index.jsp";
                break;
            case "/api/admin/dashboard":
                List<Objects> pokemons = new ArrayList<>();
                req.setAttribute("pokemons", pokemons);
                List<Objects> types = new ArrayList<>();
                req.setAttribute("types", types);
                List<Objects> people = new ArrayList<>();
                req.setAttribute("people", people);
                redirect = "/views/pokemon/index.jsp";
                break;
        }
        req.getRequestDispatcher(redirect)
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        action = req.getServletPath();
        switch (action) {
            case "/api/auth":
                username = req.getParameter("username");
                password = req.getParameter("password");
                try {
                    user = new DaoUser()
                            .loadUserByUsernameAndPassword(username, password);
                    if (user != null) {
                        session = req.getSession();
                        session.setAttribute("user", user);
                        switch (user.getRole().getDescription()) {
                            case "USER_ROLE":
                                redirect = "/api/pokemon/all";
                                break;
                            case "ADMIN_ROLE":
                                redirect = "/api/user/all";
                                break;
                        }
                    } else {
                        throw new Exception("Credentials mismatch");
                    }
                } catch (Exception e) {
                    redirect = "/api/auth?result=false&message=" + URLEncoder
                            .encode("Usuario y/o contraseña incorrecta",
                                    StandardCharsets.UTF_8);
                }
                break;
        }
        resp.sendRedirect(req.getContextPath()
                + redirect);
    }
}
