<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="css/login.css">
    <style>
        .error-message {
            color: #dc3545; 
            background-color: #f8d7da; 
            border: 1px solid #f5c6cb;
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 5px;
            text-align: center;
            font-weight: bold;
        }
    </style>
</head>
<body>
    <section class="login-page">
        <h1>Login</h1>

        <c:if test="${not empty requestScope.error}">
            <div class="error-message">
                <p>${requestScope.error}</p>
            </div>
        </c:if>

        <form class="login-form" action="login" method="post">
            <label for="prontuario">Prontuário</label>
            <input type="text" id="prontuario" name="prontuario" required maxlength="10">

            <label for="senha">Senha</label>
            <input type="password" id="senha" name="senha" required maxlength="100">

            <button type="submit">Login</button>
            <a href="cadastro.jsp"><button type="button">Cadastrar-se</button></a>
        </form>
    </section>
</body>
</html>
