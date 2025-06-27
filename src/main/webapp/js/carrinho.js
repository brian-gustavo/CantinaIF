document.addEventListener("DOMContentLoaded", function() {
    const CONTEXT_PATH = '/CantinaIF';

    fetch(`${CONTEXT_PATH}/carrinho`)
        .then(response => {
            console.log('Resposta do servidor:', response);

            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(`Erro HTTP: ${response.status} - ${response.statusText} - Detalhe: ${text}`);
                }).catch(() => {
                    throw new Error(`Erro HTTP: ${response.status} - ${response.statusText}`);
                });
            }
            return response.json();
        })
        .then(data => {
            const container = document.getElementById("carrinho-container");
            const displayTotalSpan = document.getElementById("display-total");

            container.innerHTML = "";

            let totalGeral = 0;

            console.log('Dados recebidos do carrinho (JSON):', data);

            const itensCarrinho = Array.isArray(data) ? data : (data.itens || []);

            if (!itensCarrinho || itensCarrinho.length === 0) {
                container.innerHTML = '<p>Seu carrinho está vazio.</p>';
                if (displayTotalSpan) displayTotalSpan.textContent = '0.00';
                return;
            }

            itensCarrinho.forEach(item => {
                if (!item.produto || typeof item.quantidade === 'undefined') {
                    console.warn('Item do carrinho com estrutura inválida:', item);
                    return;
                }

                const { produto, quantidade } = item;
                const { nome, descricao, preco } = produto;

                const imagem = produto.imagemBase64 ? `data:image/jpeg;base64,${produto.imagemBase64}` : "img/padrao.png";

                const subtotal = preco * quantidade;

                totalGeral += subtotal;

                const itemDiv = document.createElement("div");
                itemDiv.classList.add("item-carrinho");

                itemDiv.innerHTML = `
                    <div style="display: flex; gap: 10px; margin-bottom: 16px;">
                        <img src="${imagem}" alt="Produto" style="width: 100px; height: 100px; object-fit: cover;">
                        <div>
                            <h3>${nome}</h3>
                            <p>${descricao || ''}</p>
                            <p>Preço unitário: R$ ${preco.toFixed(2)}</p>
                            <p>Quantidade: ${quantidade}</p>
                            <p><strong>Total: R$ ${subtotal.toFixed(2)}</strong></p>
                        </div>
                    </div>
                `;
                container.appendChild(itemDiv);
            });

            console.log("Total Geral Calculado:", totalGeral);
            if (displayTotalSpan) {
                displayTotalSpan.textContent = totalGeral.toFixed(2);
            }

        })
        .catch(error => {
            console.error("Erro ao carregar carrinho:", error);
            document.getElementById("carrinho-container").innerHTML = '<p>Erro ao carregar o carrinho. Verifique o console para mais detalhes.</p>';
            const displayTotalSpan = document.getElementById("display-total");
            if (displayTotalSpan) displayTotalSpan.textContent = '0.00';
        });
});
