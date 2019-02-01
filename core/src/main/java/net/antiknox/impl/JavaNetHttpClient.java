package net.antiknox.impl;

import net.antiknox.Client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class JavaNetHttpClient implements Client {

	@Override
	public InputStream get(String url) throws IOException {
		URL urlObject = new URL(url);

		URLConnection urlConnection = urlObject.openConnection();
		urlConnection.setAllowUserInteraction(false);
		urlConnection.setConnectTimeout(10_000);
		urlConnection.setReadTimeout(10_000);

		return urlConnection.getInputStream();
	}

}
