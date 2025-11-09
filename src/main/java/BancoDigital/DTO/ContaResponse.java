package BancoDigital.DTO;

import BancoDigital.Model.Conta;
import lombok.Getter;

@Getter
    public class ContaResponse {

        private final String idConta;
        private final String tipoConta;
        private final String status;
        private final String nomeCliente;
        private final String cpfCnpj;

        public ContaResponse(Conta conta) {
            this.idConta = conta.getIdConta().toString();
            this.tipoConta = conta.getTipoConta().name();
            this.status = conta.getStatus().name();
            this.nomeCliente = conta.getCliente().getNomeCompleto();
            this.cpfCnpj = conta.getCliente().getCpfCnpj();
        }

}



