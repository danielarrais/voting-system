package dev.danielarrais.votingsystem.application;

import dev.danielarrais.votingsystem.infra.feign.CPFValido;
import dev.danielarrais.votingsystem.infra.feign.InvertextClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VotarService {
    private  final InvertextClient invertextClient;

    public void votar(String CPF, boolean voto) {

    }

    public boolean ehAutorizadoAVotar(String CPF) {
        CPFValido cpfValido = invertextClient.cpfValido(CPF);

        if (cpfValido != null) {
            return cpfValido.getValido();
        }

        return false;
    }
}
