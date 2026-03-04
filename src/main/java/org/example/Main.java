package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.service.PolicyService;
import org.example.storage.*;
import org.example.controller.PolicyController;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        try {
            PolicyStore store = new PolicyStore();
            PolicyService service = new PolicyService(store);
            PolicyController controller = new PolicyController(service);

            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/api/policy", controller);

            server.start();
            System.out.println("Server started on port 8080");
        } catch (Exception e) {
            System.out.println("Error starting server: " + e.getMessage());
        }
    }
}