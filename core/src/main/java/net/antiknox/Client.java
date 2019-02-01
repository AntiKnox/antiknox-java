package net.antiknox;

import java.io.IOException;
import java.io.InputStream;

public interface Client {

	 InputStream get(String url) throws IOException;

}
