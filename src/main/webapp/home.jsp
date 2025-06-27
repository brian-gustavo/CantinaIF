<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="model.Comprador" %>
<%@ page import="model.Produto" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Home - Cantina Universitária</title>
    <link rel="stylesheet" href="css/user.css">
</head>
<body>

<div class="navbar">
    <div class="logo">
        	<a href="home.jsp"><img src="img/Logo.png" alt="Logo" style="height: 40px;"></a>
    </div>    
    <div class="info">
        <span>Prontuário: ${sessionScope.comprador.prontuario}</span>
        <a href="carrinho.jsp" style="
        font-weight: bold;
		text-decoration: none;
  		color: white;">Carrinho</a>
        <a href="login.jsp" style="
        font-weight: bold;
		text-decoration: none;
  		color: white;">Logout</a>
    </div>
</div>

<div class="filters">
    <button class="filter-btn active" data-filter="todos">Todos</button>
    <button class="filter-btn" data-filter="salgado">Salgados</button>
    <button class="filter-btn" data-filter="doce">Doces</button>
    <button class="filter-btn" data-filter="lanche">Lanches</button>
    <button class="filter-btn" data-filter="bebida">Bebidas</button>
</div>

<div id="productContainer" class="produtosPai">
</div>

<script src="js/filtro.js"></script>

</body>
</html>
