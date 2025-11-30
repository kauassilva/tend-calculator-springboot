# Tend Calculator

Uma aplicação Java que calcula limites de funções de uma variável utilizando a biblioteca Symja para avaliação simbólica.

## Requisitos
- Java 21 (definido no `pom.xml`)
- Maven (o projeto contém o wrapper `mvnw`, use-o)

## Estrutura do projeto (resumo)
- `src/main/java` - código da aplicação
  - `com.learning.tend_calculator.Application` - ponto de entrada (CLI)
  - `com.learning.tend_calculator.limit.LimitCalculator` - componente que usa Symja para avaliar limites
  - `com.learning.tend_calculator.limit.LimitResult` / `LimitContext` - modelos de resultado/contexto
- `src/test/java` - testes JUnit

## Como executar

1) Compilar o projeto

```bash
./mvnw -q -DskipTests=true compile
```

2) Rodar a aplicação em modo CLI (interativo)

Por padrão o CLI é ativado via propriedade `cli.enabled`.

```bash
# inicia a aplicação com o prompt interativo
./mvnw -q compile exec:java -Dexec.mainClass="com.learning.tend_calculator.Application" -Dcli.enabled=true
```

## Executando os testes unitários

```bash
./mvnw -q test
```

O projeto contém testes parametrizados que cobrem vários casos de limites (polinômios, frações, raízes, formas removíveis, etc.).

## Guia rápido de uso (expressões)

- Variável: use `x` (minúsculo) como variável independente.
- Operadores básicos:
  - Soma: `+`
  - Subtração: `-`
  - Multiplicação: `*`
  - Divisão: `/`
  - Potência: `^` (por exemplo `x^2`)
- Parênteses: `(` e `)` para agrupamento.
- Frações: expressas como `a/b` (por exemplo `9/4`).
- Funções suportadas (sintaxe compatível com Symja):
  - seno: `sin(x)`
  - cosseno: `cos(x)`
  - tangente: `tan(x)`
  - exponencial: `exp(x)`
  - logaritmo natural: `log(x)` (use `log` para ln)
  - raiz quadrada: `Sqrt(x)`
  - raiz cúbica: `CubeRoot(x)`
- Exemplos de expressões válidas:
  - `(x^2 - 1)/(x - 1)`
  - `9/4`
  - `(x^2 - 4)/(x - 2)`
  - `(-x^3 + 2 * x) / (x - 1)`
  - `CubeRoot((x^2 + 5*x + 3) / (x^2 - 1))`


## Exemplo de sessão (interativa)
```
x -> 1
Função f(x)> (x^2 - 1)/(x - 1)

Resultado: 2 ou 2.0
Passos:
 - Using Symja engine
 - Symja expression: Limit((x^2 - 1)/(x - 1), x->1)
 - Symja result: 2

```

## Observações e comportamento
- O projeto usa a biblioteca Symja para calcular limites simbolicamente. O componente `LimitCalculator` tenta:
  1. Substituição direta para detectar resultados imediatos.
  2. Se isso falhar, avalia `Limit(expression, x->point)` no Symja e tenta extrair um valor numérico (ou uma representação simbólica, como `19/9`).

## Boas práticas ao escrever expressões
- Use `x` para a variável e evite espaços desnecessários, embora espaços sejam tolerados.
- Para evitar ambiguidades, preferir `*` para multiplicação explícita (por exemplo `2 * x` em vez de `2x`).

## Licença
- Projeto de exemplo / educativo. Adapte conforme necessário.
