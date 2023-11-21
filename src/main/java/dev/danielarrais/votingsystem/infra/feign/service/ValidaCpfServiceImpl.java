package dev.danielarrais.votingsystem.infra.feign.service;

import dev.danielarrais.votingsystem.core.application.exceptions.PautaNaoEncontradaException;
import dev.danielarrais.votingsystem.core.application.service.out.ValidaCpfService;
import dev.danielarrais.votingsystem.infra.exceptions.FalhaAoValidarCPFNoInvertextException;
import dev.danielarrais.votingsystem.infra.feign.CpfValido;
import dev.danielarrais.votingsystem.infra.feign.InvertextClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidaCpfServiceImpl implements ValidaCpfService {
    private final InvertextClient invertextClient;

    @Override
    public boolean cpfValido(String cpf) {
        try {
            var cpfValido = invertextClient.cpfValido(cpf);
            return cpfValido != null && cpfValido.getValido();
        } catch (Exception e) {
            throw new FalhaAoValidarCPFNoInvertextException(e);
        }
    }
}
