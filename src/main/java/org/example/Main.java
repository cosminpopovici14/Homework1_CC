package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.service.PolicyService;
import org.example.service.CustomerService;
import org.example.storage.*;
import org.example.controller.PolicyController;
import org.example.controller.CustomerController;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        try {
            PolicyStore policyStore = new PolicyStore();
            PolicyService policyService = new PolicyService(policyStore);
            PolicyController policyController = new PolicyController(policyService);

            CustomerStore customerStore = new CustomerStore();
            CustomerService customerService = new CustomerService(customerStore);
            CustomerController customerController = new CustomerController(customerService);

            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/api/policy", policyController);
            server.createContext("/api/customer", customerController);

            server.start();
            System.out.println("Server started on port 8080");
        } catch (Exception e) {
            System.out.println("Error starting server: " + e.getMessage());
        }
    }
}