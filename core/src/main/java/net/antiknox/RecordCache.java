package net.antiknox;

import java.util.Optional;

public interface RecordCache {

	Optional<Record> get(String ip);

	void put(String ip, Record record);

}
