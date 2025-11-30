package com.learning.tend_calculator;

import com.learning.tend_calculator.limit.LimitCalculatorService;
import com.learning.tend_calculator.limit.LimitContext;
import com.learning.tend_calculator.limit.LimitResult;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

import static com.learning.tend_calculator.util.ConstantUtil.*;
import static com.learning.tend_calculator.util.InterfaceUtil.isEmptyInput;
import static com.learning.tend_calculator.util.InterfaceUtil.isExitInput;
import static com.learning.tend_calculator.util.LimitUtil.isInfinity;
import static com.learning.tend_calculator.util.LimitUtil.parsePoint;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
    @ConditionalOnProperty(name = "cli.enabled", havingValue = "true")
	CommandLineRunner cli(LimitCalculatorService calculator) {
		return args -> {
			System.out.println(BLUE_BOLD + "\n---- Calculadora de Limites (digite 'sair' ou 'exit' para encerrar) ----" + RESET);
			System.out.println("Informe primeiro o valor para o qual x tende (ex: 0, 2, inf, -inf) e depois a função f(x).");
            System.out.println("Exemplo:");
            System.out.println("X -> 0");
            System.out.println("Função f(x)> (x^2 - 1)/(x - 1)");
			Scanner scanner = new Scanner(System.in);

			while (true) {
				System.out.print(GREEN_BOLD + "\nx -> " + RESET);

				if (!scanner.hasNextLine())
					break;

				String pointStr = scanner.nextLine().trim();

                if (isExitInput(pointStr)) break;
                if (isEmptyInput(pointStr)) continue;

                Double point = parsePoint(pointStr);

				if (point == null && !isInfinity(pointStr)) {
					System.out.println("Ponto inválido. Use número ou inf/-inf.");
					continue;
				}

				System.out.print("Função f(x)> ");

				if (!scanner.hasNextLine())
					break;

				String expressionStr = scanner.nextLine().trim();

                if (isExitInput(expressionStr)) break;
                if (isEmptyInput(expressionStr)) continue;

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
}
