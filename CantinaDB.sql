CREATE DATABASE todoapp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE todoapp;

CREATE TABLE Comprador (
    prontuario VARCHAR(10) PRIMARY KEY,
    email VARCHAR(100) UNIQUE,
    senha VARCHAR(100),
    nome VARCHAR(75)
);

CREATE TABLE Pedido (
    id INT PRIMARY KEY AUTO_INCREMENT,
    comprador_prontuario VARCHAR(10),
    FOREIGN KEY (comprador_prontuario) REFERENCES Comprador(prontuario)
);

CREATE TABLE Produto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100),
    descricao VARCHAR(400),
    preco DECIMAL(10,2),
    estoque INT,
    categoria ENUM('SALGADO', 'DOCE', 'LANCHE', 'BEBIDA'),
    imagem MEDIUMBLOB
);

CREATE TABLE Vendedor (
    prontuario VARCHAR(10) PRIMARY KEY,
    email VARCHAR(100) UNIQUE,
    senha VARCHAR(100)
);

CREATE TABLE Pedido_Produto (
    pedido_id INT,
    produto_id INT,
    quantidade INT DEFAULT 1,
    PRIMARY KEY (pedido_id, produto_id),
    FOREIGN KEY (pedido_id) REFERENCES Pedido(id),
    FOREIGN KEY (produto_id) REFERENCES Produto(id)
);

-- Inserção de dados de um vendedor fictício para poder testar o cadastro de produtos
INSERT INTO Vendedor VALUES ('CJ40028922', 'vendedor@gmail.com', '654321');
