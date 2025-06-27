package servlets;

import dao.PedidoDao;
import model.Pedido;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/pedidos")
public class PedidosJsonServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PedidoDao pedidoDAO = new PedidoDao();

        List<Pedido> pedidos = pedidoDAO.listarTodosPedidosComDetalhes();

        Gson gson = new Gson();
        String jsonPedidos = gson.toJson(pedidos);

        PrintWriter out = response.getWriter();
        out.print(jsonPedidos);
        out.flush();
    }
}
