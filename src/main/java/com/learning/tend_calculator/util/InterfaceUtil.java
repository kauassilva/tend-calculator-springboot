package com.learning.tend_calculator.util;

import static com.learning.tend_calculator.util.ConstantUtil.RED_BOLD;
import static com.learning.tend_calculator.util.ConstantUtil.RESET;

public final class InterfaceUtil {

    private InterfaceUtil() {}

    public static boolean isEmptyInput(String pointStr) {
        if (pointStr.isEmpty()) {
            System.out.println(RED_BOLD + "Valor inválido. Tente novamente." + RESET);
            return true;
        }

        return false;
    }

    public static boolean isExitInput(String input) {
        if (input.equalsIgnoreCase("sair") || input.equalsIgnoreCase("exit")) {
            System.out.println("Encerrando...");
            return true;
        }

        return false;
    }
}
