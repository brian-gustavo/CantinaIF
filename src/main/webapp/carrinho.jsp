<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Carrinho - Cantina Universitária</title>
    <link rel="stylesheet" href="css/carrinho.css">
</head>
<body>

<div class="navbar">
    <div class="logo">
        <a href="home.jsp"><img src="img/Logo.png" alt="Logo" style="height: 40px;"></a>
    </div>    
    <div class="info">
        <span>Prontuário: ${sessionScope.comprador.prontuario}</span>
        <a href="carrinho.jsp" style="font-weight: bold; text-decoration: none; color: white;">Carrinho</a>
        <a href="login.jsp" style="font-weight: bold; text-decoration: none; color: white;">Logout</a>
    </div>
</div>

    <h1>Seu Carrinho</h1>

    <div id="message-area">
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="mensagem-sucesso">
                <p>${sessionScope.successMessage}</p>
            </div>
            <%-- Remove a mensagem da sessão para que não apareça novamente na próxima visita --%>
            <c:remove var="successMessage" scope="session"/>
        </c:if>

        <c:if test="${not empty requestScope.errorMessage}">
            <div class="mensagem-erro">
                <p>${requestScope.errorMessage}</p>
            </div>
        </c:if>
    </div>

  <div id="carrinho-container"></div>

        <div class="finalizar">
            <p><strong>Total: R$ <span id="display-total">0.00</span></strong></p> <%-- Este é o único total que vamos usar --%>
            <form action="FinalizarCompraServlet" method="post">
                <button type="submit">FINALIZAR COMPRA</button>
            </form>
        </div>
        
        <script src="js/carrinho.js"></script>
</body>
</html>
