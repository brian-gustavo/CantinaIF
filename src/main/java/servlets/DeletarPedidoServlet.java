package servlets;

import dao.PedidoDao;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/api/deletarPedido")
public class DeletarPedidoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String pedidoIdStr = request.getParameter("id");
        int pedidoId;
        try {
            pedidoId = Integer.parseInt(pedidoIdStr);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(gson.toJson(new ApiResponse("Erro: ID do pedido inválido.", false)));
            return;
        }

        PedidoDao pedidoDAO = new PedidoDao();
        try {
            pedidoDAO.deletarPedido(pedidoId);
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(gson.toJson(new ApiResponse("Pedido " + pedidoId + " concluído com sucesso!", true)));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(new ApiResponse("Erro ao concluir pedido: " + e.getMessage(), false)));
            System.err.println("Erro SQL ao deletar pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Classe interna auxiliar para padronizar as respostas JSON (sucesso/erro)
    private static class ApiResponse {
        String message;
        boolean success;

        public ApiResponse(String message, boolean success) {
            this.message = message;
            this.success = success;
        }
    }
}
