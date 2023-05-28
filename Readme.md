# Equipa

* Tiago Pinto 55172
* Ricardo Soares 54446
* Miguel Reis 54409

# Notas
O pre-commit não foi utilizado. <br>
Por lapso, o Ricardo Soares utilizou duas contas ao realizar commits (fc54446 (fc54446@alunos.fc.ul.pt) e ricardo ( ricardomcs2001@gmail.com)), e o Miguel Reis utilizou três contas (heymimal (mimalreis@gmail.com), fc54409 (fc54409@alunos.ciencias.ulisboa.pt), Miguel Reis (fc54409@alunos.fc.ul.pt)).<br>
O programa foi testado utilizando Java17 como ambiente de execução (na máquina virtual fornecida pelo DI).


# Projecto Democracia 2.0

No modelo proposto, pretende-se que os eleitores tenham a hipótese de votar directamente em todas as<br>
propostas de lei que vão a votação actualmente no parlamento. Assim, cada cidadão poderá fazer-se ouvir<br>
sobre cada assunto que lhe diz respeito. Este modelo é superior ao actual em virtude de que ao escolher um<br>
representante, o cidadão está a atribuir todos os seus votos à(s) mesma(s) pessoa(s), independentemente<br>
do tema. Com o novo modelo, o cidadão pode escolher uma sequência de votos que não corresponderia a<br>
nenhum deputado, até mesmo os que não o representam.

No entanto, ao perder o conceito de deputados, também se perde a vantagem dos cidadãos não terem de se<br>
preocupar com todos os aspectos da governação e legislação. Muito do trabalho dos deputados é de desenho<br>
de propostas, estudo para compreensão das mesmas e estudo do impacto que têm na vida dos portugueses.<br>
De forma a manter a hipótese de delegar votos, é possível uma pessoa voluntariar-se como delegado<br> (semelhante ao papel do actual deputado, mas sem eleições). Como delegado, os seus votos passam a ser públicos.<br>
No entanto passa a existir a possibilidade de qualquer cidadão delegar os seus votos a um delegado, seja na<br>
totalidade, ou por tema (saúde, educação, obras públicas, imigração, etc...).

## Fase 1
### Casos de Uso
 * **Listar as votações em curso.** Este caso de uso permite obter uma listagem das propostas de lei
    em votação neste momento.

 * **Apresentar um projecto de lei.** Neste caso de uso, um delegado poderá propor um projecto
de lei. Um projecto de lei é <br> constituído por um título, um texto descriptivo, um anexo PDF com o 
conteúdo principal do projecto de lei, uma data e hora de <br> validade (máximo de um ano), um tema
associado e o delegado proponente.
 * **Fechar projectos de lei expirados.** Todos os projectos de lei cuja data limite já tenha decorrido
deverão ser fechados, sendo que <br> não podem receber mais nenhuma assinatura.
 * **Consultar projectos de lei.** Deve ser possível listar e consultar os projectos de lei não expirados.
 * **Apoiar projectos de lei.** Cada projecto de lei pode ser apoiado por cidadãos (no limite de 1 apoio
por cidadão). Quando um projecto <br> de lei tiver pelo menos 10.000 apoios, é criada uma votação para
esse projecto de lei imediatamente, com uma data de fecho igual à <br> data de expiração do projecto de
lei, com um limite mínimo de 15 dias e um limite máximo de 2 meses. Ao abrir a votação, é lançado <br>
automàticamente o voto do delegado proponente, como favorável.
 * **Escolher delegado.** Um cidadão pode escolher vários delegados, mas apenas um para cada
tema. Ou seja, pode ter um delegado para <br> o tema de Saúde, um para Educação e um para outros
temas. Quando fecha uma votação onde o cidadão não votou, é feito um voto <br> automático com base
no delegado do tema mais específico da proposta de lei.
 * **Votar numa proposta.** Um cidadão deve pedir uma listagem das votações e escolher a que lhe
interessa. Deverá ver qual o voto por <br> omissão caso não o faça explicitamente (isto é, o voto do seu
delegado para o tópico), caso este esteja disponível. Caso não concorde,<br> o cidadãopoderá lançar o seu
voto (favorável ou desfavorável, não existem votos em branco nem nulos) numa proposta concreta.<br>
Deve ser verificado se o cidadão já votou nesta proposta em concreto, mas não deve ser registado em
que opção foi o voto <br> (dica: poderá ter de dividir o voto em duas partes, a do cidadão e a do conteúdo).
Se o cidadão for também um delegado, então <br> deverá ser registado um voto público, que qualquer um
poderá confirmar.
 * **Fechar uma votação.** Assim que termina o prazo de uma votação, são atribuídos os votos dos
delegados para cada cidadão que <br> não tenha votado explicitamente. Depois são contados os votos,
e se mais de metade dos votos forem favoráveis, então a proposta <br> é fechada como aprovada. Caso
contrário é fechada como rejeitada.

## Testes

Para correr os testes desenvolvidos pela equipa, executar o script **test.sh** na raiz do projeto.


## Dependências

Este projecto vai usar Java17+ e Postgres, mas esses vão estar disponíveis dentro dos containers Docker. Só são necessários se quiser correr a aplicação nativamente.

### Windows

Instalar [Docker Desktop](https://docs.docker.com/desktop/install/windows-install/) e [Python 3](https://www.python.org/ftp/python/3.11.2/python-3.11.2-amd64.exe)

### Ubuntu

```
sudo apt-get python3 python3-pip
```

Seguir [esta página](https://docs.docker.com/engine/install/ubuntu/) e os quatro passos listados [aqui](https://docs.docker.com/engine/install/linux-postinstall/#manage-docker-as-a-non-root-user).

### Fedora

```
sudo dnf python3 python3-pip
```

Seguir [esta página](https://docs.docker.com/engine/install/fedora/) e os quatro passos listados [aqui](https://docs.docker.com/engine/install/linux-postinstall/#manage-docker-as-a-non-root-user).


## Primeiro passo

Deve correr os comandos listados no ficheiro `setup.sh`.

## Segundo passo

Deve correr `run.sh`.

Este comando vai iniciar dois containers:

* Um com a aplicação que existe nesta pasta.
* Uma instância de um container com Postgres

## Terceiro passo

Abrir http://localhost:8080 num browser e verificar que aparece a string "Olá Mundo".

Pode ainda confirmar que consegue ver um objecto JSON em http://localhost:8080/api/cidadao/1


# FAQ

## Preciso de `sudo` para correr o `run.sh`
Tenta correr `sudo usermod -aG docker $USER` seguido de um log-out na máquina.
Ou tentar [desta forma](https://www.digitalocean.com/community/questions/how-to-fix-docker-got-permission-denied-while-trying-to-connect-to-the-docker-daemon-socket)

## O Docker não instala em ubuntu.

Tentar [desta forma](https://askubuntu.com/a/1411717).

## O `run.sh` não está a correr no meu macos m1.

Tentar correr `docker ps`. Se não funcionar, [tentar isto](https://stackoverflow.com/a/68202428/28516).
Confirmar também que está instalado o Docker Desktop (`brew install --cask docker`) e não apenas a command-line tool (`brew install docker`). A aplicação Docker deve também estar a correr (icon na menubar).


## Estou em windows e o `bash setup.sh` não funciona

Correr numa bash (tanto a Git Bash, MSys2 bash ou WSL em linux) e não na Powershell, nem no CMD.exe.

## `docker compose` não funciona

`docker compose` é o comando da última versão de docker. `docker-compose` é a versão antiga. Devem actualizar o docker.
