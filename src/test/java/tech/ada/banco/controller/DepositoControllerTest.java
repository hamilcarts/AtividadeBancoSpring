package tech.ada.banco.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tech.ada.banco.model.Conta;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DepositoControllerTest extends BaseContaTest {
    private final String baseUri = "/deposito";

    @Test
    void testDeposito() throws Exception {
        Conta contaBase = criarConta(BigDecimal.TEN);

        String response =
                mvc.perform(post(baseUri + "/" + contaBase.getNumeroConta())
                                .param("valor", "5")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        contaBase = obtemContaDoBanco(contaBase);
        assertEquals("15", response);
        assertEquals(BigDecimal.valueOf(15), contaBase.getSaldo());
    }

    @Test
    void testDepositoQuebrado() throws Exception {
        Conta contaBase = criarConta(BigDecimal.TEN);

        String response =
                mvc.perform(post(baseUri + "/" + contaBase.getNumeroConta())
                                .param("valor", "5.5")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        contaBase = obtemContaDoBanco(contaBase);
        assertEquals("15.5", response);
        assertEquals(BigDecimal.valueOf(15.5).setScale(1), contaBase.getSaldo());
    }

    @Test
    void testDepositoNegativo() throws Exception {
        Conta contaBase = criarConta(BigDecimal.TEN);

        String response =
                mvc.perform(post(baseUri + "/" + contaBase.getNumeroConta())
                                .param("valor", "-5")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isBadRequest())
                        .andReturn().getResponse().getErrorMessage();

        contaBase = obtemContaDoBanco(contaBase);
        assertEquals("Valor informado está inválido.", response);
        assertEquals(BigDecimal.TEN, contaBase.getSaldo());
    }

    @Test
    void testDepositoContaInvalida() throws Exception {
        Conta contaBase = criarConta(BigDecimal.TEN);
        Optional<Conta> contaInexistente = repository.findContaByNumeroConta(9999);
        assertTrue(contaInexistente.isEmpty());

        String response =
                mvc.perform(post(baseUri + "/9999")
                                .param("valor", "7")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andReturn().getResponse().getContentAsString();

        contaBase = obtemContaDoBanco(contaBase);
        assertEquals(BigDecimal.valueOf(10), contaBase.getSaldo());
    }
}