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
					</div>
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

document.addEventListener('DOMContentLoaded', function() {
	const botoesFiltro = document.querySelectorAll('.filter-btn');

	botoesFiltro.forEach(botao => {
		botao.addEventListener('click', function() {
			const categoria = this.getAttribute('data-filter');

			document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
			this.classList.add('active');

			carregarProdutosADM(categoria);
		});
	});

	carregarProdutosADM('todos');
});
