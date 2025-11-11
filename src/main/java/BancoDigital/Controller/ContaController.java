package BancoDigital.Controller;

import BancoDigital.DTO.ContaRequest;
import BancoDigital.DTO.ContaResponse;
import BancoDigital.Exception.ErrorResponse;
import BancoDigital.Model.Conta;
import BancoDigital.Service.ContaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contas")

    public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<?> abrirConta(@RequestBody ContaRequest request) {
        try {
            Conta conta = contaService.abrirConta(request.getClienteId(), request.getTipoConta());
            ContaResponse response = new ContaResponse(conta);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Bad Request", e.getMessage()));
        }
    }

    @GetMapping("/{idConta}")
    public ResponseEntity<?> consultarConta(@PathVariable UUID idConta) {
        try {
            Conta conta = contaService.consultarConta(idConta);
            ContaResponse response = new ContaResponse(conta);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Not Found", e.getMessage()));
        }
    }

    @PutMapping("/{idConta}/status")
    public ResponseEntity<?> atualizarStatus(
            @PathVariable UUID idConta,
            @RequestParam String status)
    {
        try {
            Conta contaAtualizada = contaService.atualizarStatus(idConta, status);
            ContaResponse response = new ContaResponse(contaAtualizada);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Bad Request", e.getMessage()));
        }
    }

    @DeleteMapping("/{idConta}")
    public ResponseEntity<?> removerConta(@PathVariable UUID idConta) {
        try {
            contaService.removerConta(idConta);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Bad Request", e.getMessage()));
        }
    }


    @GetMapping
    public ResponseEntity<List<Conta>> listarContas() {
        return ResponseEntity.ok(contaService.listarContas());
    }
}


