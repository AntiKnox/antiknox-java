package net.antiknox.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.antiknox.JsonDeserializer;
import net.antiknox.Record;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Optional;

public class GsonDeserializer implements JsonDeserializer {

	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(Record.Type.class, new RecordTypeTypeAdapter())
			.create();

	@Override
	public <T> Optional<T> fromJson(InputStream source, Type type) {
		T object = gson.fromJson(new InputStreamReader(source), type);

		return Optional.ofNullable(object);
	}

	private static class RecordTypeTypeAdapter extends TypeAdapter<Record.Type> {

		@Override
		public void write(JsonWriter out, Record.Type value) throws IOException {
			out.value(value.name().toLowerCase());
		}

		@Override
		public Record.Type read(JsonReader in) throws IOException {
			return Record.Type.valueOf(in.nextString().toUpperCase());
		}

	}

}
