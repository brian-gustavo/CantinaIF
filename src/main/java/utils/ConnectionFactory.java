package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:mysql://localhost:3306/todoapp?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; 
    private static final String PASSWORD = "root";

    /**
     * Retorna uma nova conexão com o banco de dados
     * @return Objeto Connection para o banco de dados
     * @throws SQLException Se ocorrer um erro ao estabelecer a conexão
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver JDBC do MySQL não encontrado no classpath.");
            e.printStackTrace();
            throw new SQLException("Driver JDBC do MySQL não encontrado.", e);
        }
        // Se houver problemas com URL, USER, PASSWORD, DriverManager.getConnection() já lançará SQLException; não é necessário catch aqui
    }

    /**
     * Método auxiliar para fechar conexões de forma segura.
     * Pode ser usado nos blocos finally dos DAOs.
     * @param conn A conexão a ser fechada
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
