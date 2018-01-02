package com.standardcheckout.plugin.passwords;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

import com.google.gson.Gson;
import com.ulfric.buycraft.sco.model.ResetToken;
import com.ulfric.buycraft.sco.model.StandardCheckoutResetRequest;

import okhttp3.CacheControl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StandardCheckoutClient {

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	private final OkHttpClient client = new OkHttpClient();
	private final Gson gson = new Gson();
	private final SecureRandom random = random();

	private SecureRandom random() {
		try {
			return SecureRandom.getInstanceStrong();
		} catch (NoSuchAlgorithmException exception) {
			StandardCheckoutPasswordsPlugin.getInstance().getLogger().info("Strong secure random instance not available, defaulting to standard");
			return new SecureRandom();
		}
	}

	public String reset(UUID mojangId) {
		String adminToken = System.getenv("SCO_ADMIN_TOKEN");

		StandardCheckoutResetRequest request = new StandardCheckoutResetRequest();

		request.setMojangId(mojangId);
		request.setScoToken(adminToken);

		ResetToken token = new ResetToken();
		token.setCode(randomNumbers());
		token.setCreated(Instant.now());

		String json = gson.toJson(request);
		Request post = new Request.Builder()
				.cacheControl(CacheControl.FORCE_NETWORK)
				.addHeader("Accept", "application/json")
				.addHeader("User-Agent", "StandardCheckout")
				.url("https://standardcheckout.com/internal/resetpassword")
				.post(RequestBody.create(JSON, json))
				.build();

		try {
			Response response = client.newCall(post).execute();
			int code = response.code();
			if (code != 200) {
				StandardCheckoutPasswordsPlugin.getInstance().getLogger().severe("Response code " + code);
				return null;
			}
			return token.getCode();
		} catch (IOException exception) {
			exception.printStackTrace(); // TODO proper error handling
			return null;
		}
	}

	private String randomNumbers() {
		StringBuilder string = new StringBuilder();
		for (int x = 0; x < 6; x++) {
			string.append(random.nextInt(9));
		}
		return string.toString();
	}

}
