package servlet;

import Dao.UserDao;
import model.Comprador;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class UserRegisterServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	private final UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String prontuario = request.getParameter("prontuario");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
	String nome = request.getParameter("nome");

        Comprador comprador = new Comprador(prontuario, email, senha, nome);
        userDao.create(comprador);

        response.sendRedirect("login.jsp");
    }
}
