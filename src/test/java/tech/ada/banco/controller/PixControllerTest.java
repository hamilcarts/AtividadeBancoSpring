package tech.ada.banco.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tech.ada.banco.model.Conta;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PixControllerTest extends BaseContaTest{
    private final String baseUri = "/pix";

    @Test
    void testPix() throws Exception {
        Conta contaBase1 = criarConta(BigDecimal.valueOf(10.00));
        Conta contaBase2 = criarConta(BigDecimal.valueOf(7.00));

        String response =
                mvc.perform(post(baseUri + "/" + contaBase1.getNumeroConta())
                                .param("valor", "5")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
//                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        contaBase1 = obtemContaDoBanco(contaBase1);
        assertEquals("5", response);
        assertEquals(BigDecimal.valueOf(15), contaBase1.getSaldo());


    }
    @Test
    void testPixQuebrado() throws Exception {
        Conta contaBase1 = criarConta(BigDecimal.valueOf(5.00));
        Conta contaBase2 = criarConta(BigDecimal.valueOf(7.00));
        fail();

    }
    @Test
    void testPixNegativo() throws Exception {
        Conta contaBase1 = criarConta(BigDecimal.valueOf(5.00));
        Conta contaBase2 = criarConta(BigDecimal.valueOf(7.00));
        fail();
    }
    @Test
    void testPixContaRemetenteInvalido() throws Exception {
        Conta contaBase1 = criarConta(BigDecimal.valueOf(5.00));
        Conta contaBase2 = criarConta(BigDecimal.valueOf(7.00));
        fail();
    }
    @Test
    void testPixContaDestinoInvalido() throws Exception {
        Conta contaBase1 = criarConta(BigDecimal.valueOf(5.00));
        Conta contaBase2 = criarConta(BigDecimal.valueOf(7.00));
        fail();
    }
}