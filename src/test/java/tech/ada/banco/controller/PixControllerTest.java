package tech.ada.banco.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import tech.ada.banco.model.Conta;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PixControllerTest extends BaseContaTest {
    private final String baseUri = "/pix";

    @Test
    void testPix() throws Exception {
        Conta contaBase1 = criarConta(BigDecimal.valueOf(10.00));
        Conta contaBase2 = criarConta(BigDecimal.valueOf(7.00));

        String response =
                mvc.perform(post(baseUri + "/" + contaBase1.getNumeroConta())
                                .param("valor", "5")
                                .param("destino", String.valueOf(contaBase2.getNumeroConta()))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();
        contaBase1 = obtemContaDoBanco(contaBase1);
        assertEquals("5.0", response);
        assertEquals(0, contaBase1.getSaldo().compareTo(BigDecimal.valueOf(5)));
        contaBase2 = obtemContaDoBanco(contaBase2);
        assertEquals(0, contaBase2.getSaldo().compareTo(BigDecimal.valueOf(12)));
    }

    @Test
    void testPixQuebrado() throws Exception {
        Conta contaBase1 = criarConta(BigDecimal.valueOf(5.00));
        Conta contaBase2 = criarConta(BigDecimal.valueOf(7.00));
        String response =
                mvc.perform(post(baseUri + "/" + contaBase1.getNumeroConta())
                                .param("valor", "3.45")
                                .param("destino", String.valueOf(contaBase2.getNumeroConta()))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();
        contaBase1 = obtemContaDoBanco(contaBase1);
        assertEquals("1.55", response);
        assertEquals(0, contaBase1.getSaldo().compareTo(BigDecimal.valueOf(1.55)));
        contaBase2 = obtemContaDoBanco(contaBase2);
        assertEquals(0, contaBase2.getSaldo().compareTo(BigDecimal.valueOf(10.45)));
    }

    @Test
    void testPixNegativo() throws Exception {
        Conta contaBase1 = criarConta(BigDecimal.valueOf(5.00));
        Conta contaBase2 = criarConta(BigDecimal.valueOf(7.00));
        try {
            String response =
                    mvc.perform(post(baseUri + "/" + contaBase1.getNumeroConta())
                                    .param("valor", "-3")
                                    .param("destino", String.valueOf(contaBase2.getNumeroConta()))
                                    .contentType(MediaType.APPLICATION_JSON))
                            .andDo(print())
                            .andExpect(status().isInternalServerError())
                            .andReturn().getResponse().getErrorMessage();

        }catch (Exception e){
            assertTrue(e.getMessage().contains("Operação não foi realizada pois o valor da transação é negativo."));
        }
    }

    @Test
    void testPixSemSaldo() throws Exception {
        Conta contaBase1 = criarConta(BigDecimal.valueOf(5.00));
        Conta contaBase2 = criarConta(BigDecimal.valueOf(7.00));
        String response =
                mvc.perform(post(baseUri + "/" + contaBase1.getNumeroConta())
                                .param("valor", "10")
                                .param("destino", String.valueOf(contaBase2.getNumeroConta()))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andReturn().getResponse().getContentAsString();
        contaBase1 = obtemContaDoBanco(contaBase1);
        assertEquals("", response);
        assertEquals(0, contaBase1.getSaldo().compareTo(BigDecimal.valueOf(5)));
        contaBase2 = obtemContaDoBanco(contaBase2);
        assertEquals(0, contaBase2.getSaldo().compareTo(BigDecimal.valueOf(7)));
    }

    @Test
    void testPixContaRemetenteInvalido() throws Exception {
        Conta contaBase1 = criarConta(BigDecimal.valueOf(5.00));
        Conta contaBase2 = criarConta(BigDecimal.valueOf(7.00));
        String response =
                mvc.perform(post(baseUri + "/" + "9999")
                                .param("valor", "10")
                                .param("destino", String.valueOf(contaBase2.getNumeroConta()))
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andReturn().getResponse().getContentAsString();
        contaBase1 = obtemContaDoBanco(contaBase1);
        assertEquals("", response);
        assertEquals(0, contaBase1.getSaldo().compareTo(BigDecimal.valueOf(5)));
        contaBase2 = obtemContaDoBanco(contaBase2);
        assertEquals(0, contaBase2.getSaldo().compareTo(BigDecimal.valueOf(7)));
    }

    @Test
    void testPixContaDestinoInvalido() throws Exception {
        Conta contaBase1 = criarConta(BigDecimal.valueOf(5.00));
        Conta contaBase2 = criarConta(BigDecimal.valueOf(7.00));
        String response =
                mvc.perform(post(baseUri + "/" + contaBase1.getNumeroConta())
                                .param("valor", "10")
                                .param("destino", "999")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andDo(print())
                        .andExpect(status().isNotFound())
                        .andReturn().getResponse().getContentAsString();
        contaBase1 = obtemContaDoBanco(contaBase1);
        assertEquals("", response);
        assertEquals(0, contaBase1.getSaldo().compareTo(BigDecimal.valueOf(5)));
        contaBase2 = obtemContaDoBanco(contaBase2);
        assertEquals(0, contaBase2.getSaldo().compareTo(BigDecimal.valueOf(7)));
    }
}