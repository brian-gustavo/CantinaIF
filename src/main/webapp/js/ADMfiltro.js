// Filtra e carrega as coisas adequadamente
function carregarProdutosADM(categoria = 'todos') {
    fetch(`apiAdm/produtos?categoria=${categoria}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(produtos => {
            const container = document.getElementById('productContainer');
            container.innerHTML = '';

            produtos.forEach(produto => {
                const produtoDiv = document.createElement('div');
                produtoDiv.className = 'product';

                produtoDiv.setAttribute('data-type', produto.categoria ? produto.categoria.toLowerCase() : 'outros');

                const imageUrl = produto.id ? `image?id=${produto.id}` : '#';
                const imageTag = produto.imagem && produto.imagem.length > 0
                    ? `<img src="${imageUrl}" alt="${produto.nome}" class="product-image">`
                    : `<img src="img/placeholder-image.png" alt="Imagem padrão" class="product-image">`;

                produtoDiv.innerHTML = `
                    <div class="product-image-wrapper">
                        ${imageTag}
                    </div>
					<div>
						<h3>${produto.nome}</h3>
					    <p>${produto.descricao}</p>
					    <p><strong>Preço:</strong> R$ ${produto.preco}</p>
					    <p><strong>Disponível:</strong> ${produto.estoque}</p>

					    <div class="quantidade-container">
					          <button onclick="alterarQuantidade(${produto.id}, -1)">-</button>
					          <input type="text" id="quantidade-${produto.id}" value="1" readonly>
					          <button onclick="alterarQuantidade(${produto.id}, 1)">+</button>
						</div>
					</div>
					<button onclick="abrirPopupEdicao(${produto.id})">Editar</button>
                `;

                container.appendChild(produtoDiv);
            });
        })
        .catch(error => {
            console.error('Erro ao buscar produtos:', error);
            const container = document.getElementById('productContainer');
            container.innerHTML = '<p>Não foi possível carregar os produtos. Tente novamente mais tarde.</p>';
        });
}

// Altera a quantidade do pedido
function alterarQuantidade(produtoId, delta) {
  const input = document.getElementById(`quantidade-${produtoId}`);
  let valor = parseInt(input.value);
  valor = isNaN(valor) ? 1 : valor + delta;
  if (valor < 1) valor = 1;
  input.value = valor;
}

// Desenha no HTML
document.addEventListener('DOMContentLoaded', function () {
    const botoesFiltro = document.querySelectorAll('.filter-btn');

    botoesFiltro.forEach(botao => {
        botao.addEventListener('click', function () {
            const categoria = this.getAttribute('data-filter');

            // Atualiza visualmente o botão ativo
            document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');

            // Chama o carregamento AJAX com a categoria selecionada
            carregarProdutosADM(categoria);
        });
    });

    // Carrega todos os produtos ao abrir a página
    carregarProdutosADM('todos');
});

function abrirPopupEdicao(id) {
  fetch('CarregarProdutoServlet?id=' + id)
    .then(response => response.json())
    .then(produto => {
      document.getElementById('editar-id').value = produto.id;
      document.getElementById('editar-nome').value = produto.nome;
      document.getElementById('editar-descricao').value = produto.descricao;
      document.getElementById('editar-preco').value = produto.preco;
      document.getElementById('editar-estoque').value = produto.estoque;
      document.getElementById('editar-categoria').value = produto.categoria;
      document.getElementById('popup-edicao').style.display = 'block';
    });
}

document.getElementById('form-editar-produto').addEventListener('submit', function(e) {
  e.preventDefault();
  const formData = new FormData(this);

  fetch('EditarProdutosServlet', {
    method: 'POST',
    body: formData
  })
  .then(() => {
    alert('Produto atualizado com sucesso!');
    fecharPopup();
    location.reload(); // ou atualizar a lista dinamicamente
  });
});

function confirmarExclusao() {
  if (confirm("Tem certeza que deseja excluir este produto?")) {
    const id = document.getElementById('editar-id').value;
    fetch('EditarProdutosServlet', {
      method: 'POST',
      body: new URLSearchParams({ id: id, acao: 'excluir' })
    })
    .then(() => {
      alert('Produto excluído com sucesso!');
      fecharPopup();
      location.reload();
    });
  }
}

function fecharPopup() {
  document.getElementById('popup-edicao').style.display = 'none';
}
