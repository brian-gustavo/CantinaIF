package servlets;

import java.io.IOException;
import java.io.InputStream;

import dao.ProductDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.Produto;

@WebServlet("/EditarProdutosServlet")
@MultipartConfig
public class EditarProdutosServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");
        int id = Integer.parseInt(request.getParameter("id"));
        ProductDao dao = new ProductDao();

        if ("salvar".equals(acao)) {
            String nome = request.getParameter("nome");
            String descricao = request.getParameter("descricao");
            double preco = Double.parseDouble(request.getParameter("preco"));
            int estoque = Integer.parseInt(request.getParameter("estoque"));
            String categoria = request.getParameter("categoria");

            // Processar imagem
            Part imagemPart = request.getPart("imagem");
            InputStream imagemStream = imagemPart.getSize() > 0 ? imagemPart.getInputStream() : null;

            Produto produto = new Produto(id, nome, descricao, preco, estoque, categoria);
            dao.atualizarProduto(produto, imagemStream); // método precisa aceitar stream opcional

        } else if ("excluir".equals(acao)) {
            dao.excluirProduto(id);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Produto atualizado com sucesso");

    }
}
