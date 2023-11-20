package dev.danielarrais.votingsystem.api;

import dev.danielarrais.votingsystem.api.dto.request.PautaRequest;
import dev.danielarrais.votingsystem.api.dto.request.VotoRequest;
import dev.danielarrais.votingsystem.application.*;
import dev.danielarrais.votingsystem.domain.Resultado;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/pautas")
@RequiredArgsConstructor
public class PautaController {

    private final CriarPautaService criarPautaService;
    private final CriarSessaoService criarSessaoService;
    private final RegistrarVotoService registrarVotoService;
    private final RecuperarResultadoService resultadoService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void criarPauta(@Valid @RequestBody PautaRequest pautaRequest) {
        criarPautaService.cria(pautaRequest);
    }

    @PostMapping("/{pauta_id}/sessoes")
    @ResponseStatus(CREATED)
    public void criarSessao(@PathVariable(name = "pauta_id") Long pautaId,
                            @RequestParam(required = false) Integer duracao) {
        criarSessaoService.cria(pautaId, duracao);
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
        return resultadoService.resultado(pautaId);
    }

}
