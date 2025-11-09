package BancoDigital.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

    public class EnderecoRequest {

    @NotBlank(message = "CEP é obrigatório")
    private String cep;
}

