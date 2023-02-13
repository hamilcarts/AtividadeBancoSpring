package tech.ada.banco.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import tech.ada.banco.model.Conta;
import tech.ada.banco.model.ModalidadeConta;
import tech.ada.banco.repository.ContaRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@AutoConfigureMockMvc
abstract class BaseContaTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ContaRepository repository;

    protected Conta criarConta(BigDecimal saldo) {
        Conta contaBase = repository.save(new Conta(ModalidadeConta.CC, null));
        contaBase.deposito(saldo);
        contaBase = repository.save(contaBase);
        assertEquals(saldo, contaBase.getSaldo());
        return contaBase;
    }

    protected Conta obtemContaDoBanco(Conta contaBase) {
        return repository.findContaByNumeroConta(contaBase.getNumeroConta())
                .orElseThrow(NullPointerException::new);
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
