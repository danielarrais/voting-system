package dev.danielarrais.votingsystem.api;

import dev.danielarrais.votingsystem.api.dto.request.PautaRequest;
import dev.danielarrais.votingsystem.api.dto.request.VotoRequest;
import dev.danielarrais.votingsystem.application.CriarPautaService;
import dev.danielarrais.votingsystem.application.CriarSessaoService;
import dev.danielarrais.votingsystem.application.RegistrarVotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/pautas")
@RequiredArgsConstructor
public class PautaController {

    private final CriarPautaService criarPautaService;
    private final CriarSessaoService criarSessaoService;
    private final RegistrarVotoService registrarVotoService;

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

}
