package dev.danielarrais.votingsystem.core.application.service.in.impl;

import dev.danielarrais.votingsystem.core.application.exceptions.CpfNaoAutorizadoAVotarException;
import dev.danielarrais.votingsystem.core.application.exceptions.PautaNaoEncontradaException;
import dev.danielarrais.votingsystem.core.application.exceptions.PautaSemSessaoAbertaException;
import dev.danielarrais.votingsystem.core.application.exceptions.VotoJaRegistradoException;
import dev.danielarrais.votingsystem.core.application.service.in.RegistrarVotoUserCase;
import dev.danielarrais.votingsystem.core.application.service.out.*;
import dev.danielarrais.votingsystem.core.domain.Voto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegistrarVotoUserCaseImpl implements RegistrarVotoUserCase {
    private final ValidaCpfService validaCpfService;
    private final RecuperarPautaService recuperarPautaService;
    private final RecuperarSessaoService recuperarSessaoService;
    private final RecuperarVotosService recuperarVotosService;
    private final SalvarVotoService salvarVotoService;

    public void votar(Voto voto) {
        validaSePautaExiste(voto.getPautaId());
        validaAutorizacaoDeVoto(voto.getCpf());
        validaSeSessaoDaPautaEstarAberta(voto.getPautaId(), voto.getHora());
        validaSeJaVotou(voto.getPautaId(), voto.getCpf());

        salvarVotoService.salvar(voto);
    }

    private void validaSeJaVotou(Long pautaId, String cpf) {
        boolean jaVotou = recuperarVotosService.existsByPautaIdAndCpf(pautaId, cpf);

        if (jaVotou) {
            throw new VotoJaRegistradoException(cpf);
        }
    }

    private void validaSePautaExiste(Long pautaId) {
        boolean pautaExiste = recuperarPautaService.existe(pautaId);

        if (!pautaExiste) {
            throw new PautaNaoEncontradaException(pautaId);
        }
    }

    private void validaAutorizacaoDeVoto(String CPF) {
        boolean podeVotar = cpfAutorizadoAVotar(CPF);

        if (!podeVotar) {
            throw new CpfNaoAutorizadoAVotarException(CPF);
        }
    }

    private void validaSeSessaoDaPautaEstarAberta(Long pautaId, LocalDateTime horaDoVoto) {
        boolean pautaAberta = recuperarSessaoService.existePautaAberta(pautaId, horaDoVoto);

        if (!pautaAberta) {
            throw new PautaSemSessaoAbertaException(pautaId);
        }

    }

    private boolean cpfAutorizadoAVotar(String cpf) {
        return validaCpfService.cpfValido(cpf);
    }
}
