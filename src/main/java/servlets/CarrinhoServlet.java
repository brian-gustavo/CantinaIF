package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import dao.ProductDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Carrinho;
import model.ItemCarrinho;
import model.Produto;

@WebServlet("/carrinho")
public class CarrinhoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        
        try (BufferedReader reader = request.getReader()) {
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            if (json == null || !json.has("produtoId") || !json.has("quantidade")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(Map.of("message", "Requisição inválida: produtoId ou quantidade ausente.")));
                return;
            }

            int produtoId = json.get("produtoId").getAsInt();
            int quantidade = json.get("quantidade").getAsInt();

            if (quantidade <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(Map.of("message", "Quantidade deve ser um número positivo.")));
                return;
            }

            ProductDao dao = new ProductDao();
            Produto produto = dao.buscarPorId(produtoId);

            if (produto == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(gson.toJson(Map.of("message", "Produto não encontrado.")));
                return;
            }
            
            if (produto.getEstoque() < quantidade) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(gson.toJson(Map.of("message", "Quantidade solicitada excede o estoque disponível. Estoque: " + produto.getEstoque())));
                return;
            }

            HttpSession session = request.getSession();
            Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");

            if (carrinho == null) {
                carrinho = new Carrinho();
                session.setAttribute("carrinho", carrinho);
            }

            carrinho.adicionarProduto(produto, quantidade);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(Map.of("message", "Produto adicionado ao carrinho com sucesso!")));

        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(gson.toJson(Map.of("message", "Erro no formato JSON da requisição.")));
            e.printStackTrace();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of("message", "Erro interno ao processar a requisição.")));
            e.printStackTrace();
        }
    }
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    HttpSession session = request.getSession(false);
	    Carrinho carrinho = (session != null) ? (Carrinho) session.getAttribute("carrinho") : null;

	    List<ItemCarrinho> itens = (carrinho != null) ? carrinho.getItens() : new ArrayList<>();

	    List<Map<String, Object>> jsonList = new ArrayList<>();

	    for (ItemCarrinho item : itens) {
	        Map<String, Object> itemJson = new HashMap<>();
	        
	        Map<String, Object> produtoJson = new HashMap<>();
	        produtoJson.put("id", item.getProduto().getId());
	        produtoJson.put("nome", item.getProduto().getNome());
	        produtoJson.put("descricao", item.getProduto().getDescricao());
	        produtoJson.put("preco", item.getProduto().getPreco());
	        produtoJson.put("estoque", item.getProduto().getEstoque());
	        produtoJson.put("categoria", item.getProduto().getCategoria());
	        
	        byte[] imagemBytes = item.getProduto().getImagem();
	        String imageBase64 = imagemBytes != null ? Base64.getEncoder().encodeToString(imagemBytes) : null;
	        produtoJson.put("imagemBase64", imageBase64);
	        
	        itemJson.put("produto", produtoJson);
	        itemJson.put("quantidade", item.getQuantidade());
	        itemJson.put("subtotal", item.getSubtotal());
	        
	        jsonList.add(itemJson);
	    }

	    Gson gson = new Gson();
	    String json = gson.toJson(jsonList);
	    response.getWriter().write(json);
	}
}
