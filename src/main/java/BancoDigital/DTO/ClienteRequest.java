package BancoDigital.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
    public class ClienteRequest {

    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    @NotBlank(message = "CPF/CNPJ é obrigatório")
    private String cpfCnpj;

    @Email(message = "E-mail inválido")
    @NotBlank(message = "E-mail é obrigatório")
    private String email;

    @NotBlank(message = "Data de nascimento é obrigatória")
    private String dataNascimento;

    @Valid
    @NotNull(message = "Endereço é obrigatório")
    private String endereco;

}
