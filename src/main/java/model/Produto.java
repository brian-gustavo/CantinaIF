package model;

public class Produto {
    // Classe do tipo enum para o atributo Categoria
    public enum Categoria {
        SALGADO,
        DOCE,
        LANCHE,
        BEBIDA
    }

    private int id;
    private String nome;
    private String descricao;
    private float preco;
    private int estoque;
    private Categoria categoria;
    private byte[] imagem;

    // Construtor original (mantido para compatibilidade, se necessário)
    public Produto(String nome, String descricao, float preco, int estoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.imagem = null;
    }

    // Construtor adicional para incluir a imagem (mantido para compatibilidade, se necessário)
    public Produto(String nome, String descricao, float preco, int estoque, byte[] imagem) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.imagem = imagem;
    }

    // --- NOVO CONSTRUTOR PARA CORRESPONDER AO SERVLET DE EDIÇÃO ---
    public Produto(int id, String nome, String descricao, float preco, int estoque, String categoriaString) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        // Converte a String da categoria para o enum Categoria
        try {
            this.categoria = Categoria.valueOf(categoriaString.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Lidar com categoria inválida, talvez defina um valor padrão ou lance uma exceção
            System.err.println("Categoria inválida: " + categoriaString);
            this.categoria = null; // Ou um valor padrão, como Categoria.OUTRO (se você adicionar)
        }
        this.imagem = null; // Inicializa a imagem como null, pois não é passada neste construtor
    }

    // --- NOVO CONSTRUTOR SOBRECARREGADO QUE INCLUI A IMAGEM (OPCIONAL) ---
    // Você pode precisar deste se for atualizar a imagem junto com outros dados
    public Produto(int id, String nome, String descricao, float preco, int estoque, String categoriaString, byte[] imagem) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        try {
            this.categoria = Categoria.valueOf(categoriaString.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Categoria inválida: " + categoriaString);
            this.categoria = null;
        }
        this.imagem = imagem;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }
}
