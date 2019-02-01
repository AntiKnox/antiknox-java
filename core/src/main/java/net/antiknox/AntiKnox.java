package net.antiknox;

import net.antiknox.impl.JavaNetHttpClient;
import net.antiknox.impl.InMemoryWhitelist;

import java.io.InputStream;
import java.util.Optional;

public class AntiKnox {

	private String key;
	private JsonDeserializer jsonDeserializer;
	private Whitelist whitelist;
	private RecordCache cache;
	private Client httpClient;

	private AntiKnox() {

	}

	public Whitelist getWhitelist() {
		return whitelist;
	}

	public Record lookup(String ip) {
		// If the IP is whitelisted, we return an empty response to avoid blocking them.
		if (whitelist.contains(ip)) {
			return Record.EMPTY;
		}

		// Check for a cache hit (if we're using a cache at all)
		if (cache != null) {
			Optional<Record> cached = cache.get(ip);

			if (cached.isPresent()) {
				return cached.get();
			}
		}

		try (InputStream httpStream = httpClient.get("https://api.antiknox.net/lookup/" + ip + "?auth=" + key)) {
			Optional<Record> record = jsonDeserializer.fromJson(httpStream, Record.class);
			if (!record.isPresent()) {
				return Record.EMPTY;
			}

			// If caching is enabled we put the record in the cache to remove redundant calls.
			if (cache != null) {
				cache.put(ip, record.get());
			}

			return record.get();
		} catch (Exception e) {
			return Record.EMPTY;
		}
	}

	public static class Builder {

		private AntiKnox obj = new AntiKnox();

		public Builder key(String key) {
			obj.key = key;
			return this;
		}

		public Builder httpClient(Client httpClient) {
			if (httpClient == null) {
				throw new IllegalArgumentException("httpClient cannot be null - use a DefaultHttpClient instead");
			}

			obj.httpClient = httpClient;
			return this;
		}

		public Builder jsonDeserializer(JsonDeserializer deserializer) {
			obj.jsonDeserializer = deserializer;
			return this;
		}

		public Builder cache(RecordCache cache) {
			obj.cache = cache;
			return this;
		}

		public AntiKnox build() {
			// Verify key existence and length
			if (obj.key == null || obj.key.length() != 64) {
				throw new IllegalArgumentException("key has to be 64 characters and cannot be null");
			}

			// Require a valid JSON deserializer (no default implementation as Java has no JSON built-in)
			if (obj.jsonDeserializer == null) {
				throw new IllegalArgumentException("deserializer cannot be null");
			}

			// Set the HTTP client to the default one if it's not set
			if (obj.httpClient == null) {
				obj.httpClient = new JavaNetHttpClient();
			}

			// Use an in-memory whitelist by default
			if (obj.whitelist == null) {
				obj.whitelist = new InMemoryWhitelist();
			}

			return obj;
		}

	}

}
