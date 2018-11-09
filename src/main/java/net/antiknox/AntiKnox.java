package net.antiknox;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AntiKnox {

	private static Gson gson = new Gson();

	private String key;
	private Set<String> whitelist = new HashSet<>();
	private Cache<String, Record> cache = Caffeine.newBuilder().maximumSize(1024).expireAfterAccess(2, TimeUnit.HOURS).build();

	public AntiKnox(String key) {
		if (key == null || key.length() != 64) {
			throw new IllegalArgumentException("key has to be 64 characters and cannot be null");
		}

		this.key = key;
	}

	public Set<String> getWhitelist() {
		return whitelist;
	}

	public Record lookup(String ip) {
		// If the IP is whitelisted, we return an empty response to avoid blocking them.
		if (whitelist.contains(ip)) {
			return Record.EMPTY;
		}

		// Check for a cache hit
		Record cached = cache.getIfPresent(ip);
		if (cached != null) {
			return cached;
		}

		OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build();

		try {
			Call call = client.newCall(new Request.Builder().url("https://api.antiknox.net/lookup/" + ip + "?auth=" + key).get().build());

			String data = call.execute().body().string();
			Record record = gson.fromJson(data, Record.class);

			// Put the record in the cache to remove redundant calls.
			cache.put(ip, record);

			return record;
		} catch (IOException e) {
			return Record.EMPTY;
		}
	}

}
