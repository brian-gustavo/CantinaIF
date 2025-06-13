document.addEventListener("DOMContentLoaded", function () {
  fetch('carrinho') // GET por padrão
    .then(response => response.json())
    .then(produtos => {
      const container = document.getElementById("carrinho-container");
      const resumo = document.getElementById("resumo-carrinho");
      container.innerHTML = "";

      let totalItens = 0;
      let totalGeral = 0;

      produtos.forEach(produto => {
        const { nome, descricao, preco, quantidade, subtotal, imagemUrl } = produto;

        totalItens += quantidade;
        totalGeral += subtotal;

        const itemDiv = document.createElement("div");
        itemDiv.classList.add("item-carrinho");

        itemDiv.innerHTML = `
          <div style="display: flex; gap: 10px; margin-bottom: 16px;">
            <img src="${imagemUrl || 'img/padrao.png'}" alt="Produto" style="width: 100px; height: 100px; object-fit: cover;">
            <div>
              <h3>${nome}</h3>
              <p>${descricao}</p>
              <p>Preço unitário: R$ ${preco.toFixed(2)}</p>
              <p>Quantidade: ${quantidade}</p>
              <p><strong>Total: R$ ${subtotal.toFixed(2)}</strong></p>
            </div>
          </div>
        `;

        container.appendChild(itemDiv);
      });

      resumo.innerHTML = `
        <hr>
        <h2>Total de itens: ${totalItens}</h2>
        <h2>Valor total: R$ ${totalGeral.toFixed(2)}</h2>
      `;
    })
    .catch(error => {
      console.error("Erro ao carregar carrinho:", error);
    });
});
