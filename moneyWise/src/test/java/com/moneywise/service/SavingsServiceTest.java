package com.moneywise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class SavingsServiceTest {

    private final SavingsService service = new SavingsService();

    @Test
    void deveProjetarJurosCompostos() {
        BigDecimal resultado = service.projectSavings(
                new BigDecimal("1000"), new BigDecimal("0.01"), 12);

        // 1000 × (1.01)^12 = 1126.8250... → arredonda para comparação
        assertEquals(new BigDecimal("1126.83"),
                resultado.setScale(2, java.math.RoundingMode.HALF_UP));
    }

    @Test
    void deveRetornarCapitalInalteradoComZeroMeses() {
        BigDecimal resultado = service.projectSavings(
                new BigDecimal("500"), new BigDecimal("0.02"), 0);

        assertEquals(new BigDecimal("500"), resultado);
    }

    @Test
    void deveLancarExcecaoComMesesNegativos() {
        assertThrows(IllegalArgumentException.class, () ->
                service.projectSavings(new BigDecimal("100"), new BigDecimal("0.01"), -1));
    }
}