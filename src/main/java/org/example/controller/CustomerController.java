package org.example.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.error.BadRequestException;
import org.example.error.ErrorHandler;
import org.example.mapper.Json;
import org.example.model.Customer;
import org.example.service.CustomerService;
import org.example.storage.HttpUtil;

import java.io.IOException;
import java.util.List;

public class CustomerController implements HttpHandler {
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        ErrorHandler.handle(ex, () -> {
            String method = ex.getRequestMethod().toUpperCase();
            String path = ex.getRequestURI().getPath();

            if (!isValidPath(path)) {
                ErrorHandler.sendError(ex, 404, "Not Found");
                return;
            }

            Long id = extractId(path);

            switch (method) {
                case "GET" -> {
                    if (id == null) {
                        List<Customer> all = service.getAll();
                        HttpUtil.sendJson(ex, 200, Json.mapper().writeValueAsString(all));
                    } else {
                        Customer c = service.getById(id);
                        HttpUtil.sendJson(ex, 200, Json.mapper().writeValueAsString(c));
                    }
                }

                case "POST" -> {
                    if (id != null) throw new BadRequestException("POST does not accept id in path");
                    Customer incoming = HttpUtil.readJson(ex, Customer.class);
                    Customer created = service.create(incoming);
                    HttpUtil.sendJson(ex, 201, Json.mapper().writeValueAsString(created));
                }

                case "PUT" -> {
                    if (id == null) throw new BadRequestException("Missing id in path. Use /api/customer/{id}");
                    Customer incoming = HttpUtil.readJson(ex, Customer.class);
                    Customer updated = service.update(id, incoming);
                    HttpUtil.sendJson(ex, 200, Json.mapper().writeValueAsString(updated));
                }

                case "DELETE" -> {
                    if (id == null) throw new BadRequestException("Missing id in path. Use /api/customer/{id}");
                    service.delete(id);
                    HttpUtil.sendNoContent(ex);
                }

                default -> ErrorHandler.sendError(ex, 405, "Method Not Allowed");
            }
        });
    }

    private boolean isValidPath(String path) {
        String base = "/api/customer";
        return path.equals(base) || path.startsWith(base + "/");
    }

    private Long extractId(String path) {
        String base = "/api/customer";
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
