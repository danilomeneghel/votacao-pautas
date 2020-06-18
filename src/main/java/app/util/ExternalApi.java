package app.util;

import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import app.enumerator.StatusUserEnum;
import app.model.User;

public class ExternalApi {

	private static String uri = "https://user-info.herokuapp.com/users/";
	private static String result = null;

	public static StatusUserEnum getStatusCpf(Long cpf) {
		Client client = ClientBuilder.newClient();
		result = client.target(uri + cpf).request().get(String.class);
		client.close();

		if (result != null) {
			User jsonb = JsonbBuilder.create().fromJson(result, User.class);
			return jsonb.status;
		} else {
			return StatusUserEnum.UNABLE_TO_VOTE;
		}
	}
}
