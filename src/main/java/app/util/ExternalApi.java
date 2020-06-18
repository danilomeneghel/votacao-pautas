package app.util;

import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.enumerator.StatusUserEnum;
import app.model.User;

public class ExternalApi {

	private String uri = "https://user-info.herokuapp.com/users/";
	private String result = null;
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	public StatusUserEnum getStatusCpf(Long cpf) {
		boolean validaCpf = CpfValidate.isCPF(cpf.toString());

		if (validaCpf) {
			log.info("CPF Válido: " + cpf);

			Client client = ClientBuilder.newClient();
			result = client.target(uri + cpf).request().get(String.class);
			client.close();

			log.info("Status do CPF: " + result);

			if (result != null) {
				User jsonb = JsonbBuilder.create().fromJson(result, User.class);
				return jsonb.status;
			}
		}

		log.info("CPF Inválido: " + cpf);

		return StatusUserEnum.UNABLE_TO_VOTE;
	}
}
