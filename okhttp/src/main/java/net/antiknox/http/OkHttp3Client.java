package net.antiknox.http;

import net.antiknox.Client;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OkHttp3Client implements Client {

    private OkHttpClient client;

    private OkHttp3Client(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public InputStream get(String url) throws IOException {
        Call call = client.newCall(new Request.Builder().url(url).get().build());

        ResponseBody body = call.execute().body();
        if (body == null) {
            throw new IOException("no body available from api");
        }

        return body.byteStream();
    }

    public static OkHttp3Client createDefault() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS).build();

        return wrap(client);
    }

    public static OkHttp3Client wrap(OkHttpClient client) {
        return new OkHttp3Client(client);
    }

}
