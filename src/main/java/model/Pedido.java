package model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private Comprador comprador;
    private List<ItemCarrinho> itens;

    public Pedido() {
        this.itens = new ArrayList<>();
    }

    public Pedido(int id, Comprador comprador) {
        this.id = id;
        this.comprador = comprador;
        this.itens = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public Comprador getComprador() {
        return comprador;
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setComprador(Comprador comprador) {
        this.comprador = comprador;
    }

    public void setItens(List<ItemCarrinho> itens) {
        this.itens = itens;
    }

    public void addItem(ItemCarrinho item) {
        if (this.itens == null) {
            this.itens = new ArrayList<>();
        }
        this.itens.add(item);
    }
}
