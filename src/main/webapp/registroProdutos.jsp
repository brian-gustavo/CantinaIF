<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="model.Produto" %>
<%@ page import="servlets.APIUserProdutos" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Painel do Vendedor</title>
    <link rel="stylesheet" href="css/user.css"> <!-- Link para o arquivo de estilização user.css -->
    <script>
        // Formulário de criação de novos produtos
        function toggleNewProductForm() {
            const form = document.getElementById('new-product-form');
            form.style.display = (form.style.display === 'none') ? 'block' : 'none';
        }
    </script>
</head>
<body>
    <!-- Barra de navegação -->
    <div class="navbar">
        <div class="logo">
        	<img src="img/Logo.png" alt="Logo" style="height: 40px;">
    	</div>       
        <div>Painel do Vendedor</div>
        <div>
            <button class="new-product-btn" onclick="toggleNewProductForm()">+ Novo Produto</button>
            <a href="logout" style="color: white; margin-left: 20px;">Logout</a>
        </div>
         <div class="dark-mode-toggle" onclick="toggleDarkMode()">
	    <div class="icon"></div>
	</div>
	
	<script>
	function toggleDarkMode() {
	    document.body.classList.toggle('dark-mode');
	    document.querySelector('.dark-mode-toggle').classList.toggle('active');
	}
	</script>
    </div>
    
    <!-- Menu de filtragem por tipo de produto -->
    <div class="filters">
        <button class="filter-btn active" data-filter="todos">Todos</button>
        <button class="filter-btn" data-filter="salgado">Salgados</button>
        <button class="filter-btn" data-filter="doce">Doces</button>
        <button class="filter-btn" data-filter="lanche">Lanches</button>
        <button class="filter-btn" data-filter="bebida">Bebidas</button>
    </div>
    
    <!-- Novo produto -->
    <div id="new-product-form" class="new-product-form">
	    <form action="registrar-produto" method="post">
	        <h3>Novo Produto</h3>
	
	        <input type="text" name="nome" placeholder="Nome do produto" required><br><br>
	
	        <textarea name="descricao" placeholder="Descrição" required></textarea><br><br>
	
	        <input type="number" name="preco" step="0.01" placeholder="Valor" required><br><br>
	
	        <input type="number" name="estoque" placeholder="Quantidade inicial" required><br><br>
	
	        <select name="categoria" required>
	            <option value="" disabled selected>Selecione uma categoria</option>
	            <option value="SALGADO">Salgado</option>
	            <option value="DOCE">Doce</option>
	            <option value="LANCHE">Lanche</option>
	            <option value="BEBIDA">Bebida</option>
	        </select><br><br>
	        
	         <label for="editar-imagem">Imagem:</label><br>
    		<input type="file" name="imagem" id="editar-imagem" accept="image/*"><br><br>
	
	        <button type="submit">Criar</button>
	    </form>
	</div>
    
    <!-- Lista de produtos renderizados dinamicamente -->
    <div id="productContainer" class="produtosPai">
    <!-- Produtos serão inseridos aqui via JavaScript -->
</div>
    <script src="js/admFiltro.js"></script>
</body>
</html>
