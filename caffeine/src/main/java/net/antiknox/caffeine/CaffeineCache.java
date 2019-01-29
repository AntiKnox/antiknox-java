package net.antiknox.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.antiknox.Record;
import net.antiknox.RecordCache;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CaffeineCache implements RecordCache {

	private static final int DEFAULT_SIZE = 1024;
	private static final long DEFAULT_LIFETIME = TimeUnit.HOURS.toMillis(2);

	private Cache<String, Record> cache;

	private CaffeineCache(Cache<String, Record> wrap) {
		cache = wrap;
	}

	public static CaffeineCache wrap(Cache<String, Record> wrap) {
		return new CaffeineCache(wrap);
	}

	public static CaffeineCache create(int size, long lifetime, TimeUnit unit) {
		Cache<String, Record> wrap = Caffeine.newBuilder()
				.maximumSize(size)
				.expireAfterAccess(lifetime, unit)
				.build();

		return new CaffeineCache(wrap);
	}

	public static CaffeineCache createDefault() {
		return create(DEFAULT_SIZE, DEFAULT_LIFETIME, TimeUnit.MILLISECONDS);
	}

	@Override
	public Optional<Record> get(String ip) {
		return Optional.ofNullable(cache.getIfPresent(ip));
	}

	@Override
	public void put(String ip, Record record) {
		cache.put(ip, record);
	}

}
