package com.learning.tend_calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
public class TendCalculatorApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(TendCalculatorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TendCalculatorApplication.class, args);
	}

	@Bean
	CommandLineRunner cli() {
		return args -> {
			LOGGER.info("Inicializando CLI de Tend Calculator");
			System.out.println("\n---- Tend Calculator (digite 'sair' para encerrar) ----");
			System.out.println("Informe primeiro o valor para o qual x tende (ex: 0, 2, inf, -inf) e depois a função f(x).");
			Scanner scanner = new Scanner(System.in);

			while (true) {
				System.out.print("X -> ");

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

				System.out.println(pointStr);
			}

			LOGGER.info("CLI finalizada");
		};
	}

}
