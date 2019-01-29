package net.antiknox;

import net.antiknox.impl.InMemoryWhitelist;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class AntiKnox {

	private String key;
	private JsonDeserializer jsonDeserializer;
	private Whitelist whitelist = new InMemoryWhitelist();
	private RecordCache cache;

	public AntiKnox(String key, JsonDeserializer jsonDeserializer, RecordCache cache) {
		if (key == null || key.length() != 64) {
			throw new IllegalArgumentException("key has to be 64 characters and cannot be null");
		}

		this.key = key;
		this.jsonDeserializer = jsonDeserializer;
		this.cache = cache;
	}

	public Whitelist getWhitelist() {
		return whitelist;
	}

	public Record lookup(String ip) {
		// If the IP is whitelisted, we return an empty response to avoid blocking them.
		if (whitelist.contains(ip)) {
			return Record.EMPTY;
		}

		// Check for a cache hit
		Optional<Record> cached = cache.get(ip);
		if (cached.isPresent()) {
			return cached.get();
		}

		OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build();

		try {
			Call call = client.newCall(new Request.Builder().url("https://api.antiknox.net/lookup/" + ip + "?auth=" + key).get().build());

			Optional<Record> record = jsonDeserializer.fromJson(call.execute().body().byteStream(), Record.class);
			if (!record.isPresent()) {
				return Record.EMPTY;
			}

			// Put the record in the cache to remove redundant calls.
			cache.put(ip, record.get());

			return record.get();
		} catch (Exception e) {
			return Record.EMPTY;
		}
	}

}
