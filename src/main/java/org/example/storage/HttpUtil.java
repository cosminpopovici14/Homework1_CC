package org.example.storage;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.example.mapper.Json;

public class HttpUtil {

    public static <T> T readJson(HttpExchange ex, Class<T> clazz) throws IOException {
        try (InputStream is = ex.getRequestBody()) {
            return Json.mapper().readValue(is, clazz);
        }
    }

    public static void sendJson(HttpExchange ex, int code, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static void sendNoContent(HttpExchange ex) throws IOException {
        ex.sendResponseHeaders(204, -1);
    }
}
