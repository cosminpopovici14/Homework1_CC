package org.example.error;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.time.Instant;
import org.example.mapper.Json;
import org.example.storage.HttpUtil;

public class ErrorHandler {

    public static void handle(HttpExchange ex, ThrowingRunnable action) throws IOException {
        try {
            action.run();
        } catch (BadRequestException e) {
            sendError(ex, 400, e.getMessage());
        } catch (NotFoundException e) {
            sendError(ex, 404, e.getMessage());
        } catch (Exception e) {
            sendError(ex, 500, "Server error: " + e.getMessage());
        } finally {
            ex.close();
        }
    }

    public static void sendError(HttpExchange ex, int status, String message) throws IOException {
        String path = ex.getRequestURI().getPath();
        ApiError err = new ApiError(status, message, path, Instant.now());
        HttpUtil.sendJson(ex, status, Json.mapper().writeValueAsString(err));
    }

    @FunctionalInterface
    public interface ThrowingRunnable {
        void run() throws Exception;
    }
}
