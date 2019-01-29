package net.antiknox;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Optional;

public interface JsonDeserializer {

	<T> Optional<T> fromJson(InputStream source, Type type);

}
