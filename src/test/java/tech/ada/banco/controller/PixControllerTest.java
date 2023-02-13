package tech.ada.banco.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tech.ada.banco.model.Conta;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PixControllerTest extends BaseContaTest{
    private final String baseUri = "/pix";

    @Test
    void testPix() throws Exception {
        Conta contaBase1 = criarConta(BigDecimal.valueOf(5.00));
        Conta contaBase2 = criarConta(BigDecimal.valueOf(7.00));
        fail();

    }
    @Test
    void testPixQuebrado() throws Exception {
        Conta contaBase1 = criarConta(BigDecimal.valueOf(5.00));
        Conta contaBase2 = criarConta(BigDecimal.valueOf(7.00));
        fail();

    }
}