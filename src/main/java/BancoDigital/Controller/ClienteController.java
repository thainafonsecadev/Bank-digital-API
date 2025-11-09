package BancoDigital.Controller;

import BancoDigital.DTO.ClienteRequest;
import BancoDigital.Model.Cliente;
import BancoDigital.Model.Endereco;
import BancoDigital.Repository.ClienteRepository;
import BancoDigital.Service.ViaCepService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequestMapping("/clientes")
    public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final ViaCepService viaCepService;

    public ClienteController(ClienteRepository clienteRepository, ViaCepService viaCepService) {
        this.clienteRepository = clienteRepository;
        this.viaCepService = viaCepService;
    }

    @GetMapping("/teste")
    public String teste() {
        return "Controller funcionando!";
    }

    @PostMapping("/clientes")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente criarCliente(@RequestBody @Valid ClienteRequest clienteRequest) {

        Cliente cliente = new Cliente();
        cliente.setNomeCompleto(clienteRequest.getNomeCompleto());
        cliente.setCpfCnpj(clienteRequest.getCpfCnpj());
        cliente.setEndereco(clienteRequest.getEndereco());

        //  Se desejar gera UUID manualmente
        cliente.setId(UUID.randomUUID());

        return clienteRepository.save(cliente);
    }
}
