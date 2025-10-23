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

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner cli(LimitCalculator calculator) {
		return args -> {
			LOGGER.info("Inicializando CLI de Tend Calculator");
			System.out.println("\n---- Tend Calculator (digite 'sair' para encerrar) ----");
			System.out.println("Informe primeiro o valor para o qual x tende (ex: 0, 2, inf, -inf) e depois a função f(x).");
			Scanner scanner = new Scanner(System.in);

			while (true) {
				System.out.print("\nX -> ");

				if (!scanner.hasNextLine()) {
					LOGGER.info("EOF detectado, encerrando CLI");
					break;
				}

				String pointStr = scanner.nextLine().trim();

				if (pointStr.equalsIgnoreCase("sair") || pointStr.equalsIgnoreCase("exit")) {
					LOGGER.info("Comando de saída recebido");
					System.out.println("Encerrando...");
					break;
				}
				if (pointStr.isEmpty()) {
					System.out.println("Valor inválido. Tente novamente.");
					continue;
				}

				Double point = parsePoint(pointStr);

				if (point == null && !isInfinity(pointStr)) {
					System.out.println("Ponto inválido. Use número ou inf/-inf.");
					continue;
				}

				System.out.print("lim ()> ");

				if (!scanner.hasNextLine()) {
					LOGGER.info("EOF após ponto, encerrando CLI");
					break;
				}

				String expressionStr = scanner.nextLine().trim();

				if (expressionStr.equalsIgnoreCase("sair") || expressionStr.equalsIgnoreCase("exit")) {
					LOGGER.info("Comando de saída recebido");
					System.out.println("Encerrando...");
					break;
				}
				if (expressionStr.isEmpty()) {
					System.out.println("Expressão vazia. Tente novamente.");
					continue;
				}

				LimitContext context = LimitContext.at(pointStr);

				try {
					LOGGER.debug("Calculando limite: expressao='{}' ponto='{}'", expressionStr, pointStr);
					LimitResult result = calculator.computeLimit(expressionStr, context);
					System.out.println(result.format());
				} catch (Exception e) {
					LOGGER.error("Erro ao calcular limite para expressao='{}' ponto='{}'", expressionStr, pointStr);
					System.out.println("Erro ao calcular: " + e.getMessage());
				}
			}

			LOGGER.info("CLI finalizada");
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
		if (isInfinity(s)) {
			return null;
		}

		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException ex) {
			return null;
		}
	}

}
