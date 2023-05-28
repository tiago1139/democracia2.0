INSERT INTO TEMA (id, nome)
VALUES (1, 'Geral');

INSERT INTO CIDADAO (id, dtype, nome, apelido)
VALUES(1,'Delegado', 'Tomas', 'Delegado');

INSERT INTO PROJETO_LEI (dtype, id, apoios, descricao, fechado, titulo, validade, aprovada, votos_desfavoraveis, votos_favoraveis, proponente_id, tema_id)
VALUES('PropostaLei', 1, 0, 'descricao da proposta1', false, 'Proposta1', TIMESTAMP '2005-05-13 07:15', false, 0, 0, 1, 1);

INSERT INTO PROJETO_LEI (dtype, id, apoios, descricao, fechado, titulo, validade, aprovada, votos_desfavoraveis, votos_favoraveis, proponente_id, tema_id)
VALUES('PropostaLei', 2, 0, 'descricao da proposta2', false, 'Proposta2', TIMESTAMP '2015-05-13 07:15', false, 0, 0, 1, 1);

INSERT INTO PROJETO_LEI (dtype, id, apoios, descricao, fechado, titulo, validade, aprovada, votos_desfavoraveis, votos_favoraveis, proponente_id, tema_id)
VALUES('PropostaLei', 3, 0, 'descricao da proposta3', false, 'Proposta3', TIMESTAMP '2010-05-13 07:15', false, 0, 0, 1, 1);

INSERT INTO PROJETO_LEI (dtype, id, apoios, descricao, fechado, titulo, validade, aprovada, votos_desfavoraveis, votos_favoraveis, proponente_id, tema_id)
VALUES('PropostaLei', 4, 0, 'descricao da proposta4', false, 'Proposta4', TIMESTAMP '2014-05-13 07:15', false, 0, 0, 1, 1);