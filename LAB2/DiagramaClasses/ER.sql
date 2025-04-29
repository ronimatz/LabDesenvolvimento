CREATE TABLE `Usuario` (
  `email` varchar(255) PRIMARY KEY,
  `senha` varchar(255)
);

CREATE TABLE `Aluno` (
  `cpf` varchar(255) PRIMARY KEY,
  `nome` varchar(255),
  `rg` varchar(255),
  `instituicao` varchar(255),
  `curso` varchar(255),
  `endereco_id` int,
  `email` varchar(255)
);

CREATE TABLE `Professor` (
  `cpf` varchar(255) PRIMARY KEY,
  `nome` varchar(255),
  `departamento_id` int,
  `instituicao` varchar(255),
  `email` varchar(255)
);

CREATE TABLE `Admin` (
  `email` varchar(255) PRIMARY KEY
);

CREATE TABLE `Endereco` (
  `id` int PRIMARY KEY,
  `rua` varchar(255),
  `bairro` varchar(255),
  `numero` int,
  `complemento` varchar(255),
  `cidade` varchar(255),
  `estado` varchar(255)
);

CREATE TABLE `InstituicaoDeEnsino` (
  `nome` varchar(255) PRIMARY KEY
);

CREATE TABLE `Departamento` (
  `id` int PRIMARY KEY,
  `instituicao` varchar(255)
);

CREATE TABLE `Curso` (
  `nome` varchar(255) PRIMARY KEY,
  `cargaHoraria` int,
  `departamento_id` int
);

CREATE TABLE `Semestre` (
  `identificador` varchar(255) PRIMARY KEY,
  `dataInicio` date,
  `dataFim` date,
  `ativo` boolean
);

CREATE TABLE `EmpresaParceira` (
  `cnpj` varchar(255) PRIMARY KEY,
  `nome` varchar(255)
);

CREATE TABLE `Vantagem` (
  `id` int PRIMARY KEY,
  `descricao` varchar(255),
  `valor` double,
  `desconto` double,
  `fotoProduto` bytea,
  `empresaParceira_cnpj` varchar(255)
);

CREATE TABLE `Extrato` (
  `id` int PRIMARY KEY,
  `saldoMoedas` int
);

CREATE TABLE `Transacao` (
  `id` int PRIMARY KEY,
  `remetente_email` varchar(255),
  `destinatario_email` varchar(255),
  `valor` double,
  `dataEnvio` datetime,
  `extrato_id` int
);

CREATE TABLE `Aluno_Vantagem` (
  `aluno_cpf` varchar(255),
  `vantagem_id` int,
  PRIMARY KEY (`aluno_cpf`, `vantagem_id`)
);

ALTER TABLE `Aluno` ADD FOREIGN KEY (`instituicao`) REFERENCES `InstituicaoDeEnsino` (`nome`);

ALTER TABLE `Aluno` ADD FOREIGN KEY (`curso`) REFERENCES `Curso` (`nome`);

ALTER TABLE `Aluno` ADD FOREIGN KEY (`endereco_id`) REFERENCES `Endereco` (`id`);

ALTER TABLE `Aluno` ADD FOREIGN KEY (`email`) REFERENCES `Usuario` (`email`);

ALTER TABLE `Professor` ADD FOREIGN KEY (`departamento_id`) REFERENCES `Departamento` (`id`);

ALTER TABLE `Professor` ADD FOREIGN KEY (`instituicao`) REFERENCES `InstituicaoDeEnsino` (`nome`);

ALTER TABLE `Professor` ADD FOREIGN KEY (`email`) REFERENCES `Usuario` (`email`);

ALTER TABLE `Admin` ADD FOREIGN KEY (`email`) REFERENCES `Usuario` (`email`);

ALTER TABLE `Departamento` ADD FOREIGN KEY (`instituicao`) REFERENCES `InstituicaoDeEnsino` (`nome`);

ALTER TABLE `Curso` ADD FOREIGN KEY (`departamento_id`) REFERENCES `Departamento` (`id`);

ALTER TABLE `Vantagem` ADD FOREIGN KEY (`empresaParceira_cnpj`) REFERENCES `EmpresaParceira` (`cnpj`);

ALTER TABLE `Transacao` ADD FOREIGN KEY (`remetente_email`) REFERENCES `Usuario` (`email`);

ALTER TABLE `Transacao` ADD FOREIGN KEY (`destinatario_email`) REFERENCES `Usuario` (`email`);

ALTER TABLE `Transacao` ADD FOREIGN KEY (`extrato_id`) REFERENCES `Extrato` (`id`);

ALTER TABLE `Aluno_Vantagem` ADD FOREIGN KEY (`aluno_cpf`) REFERENCES `Aluno` (`cpf`);

ALTER TABLE `Aluno_Vantagem` ADD FOREIGN KEY (`vantagem_id`) REFERENCES `Vantagem` (`id`);
