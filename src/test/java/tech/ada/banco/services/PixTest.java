package tech.ada.banco.services;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.ada.banco.exceptions.ResourceNotFoundException;
import tech.ada.banco.exceptions.SaldoInsuficienteException;
import tech.ada.banco.model.Conta;
import tech.ada.banco.model.ModalidadeConta;
import tech.ada.banco.repository.ContaRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PixTest {
    private final ContaRepository repository = Mockito.mock(ContaRepository.class);
    private final Pix pix = new Pix(repository);

    @Test
    void pixPositivo(){
        Conta conta1 = new Conta(ModalidadeConta.CC, null);
        Conta conta2 = new Conta(ModalidadeConta.CC, null);
        when(repository.findContaByNumeroConta(10)).thenReturn(Optional.of(conta1));
        when(repository.findContaByNumeroConta(11)).thenReturn(Optional.of(conta2));
        conta1.deposito( BigDecimal.TEN);
        conta2.deposito(BigDecimal.ONE);
        pix.executar(10, 11, BigDecimal.valueOf(7));
        assertEquals(BigDecimal.valueOf(3), conta1.getSaldo(), "O saldo da conta deve ser alterado para 3");
        assertEquals(BigDecimal.valueOf(8), conta2.getSaldo(), "O saldo da conta deve ser alterado para 8");


    }
    @Test
    void pixPositivoQuebrado() {
        Conta conta1 = new Conta(ModalidadeConta.CC, null);
        Conta conta2 = new Conta(ModalidadeConta.CC, null);
        when(repository.findContaByNumeroConta(10)).thenReturn(Optional.of(conta1));
        when(repository.findContaByNumeroConta(11)).thenReturn(Optional.of(conta2));
        conta1.deposito( BigDecimal.TEN);
        conta2.deposito(BigDecimal.ONE);
        pix.executar(10, 11, BigDecimal.valueOf(7.3));
        assertEquals(BigDecimal.valueOf(2.7), conta1.getSaldo(), "O saldo da conta deve ser alterado para 2.7");
        assertEquals(BigDecimal.valueOf(8.3), conta2.getSaldo(), "O saldo da conta deve ser alterado para 8.3");

    }
    @Test
    void pixContaRemetenteInvalida() {
        Conta conta1 = new Conta(ModalidadeConta.CC, null);
        Conta conta2 = new Conta(ModalidadeConta.CC, null);
        when(repository.findContaByNumeroConta(10)).thenReturn(Optional.of(conta1));
        when(repository.findContaByNumeroConta(11)).thenReturn(Optional.of(conta2));
        conta1.deposito( BigDecimal.TEN);
        conta2.deposito(BigDecimal.ONE);
        try {
            pix.executar(17, 11, BigDecimal.valueOf(7.3));
            fail("A conta deveria não ter sido encontrada.");
        } catch (ResourceNotFoundException e) {

        }


    }
    @Test
    void pixContaDestinatarioInvalida() {
        Conta conta1 = new Conta(ModalidadeConta.CC, null);
        Conta conta2 = new Conta(ModalidadeConta.CC, null);
        when(repository.findContaByNumeroConta(10)).thenReturn(Optional.of(conta1));
        when(repository.findContaByNumeroConta(11)).thenReturn(Optional.of(conta2));
        conta1.deposito( BigDecimal.TEN);
        conta2.deposito(BigDecimal.ONE);
        try {
            pix.executar(10, 17, BigDecimal.valueOf(7.3));
            fail("A conta deveria não ter sido encontrada.");
        } catch (ResourceNotFoundException e) {

        }
    }
    @Test
    void pixContaSemSaldo() {
        Conta conta1 = new Conta(ModalidadeConta.CC, null);
        Conta conta2 = new Conta(ModalidadeConta.CC, null);
        when(repository.findContaByNumeroConta(10)).thenReturn(Optional.of(conta1));
        when(repository.findContaByNumeroConta(11)).thenReturn(Optional.of(conta2));
        assertThrows(SaldoInsuficienteException.class, () -> pix.executar(10, 11,BigDecimal.valueOf(6)));
    }
    @Test
    void pixNegativo() {
        Conta conta1 = new Conta(ModalidadeConta.CC, null);
        Conta conta2 = new Conta(ModalidadeConta.CC, null);
        when(repository.findContaByNumeroConta(10)).thenReturn(Optional.of(conta1));
        when(repository.findContaByNumeroConta(11)).thenReturn(Optional.of(conta2));
        conta1.deposito( BigDecimal.TEN);
        conta2.deposito(BigDecimal.ONE);
        assertThrows(IllegalArgumentException.class, () -> pix.executar(10, 11,BigDecimal.valueOf(-7)));
        assertEquals(BigDecimal.valueOf(10), conta1.getSaldo(), "O saldo da conta deve ser 10");
        assertEquals(BigDecimal.valueOf(1), conta2.getSaldo(), "O saldo da conta deve ser 1");
    }
}