package dao;

import model.Comprador;
import model.Pedido;
import model.Produto;
import model.ItemCarrinho;

import utils.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoDao {

    public PedidoDao() {
    }

    /**
     * Lista todos os pedidos pendentes com todos os seus detalhes:
     * Comprador (nome, prontuário, email) e
     * Itens do Pedido (Produto e Quantidade de cada produto no pedido)
     *
     * @return Uma lista de objetos Pedido, cada um populado com seu Comprador e ItensCarrinho
     */
    public List<Pedido> listarTodosPedidosComDetalhes() {
        Map<Integer, Pedido> pedidosMap = new HashMap<>();

        // SQL que junta todas as tabelas necessárias:
        // Pedido (p), Comprador (c), Pedido_Produto (pp), Produto (prod)
        String sql = "SELECT p.id AS pedido_id, " +
                     "c.prontuario AS comprador_prontuario, c.nome AS comprador_nome, c.email AS comprador_email, " +
                     "prod.id AS produto_id, prod.nome AS produto_nome, prod.descricao AS produto_descricao, " +
                     "prod.preco AS produto_preco, prod.estoque AS produto_estoque, prod.categoria AS produto_categoria, " +
                     "pp.quantidade AS item_quantidade " +
                     "FROM Pedido p " +
                     "JOIN Comprador c ON p.comprador_prontuario = c.prontuario " +
                     "JOIN Pedido_Produto pp ON p.id = pp.pedido_id " +
                     "JOIN Produto prod ON pp.produto_id = prod.id " +
                     "ORDER BY p.id ASC, prod.nome ASC";

        // Usa try-with-resources para garantir que a conexão e os statements sejam fechados automaticamente
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int pedidoId = rs.getInt("pedido_id");

                Pedido pedido = pedidosMap.get(pedidoId);
                if (pedido == null) {
                    Comprador comprador = new Comprador();
                    comprador.setProntuario(rs.getString("comprador_prontuario"));
                    comprador.setNome(rs.getString("comprador_nome"));
                    comprador.setEmail(rs.getString("comprador_email"));

                    pedido = new Pedido();
                    pedido.setId(pedidoId);
                    pedido.setComprador(comprador);
                    pedidosMap.put(pedidoId, pedido);
                }

                Produto produto = new Produto();
                produto.setId(rs.getInt("produto_id"));
                produto.setNome(rs.getString("produto_nome"));
                produto.setDescricao(rs.getString("produto_descricao"));
                produto.setPreco(rs.getFloat("produto_preco"));
                produto.setEstoque(rs.getInt("produto_estoque"));

                String categoriaString = rs.getString("produto_categoria");
                if (categoriaString != null && !categoriaString.isEmpty()) {
                    try {
                    	produto.setCategoria(Produto.Categoria.valueOf(categoriaString.trim().toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Erro: Categoria '" + categoriaString + "' inválida para o produto ID: " + produto.getId());
                    }
                }
                // Não carregar a imagem aqui, pois ela é pesada e não necessária para a listagem inicial do pedido

                int quantidade = rs.getInt("item_quantidade");
                ItemCarrinho itemCarrinho = new ItemCarrinho(produto, quantidade);
                pedido.addItem(itemCarrinho);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar pedidos com detalhes: " + e.getMessage());
            e.printStackTrace();
        }

        return new ArrayList<>(pedidosMap.values());
    }

    /**
     * Salva um novo pedido no banco de dados, incluindo seus itens.
     * Esta operação é transacional para garantir atomicidade.
     *
     * @param pedido O objeto Pedido a ser salvo, contendo o Comprador e a lista de ItemCarrinho
     * @throws SQLException Se ocorrer um erro no banco de dados
     */
    public void criarPedido(Pedido pedido) throws SQLException {
        Connection conn = null;
        PreparedStatement stmtPedido = null;
        PreparedStatement stmtItemPedido = null;
        ResultSet rs = null;

        String sqlInsertPedido = "INSERT INTO Pedido (comprador_prontuario) VALUES (?)";
        String sqlInsertItemPedido = "INSERT INTO Pedido_Produto (pedido_id, produto_id, quantidade) VALUES (?, ?, ?)";

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            stmtPedido = conn.prepareStatement(sqlInsertPedido, Statement.RETURN_GENERATED_KEYS);
            stmtPedido.setString(1, pedido.getComprador().getProntuario());
            stmtPedido.executeUpdate();

            rs = stmtPedido.getGeneratedKeys();
            if (rs.next()) {
                pedido.setId(rs.getInt(1));
            } else {
                throw new SQLException("Não foi possível obter o ID gerado para o pedido.");
            }

            stmtItemPedido = conn.prepareStatement(sqlInsertItemPedido);
            for (ItemCarrinho item : pedido.getItens()) {
                stmtItemPedido.setInt(1, pedido.getId());
                stmtItemPedido.setInt(2, item.getProduto().getId());
                stmtItemPedido.setInt(3, item.getQuantidade());
                stmtItemPedido.addBatch();
            }
            stmtItemPedido.executeBatch();

            conn.commit();
        } catch (SQLException e) {
            // Se algo der errado, tenta fazer rollback da transação
            if (conn != null) {
                try {
                    conn.rollback(); // Desfaz todas as operações para manter a consistência do DB
                } catch (SQLException ex) {
                    System.err.println("Erro ao fazer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Erro ao criar pedido: " + e.getMessage());
            throw e; // Lança a exceção para que o servlet que chamou possa tratá-la
        } finally {
            // Garante que todos os recursos (ResultSet, PreparedStatements, Connection) sejam fechados
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (stmtPedido != null) try { stmtPedido.close(); } catch (SQLException e) {}
            if (stmtItemPedido != null) try { stmtItemPedido.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }

    /**
     * Deleta um pedido do banco de dados, incluindo seus itens associados.
     * Esta operação é transacional: assim, garante que ou tudo é deletado, ou nada é.
     *
     * @param pedidoId O ID do pedido a ser deletado
     * @throws SQLException Se ocorrer um erro no banco de dados
     */
    public void deletarPedido(int pedidoId) throws SQLException {
        String sqlDeleteItens = "DELETE FROM Pedido_Produto WHERE pedido_id = ?";
        String sqlDeletePedido = "DELETE FROM Pedido WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmtItens = null;
        PreparedStatement stmtPedido = null;

        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            stmtItens = conn.prepareStatement(sqlDeleteItens);
            stmtItens.setInt(1, pedidoId);
            stmtItens.executeUpdate();

            stmtPedido = conn.prepareStatement(sqlDeletePedido);
            stmtPedido.setInt(1, pedidoId);
            stmtPedido.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao fazer rollback: " + ex.getMessage());
                }
            }
            System.err.println("Erro ao deletar pedido: " + e.getMessage());
            throw e;
        } finally {
            if (stmtItens != null) try { stmtItens.close(); } catch (SQLException e) {}
            if (stmtPedido != null) try { stmtPedido.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        }
    }
}
