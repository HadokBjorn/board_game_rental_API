# Board Game Rental - API Documentation

A Board Game Rental API é uma aplicação de backend voltada para a gerência de aluguel de jogos de tabuleiro.

- Geral
    - O projeto foi realizado com versionamento usando Git.
    - Os commits foram implementado a cada funcionalidade e com mensagens semânticas neles.
    - A estrutura de arquivos e pastas do projeto segue o padrão `controllers`, `services`, `repositories`, `models` e `dtos`.
    - Há um `GlobalExceptionHandler` para lidar com os erros das requisições.
    - Para a persistência de dados, foi utilizado o PostgreSQL.
- Jogos
    - Formato de um jogo (tabela `games`)
        
        ```jsx
        {
          id: 1,
          name: 'Banco Imobiliário',
          image: 'http://',
          stockTotal: 3,
          pricePerDay: 1500
        }
        ```
        
    - Listar jogos
        - **Rota**: **GET** `/games`
        - **Response:** lista dos jogos encontrados, seguindo o formato abaixo e status `200 (OK)`.
            
            ```jsx
            [
              {
                id: 1,
                name: 'Banco Imobiliário',
                image: 'http://',
                stockTotal: 3,
                pricePerDay: 1500
              },
              {
                id: 2,
                name: 'Detetive',
                image: 'http://',
                stockTotal: 1,
                pricePerDay: 2500
              },
            ]
            ```
            
    - Inserir um jogo
        - **Rota**: **POST** `/games`
        - **Request**: body no formato abaixo.
            
            ```jsx
            {
              name: 'Banco Imobiliário',
              image: 'http://www.imagem.com.br/banco_imobiliario.jpg',
              stockTotal: 3,
              pricePerDay: 1500
            }
            ```
            
        - **Response:** status `201 (CREATED)`, retornando o jogo criado completo (com id).
            
            ```jsx
            {
                id: 1,
                name: 'Banco Imobiliário',
                image: 'http://www.imagem.com.br/banco_imobiliario.jpg',
                stockTotal: 3,
                pricePerDay: 1500
            }
            ```
            
        - **Regras:**
            - Validações ⇒ nesses casos, retorna status `400 (BAD REQUEST)`:
                - `name` se for nulo ou string vazia;
                - `stockTotal` e `pricePerDay` se forem nulos, se tiverem seu valor igual a zero e se forem negativos;
            
            - Nesse caso retorna ⇒ status `409 (CONFLICT)`

                - `name` se for um nome de jogo já existente.
- Clientes
    - Formato de um cliente (tabela `customers`)
        
        ```jsx
        {
          id: 1,
          name: 'João Alfredo',
          cpf: '01234567890'
        }
        ```
        
    - Buscar um cliente por id
        - **Rota: GET** `/customers/:id`
        - **Response:** somente o objeto do usuário com o id passado, como mostrado abaixo, e status `200 (OK)`.
            
            ```sql
            {
              id: 1,
              name: 'João Alfredo',
              cpf: '01234567890'
            }
            ```
            
        - **Regras:**
            - Se o cliente com id dado não existir, será respondido com status `404 (NOT FOUND)`.
    - Inserir um cliente
        - **Rota:** **POST** `/customers`
        - **Request:** body no formato abaixo.
            
            ```jsx
            {
              name: 'João Alfredo',
              cpf: '01234567890'
            }
            ```
            
        - **Response:** status `201 (CREATED)`, retornando o cliente criado completo (com id).
            
            ```jsx
            {
            	id: 1,
                name: 'João Alfredo',
                cpf: '01234567890'
            }
            ```
            
        - **Regras:**
            - Validações ⇒ nesses casos, deve retornar **status** `400 (BAD REQUEST)`:
                - `cpf` se for uma string com tamanho diferente de 11 caracteres, se for nulo ou string vazia;
                - `name` se não estiver presente no body e se for nulo ou string vazia;

            -nesse caso deve retornar ⇒  status `409 (CONFLICT)`
            - `cpf` se for de um cliente já existente `.
- Aluguéis
- Formato de um aluguel (tabela `rentals`)
    
    ```jsx
    {
      id: 1,
      customerId: 1,
      gameId: 1,
      rentDate: '2021-06-20',    // data em que o aluguel foi feito, formato **LocalDate**
      daysRented: 3,             // por quantos dias o cliente agendou o aluguel
      returnDate: null,          // data que o cliente devolveu o jogo (null enquanto não devolvido)
      originalPrice: 4500,       // preço total do aluguel em centavos (dias alugados vezes o preço por dia do jogo)
      delayFee: 0                // multa total paga por atraso (dias que passaram do prazo vezes o preço por dia do jogo), começa como 0
    }
    ```
    
- Listar aluguéis
    - **Rota: GET** `/rentals`
    - **Response:** lista com todos os aluguéis, contendo o `customer` e o `game` do aluguel em questão em cada aluguel, e status `200 (OK)`.
        
        ```jsx
        [
          {
            id: 1,
            rentDate: '2021-06-20',
            daysRented: 3,
            returnDate: null, // troca pra uma data quando já devolvido
            originalPrice: 4500,
            delayFee: 0, // troca por outro valor caso tenha devolvido com atraso
            customer: {
                id: 1,
                name: 'João Alfredo',
        	    cpf: '01234567890'
            },
            game: {
                id: 1,
        		name: 'Banco Imobiliário',
        		image: 'http://www.imagem.com.br/banco.jpg',
        		stockTotal: 3,
        		pricePerDay: 1500
            }
          }
        ]
        ```
        
- Inserir um aluguel
    - **Rota:** **POST** `/rentals`
    - **Request:** body no formato abaixo.
        
        ```jsx
        {
          customerId: 1,
          gameId: 1,
          daysRented: 3
        }
        ```
        
    - **Response:** status `201 (CREATED)`, retornando o aluguel criado completo (com id, customer e game).
        
        ```jsx
        {
            id: 1,
            rentDate: '2021-06-20',
            daysRented: 3,
            returnDate: null, 
            originalPrice: 4500,
            delayFee: 0, 
            customer: {
                id: 1,
                name: 'João Alfredo',
        	    cpf: '01234567890'
            },
            game: {
                id: 1,
        		name: 'Banco Imobiliário',
        		image: 'http://www.imagem.com.br/banco.jpg',
        		stockTotal: 3,
        		pricePerDay: 1500
            }
          }
        ```
        
    - **Regras:**
        - Ao inserir um aluguel, os campos `rentDate` e `originalPrice` são populados automaticamente:

            - `rentDate`: data atual no momento da inserção.

            - `originalPrice`: `daysRented` multiplicado pelo preço por dia do jogo no momento da inserção.

        - Ao inserir um aluguel, o campo `returnDate` começa como `null` e a `delayFee` começa como 0.

        - Validações ⇒ nesses casos, deve retornar **status** `400 (BAD REQUEST)`:

            - `daysRented` se for 0 ou negativo, se for nulo.

            - `gameId` e `customerId` se forem nulos.

        - Ao inserir um aluguel, é verificado se `gameId` se refere a um jogo existente. Se não, responde com status `404 (NOT FOUND)`.

        - Ao inserir um aluguel, é verificado se `customerId` se refere a um cliente existente. Se não, deve responder com status `404 (NOT FOUND)`.

        - Ao inserir um aluguel, é verificado se existem jogos disponíveis, ou seja, que não tem aluguéis em aberto acima da quantidade de jogos em estoque. Caso contrário, é retornado o status `422 (UNPROCESSABLE ENTITY)`.

- Finalizar aluguel

    - **Rota:** **PUT** `/rentals/:id/return`
    - **Response:** status `200 (OK)`, retornando o aluguel finalizado completo (com id, customer e game).
        
        ```jsx
        {
            id: 1,
            rentDate: '2021-06-20',
            daysRented: 3,
            returnDate: '2021-06-25', 
            originalPrice: 4500,
            delayFee: 3000, 
            customer: {
                id: 1,
                name: 'João Alfredo',
        		cpf: '01234567890'
            },
            game: {
                id: 1,
        		name: 'Banco Imobiliário',
        		image: 'http://www.imagem.com.br/banco.jpg',
        		stockTotal: 3,
        		pricePerDay: 1500
            }
          }
        ```
        
    - **Regras:**
        - É verificado se o `id` do aluguel fornecido existe. Se não, responde com status `404 (NOT FOUND)`.

        - Ao retornar um aluguel, é verificado se o aluguel já não está finalizado. Se estiver, responde com status `422 (UNPROCESSABLE ENTITY)`.

        - Ao retornar um aluguel, o campo `returnDate` é populado com a data atual do momento do retorno.

        - Ao retornar um aluguel, o campo `delayFee` é automaticamente populado com um valor equivalente ao número de dias de atraso vezes o preço por dia do jogo.
            - Exemplo: se o cliente alugou no dia **20/06** um jogo por **3 dias**, ele deveria devolver no dia **23/06**. Caso ele devolva somente no dia **25/06**, o sistema deve considerar **2 dias de atraso**. Nesse caso, se o jogo custava **R$ 15,00** por dia, a `delayFee` deve ser de **R$ 30,00** (3000 centavos).