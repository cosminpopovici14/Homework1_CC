package org.example.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.error.BadRequestException;
import org.example.error.ErrorHandler;
import org.example.mapper.Json;
import org.example.model.Policy;
import org.example.service.PolicyService;
import org.example.storage.HttpUtil;

import java.io.IOException;
import java.util.List;

public class PolicyController implements HttpHandler {
    private final PolicyService service;

    public PolicyController(PolicyService service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        ErrorHandler.handle(ex, () -> {
            String method = ex.getRequestMethod().toUpperCase();
            String path = ex.getRequestURI().getPath();
            Long id = extractId(path);

            switch (method) {
                case "GET" -> {
                    if (id == null) {
                        List<Policy> all = service.getAll();
                        HttpUtil.sendJson(ex, 200, Json.mapper().writeValueAsString(all));
                    } else {
                        Policy p = service.getById(id);
                        HttpUtil.sendJson(ex, 200, Json.mapper().writeValueAsString(p));
                    }
                }

                case "POST" -> {
                    if (id != null) throw new BadRequestException("POST does not accept id in path");
                    Policy incoming = HttpUtil.readJson(ex, Policy.class);
                    Policy created = service.create(incoming);
                    HttpUtil.sendJson(ex, 201, Json.mapper().writeValueAsString(created));
                }

                case "PUT" -> {
                    if (id == null) throw new BadRequestException("Missing id in path. Use /api/policy/{id}");
                    Policy incoming = HttpUtil.readJson(ex, Policy.class);
                    Policy updated = service.update(id, incoming);
                    HttpUtil.sendJson(ex, 200, Json.mapper().writeValueAsString(updated));
                }

                case "DELETE" -> {
                    if (id == null) throw new BadRequestException("Missing id in path. Use /api/policy/{id}");
                    service.delete(id);
                    HttpUtil.sendNoContent(ex);
                }

                default -> ErrorHandler.sendError(ex, 405, "Method Not Allowed");
            }
        });
    }

    private Long extractId(String path) {
        String base = "/api/policy";
        if (path.equals(base)) return null;
        if (path.startsWith(base + "/")) {
            String tail = path.substring((base + "/").length()).trim();
            if (tail.isEmpty()) return null;
            try {
                return Long.parseLong(tail);
            } catch (NumberFormatException e) {
                throw new BadRequestException("Invalid id: " + tail);
            }
        }
        return null;
    }
}
