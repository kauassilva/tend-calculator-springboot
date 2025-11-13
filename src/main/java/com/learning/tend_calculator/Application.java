package com.learning.tend_calculator;

import com.learning.tend_calculator.limit.LimitCalculator;
import com.learning.tend_calculator.limit.LimitContext;
import com.learning.tend_calculator.limit.LimitResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
public class Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static final String RESET = "\033[0m";
    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String RED_BOLD = "\033[1;31m";
    public static final String BLUE_BOLD = "\033[1;34m";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner cli(LimitCalculator calculator) {
		return args -> {
			System.out.println(BLUE_BOLD + "\n---- Calculadora de Limites (digite 'sair' ou 'exit' para encerrar) ----" + RESET);
			System.out.println("Informe primeiro o valor para o qual x tende (ex: 0, 2, inf, -inf) e depois a função f(x).");
            System.out.println("Exemplo:");
            System.out.println("X tende a> 0");
            System.out.println("Função f(x)> (x^2 - 1)/(x - 1)");
			Scanner scanner = new Scanner(System.in);

			while (true) {
				System.out.print(GREEN_BOLD + "\nX tende a> " + RESET);

				if (!scanner.hasNextLine())
					break;

				String pointStr = scanner.nextLine().trim();

				if (pointStr.equalsIgnoreCase("sair") || pointStr.equalsIgnoreCase("exit")) {
					System.out.println("Encerrando.");
					break;
				}
				if (pointStr.isEmpty()) {
					System.out.println(RED_BOLD + "Valor inválido. Tente novamente." + RESET);
					continue;
				}

				Double point = parsePoint(pointStr);

				if (point == null && !isInfinity(pointStr)) {
					System.out.println("Ponto inválido. Use número ou inf/-inf.");
					continue;
				}

				System.out.print("Função f(x)> ");

				if (!scanner.hasNextLine())
					break;

				String expressionStr = scanner.nextLine().trim();

				if (expressionStr.equalsIgnoreCase("sair") || expressionStr.equalsIgnoreCase("exit")) {
					System.out.println("Encerrando...");
					break;
				}
				if (expressionStr.isEmpty()) {
					System.out.println("Expressão vazia. Tente novamente.");
					continue;
				}

				LimitContext context = LimitContext.at(pointStr);

				try {
					LimitResult result = calculator.computeLimit(expressionStr, context);
					System.out.println(result.format());
				} catch (Exception e) {
					System.out.println("Erro ao calcular: " + e.getMessage());
				}
			}
		};
	}

	private boolean isInfinity(String s) {
		return s.equalsIgnoreCase("inf")
				|| s.equalsIgnoreCase("+inf")
				|| s.equalsIgnoreCase("-inf")
				|| s.equalsIgnoreCase("infinity")
				|| s.equalsIgnoreCase("-infinity");
	}

	private Double parsePoint(String s) {
		if (isInfinity(s))
			return null;

		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException ex) {
			return null;
		}
	}
}
