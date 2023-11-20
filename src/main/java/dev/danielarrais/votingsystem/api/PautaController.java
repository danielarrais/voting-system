package dev.danielarrais.votingsystem.api;

import dev.danielarrais.votingsystem.api.dto.request.PautaRequest;
import dev.danielarrais.votingsystem.api.dto.request.VotoRequest;
import dev.danielarrais.votingsystem.application.CriarPautaService;
import dev.danielarrais.votingsystem.application.CriarSessaoService;
import dev.danielarrais.votingsystem.application.RecuperarResultadoService;
import dev.danielarrais.votingsystem.application.RegistrarVotoService;
import dev.danielarrais.votingsystem.domain.Resultado;
import dev.danielarrais.votingsystem.domain.Sessao;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/pautas")
@RequiredArgsConstructor
public class PautaController {

    private final CriarPautaService criarPautaService;
    private final CriarSessaoService criarSessaoService;
    private final RegistrarVotoService registrarVotoService;
    private final RecuperarResultadoService resultadoService;

    @Value("${voting.default-session-duration}")
    private Integer sessionDuration;

    @PostMapping
    @ResponseStatus(CREATED)
    public void criarPauta(@Valid @RequestBody PautaRequest pautaRequest) {
        criarPautaService.cria(pautaRequest);
    }

    @PostMapping("/{pautaId}/sessoes")
    @ResponseStatus(CREATED)
    public void criarSessao(@PathVariable Long pautaId,
                            @RequestParam(required = false) Integer duracao) {
        if (duracao == null || duracao == 0) {
            duracao = sessionDuration;
        }

        Sessao sessao = Sessao.builder()
                .dataInicio(LocalDateTime.now())
                .duracao(duracao)
                .build();

        criarSessaoService.criar(pautaId, sessao);
    }

    @PostMapping("/{pauta_id}/votos")
    @ResponseStatus(NO_CONTENT)
    public void votar(@PathVariable(name = "pauta_id") Long pautaId,
                      @Valid @RequestBody VotoRequest votoRequest) {
        registrarVotoService.votar(pautaId, votoRequest);
    }

    @GetMapping("/{pauta_id}/resultados")
    @ResponseStatus(OK)
    public Resultado resultados(@PathVariable(name = "pauta_id") Long pautaId) {
        return resultadoService.buscarResultado(pautaId);
    }

}
