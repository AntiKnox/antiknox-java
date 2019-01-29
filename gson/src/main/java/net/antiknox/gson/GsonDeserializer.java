package net.antiknox.gson;

import com.google.gson.Gson;
import net.antiknox.JsonDeserializer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Optional;

public class GsonDeserializer implements JsonDeserializer {

	private final Gson gson = new Gson();

	@Override
	public <T> Optional<T> fromJson(InputStream source, Type type) {
		T object = gson.fromJson(new InputStreamReader(source), type);

		return Optional.ofNullable(object);
	}

}
