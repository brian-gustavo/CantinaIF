package servlets;

import dao.PedidoDao;
import model.Carrinho;
import model.Comprador;
import model.Pedido;
import model.ItemCarrinho;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/FinalizarCompraServlet")
public class FinalizarCompraServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        Comprador comprador = (Comprador) session.getAttribute("comprador");

        if (carrinho == null || carrinho.isVazio()) {
            request.setAttribute("errorMessage", "Seu carrinho está vazio. Adicione produtos para finalizar a compra.");
            request.getRequestDispatcher("carrinho.jsp").forward(request, response);
            return;
        }

        if (comprador == null || comprador.getProntuario() == null || comprador.getProntuario().isEmpty()) {
            request.setAttribute("errorMessage", "Por favor, faça login para finalizar a compra.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        PedidoDao pedidoDAO = new PedidoDao();
        try {
            Pedido novoPedido = new Pedido();
            novoPedido.setComprador(comprador);
            novoPedido.setItens(carrinho.getItens());

            pedidoDAO.criarPedido(novoPedido);

            carrinho.limparCarrinho();
            session.setAttribute("carrinho", carrinho);

            session.setAttribute("successMessage", "Pedido realizado com sucesso!"); 
            response.sendRedirect("carrinho.jsp"); // Redireciona para a própria página do carrinho
                                                   // O JavaScript de carrinho.js recarregará um carrinho vazio
        } catch (SQLException e) {
            System.err.println("Erro ao finalizar compra: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Erro ao finalizar sua compra. Tente novamente mais tarde.");
            request.getRequestDispatcher("carrinho.jsp").forward(request, response);
        }
    }
}
