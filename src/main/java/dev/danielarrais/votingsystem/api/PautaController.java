package dev.danielarrais.votingsystem.api;

import dev.danielarrais.votingsystem.api.dto.request.PautaRequest;
import dev.danielarrais.votingsystem.api.dto.request.VotoRequest;
import dev.danielarrais.votingsystem.api.dto.response.PautaResponse;
import dev.danielarrais.votingsystem.api.mapper.SessaoMapper;
import dev.danielarrais.votingsystem.core.application.service.in.*;
import dev.danielarrais.votingsystem.core.application.service.in.impl.BuscarResultadoUseCaseImpl;
import dev.danielarrais.votingsystem.core.application.service.in.impl.RegistrarVotoUserCaseImpl;
import dev.danielarrais.votingsystem.core.domain.Pauta;
import dev.danielarrais.votingsystem.core.domain.Resultado;
import dev.danielarrais.votingsystem.api.mapper.PautaMapper;
import dev.danielarrais.votingsystem.api.mapper.VotoMapper;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/v1/pautas")
@RequiredArgsConstructor
public class PautaController {

    private final CriarPautaUserCase criarPautaUserCase;
    private final CriarSessaoUserCase criarSessaoUserCase;
    private final RegistrarVotoUserCase registrarVotoUserCaseImpl;
    private final BuscarResultadosUseCase resultadoService;
    private final BuscarPautasUseCase buscarPautasUseCase;


    @PostMapping
    @ResponseStatus(CREATED)
    public void criarPauta(@Valid @RequestBody PautaRequest pautaRequest) {
        criarPautaUserCase.cria(PautaMapper.convert(pautaRequest));
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<PautaResponse> buscarPautas() {
        return PautaMapper.convert(buscarPautasUseCase.buscarTodasPautas());
    }

    @PostMapping("/{pautaId}/sessoes")
    @ResponseStatus(CREATED)
    public void criarSessao(@PathVariable Long pautaId,
                            @RequestParam(required = false, defaultValue = "${voting.default-session-duration}") Integer duracao) {
        criarSessaoUserCase.criar(pautaId, SessaoMapper.convert(duracao));
    }

    @PostMapping("/{pautaId}/votos")
    @ResponseStatus(NO_CONTENT)
    public void votar(@PathVariable(name = "pautaId") Long pautaId,
                      @Valid @RequestBody VotoRequest votoRequest) {
        registrarVotoUserCaseImpl.votar(VotoMapper.convert(pautaId, votoRequest));
    }

    @GetMapping("/{pautaId}/resultados")
    @ResponseStatus(OK)
    public Resultado resultados(@PathVariable(name = "pautaId") Long pautaId) {
        return resultadoService.buscarResultado(pautaId);
    }

}
