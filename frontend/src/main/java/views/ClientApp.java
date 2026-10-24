package views;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 *
 * @author Jayden Avontuur
 */
public class ClientApp {

    private static final String AUTH_URL = "http://localhost:8080/api/auth";
    private static final String CUSTOMER_AUTH_URL = "http://localhost:8080/api/customer-auth";
    private static final String INVENTORY_URL = "http://localhost:8080/api/inventory";
    private static final String CUSTOMER_URL = "http://localhost:8080/api/customers";
    private static final String ORDER_URL = "http://localhost:8080/api/orders";
    private static final String PURCHASE_URL = "http://localhost:8080/api/purchase";
    private static final String STORE_URL = "http://localhost:8080/api/stores";
    private static final String PRODUCT_URL = "http://localhost:8080/api/products";
    private static final String SUPPLIER_URL = "http://localhost:8080/api/v1/supplier";
    private static final String EMPLOYEE_URL = "http://localhost:8080/api/v1/employee";

    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    private String authHeader;
    private String role;

    public ClientApp() {
        httpClient = HttpClient.newHttpClient();
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    // ---------- helpers ---------- //

    private HttpRequest.Builder newRequest(String url) {
        HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url));
        if (authHeader != null) {
            builder.header("Authorization", authHeader);
        }
        return builder;
    }

    private void check(HttpResponse<?> response) throws Exception {
        if (response.statusCode() == 401) {
            throw new Exception("You are not logged in. Please log in again.");
        }
        if (response.statusCode() == 403) {
            throw new Exception("You don't have permission to do that.");
        }
    }

    public String getRole() { return role; }
    public boolean isEmployee() { return "ROLE_EMPLOYEE".equals(role); }
    public void logout() { authHeader = null; role = null; }

    // ---------- login / register ---------- //

    public boolean authenticateUser(String username, String password) {
        return login(AUTH_URL + "/login", username, password, "ROLE_EMPLOYEE");
    }

    public boolean authenticateCustomer(String username, String password) {
        return login(CUSTOMER_AUTH_URL + "/login", username, password, "ROLE_CUSTOMER");
    }

    private boolean login(String url, String username, String password, String expectedRole) {
        authHeader = null;
        role = null;
        try {
            String credentials = username + ":" + password;
            String header = "Basic " + Base64.getEncoder()
                    .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", header)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String returnedRole = response.body() == null ? "" : response.body().trim();
            if (response.statusCode() == 200 && expectedRole.equals(returnedRole)) {
                authHeader = header;
                role = returnedRole;
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // employee registration
    public boolean registerUser(String username, String email, String password) {
        try {
            String json = mapper.writeValueAsString(new RegisterRequest(username, email, password));
            HttpRequest request = newRequest(AUTH_URL + "/register")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // customer sign-up
    public boolean registerCustomer(String firstName, String surname, String email, String username, String password) {
        try {
            String json = String.format(
                    "{\"firstName\":\"%s\",\"surname\":\"%s\",\"email\":\"%s\",\"username\":\"%s\",\"password\":\"%s\"}",
                    firstName, surname, email, username, password);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(CUSTOMER_AUTH_URL + "/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            int code = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).statusCode();
            return code == 200 || code == 201;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    // ---------- inventory ---------- //

    public List<Inventory> getAllInventory() throws Exception {
        HttpRequest request = newRequest(INVENTORY_URL).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        check(response);
        return mapper.readValue(response.body(),
                mapper.getTypeFactory().constructCollectionType(List.class, Inventory.class));
    }

    public Inventory createInventory(Inventory inventory) throws Exception {
        String json = mapper.writeValueAsString(inventory);
        HttpRequest request = newRequest(INVENTORY_URL)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        check(response);

        if (response.statusCode() == 500 && response.body().contains("Unique index or primary key violation")) {
            throw new Exception("That Product ID already exists. Please use a different Product ID.");
        }
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new Exception("Failed to add inventory item (server returned " + response.statusCode() + ").");
        }

        return mapper.readValue(response.body(), Inventory.class);
    }

    public void deleteInventory(Long id) throws Exception {
        HttpRequest request = newRequest(INVENTORY_URL + "/" + id).DELETE().build();
        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        check(response);
    }

    // ---------- purchase ---------- //

    public boolean purchase(String customerUsername, Long inventoryId, int quantity) throws Exception {
        String json = String.format(
                "{\"customerId\":\"%s\",\"inventoryId\":%d,\"quantity\":%d}",
                customerUsername, inventoryId, quantity);
        HttpRequest request = newRequest(PURCHASE_URL)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    // ---------- customers ---------- //

    public Customer createCustomer(Customer customer) throws Exception {
        String json = mapper.writeValueAsString(customer);
        HttpRequest request = newRequest(CUSTOMER_URL)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        check(response);
        return mapper.readValue(response.body(), Customer.class);
    }

    public List<Customer> getAllCustomers() throws Exception {
        HttpRequest request = newRequest(CUSTOMER_URL).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        check(response);
        return mapper.readValue(response.body(),
                mapper.getTypeFactory().constructCollectionType(List.class, Customer.class));
    }

    public void deleteCustomer(String id) throws Exception {
        HttpRequest request = newRequest(CUSTOMER_URL + "/" + id).DELETE().build();
        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        check(response);
    }

    // ---------- orders ---------- //

    public List<Order> getAllOrders() throws Exception {
        HttpRequest request = newRequest(ORDER_URL).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        check(response);
        return mapper.readValue(response.body(),
                mapper.getTypeFactory().constructCollectionType(List.class, Order.class));
    }

    public Order createOrder(Order order) throws Exception {
        String json = mapper.writeValueAsString(order);
        HttpRequest request = newRequest(ORDER_URL)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        check(response);
        return mapper.readValue(response.body(), Order.class);
    }

    public void deleteOrder(String orderNum) throws Exception {
        HttpRequest request = newRequest(ORDER_URL + "/" + orderNum).DELETE().build();
        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        check(response);
    }

    // ---------- stores ---------- //

    public List<Store> getAllStores() throws Exception {
        HttpRequest request = newRequest(STORE_URL).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        check(response);
        return mapper.readValue(response.body(),
                mapper.getTypeFactory().constructCollectionType(List.class, Store.class));
    }

    // ---------- products ---------- //

    public List<Product> getAllProducts() throws Exception {
        HttpRequest request = newRequest(PRODUCT_URL).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        check(response);
        return mapper.readValue(response.body(),
                mapper.getTypeFactory().constructCollectionType(List.class, Product.class));
    }

    // ---------- suppliers ---------- //

    public List<Supplier> getAllSuppliers() throws Exception {
        HttpRequest request = newRequest(SUPPLIER_URL + "/getAll").GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        check(response);
        return mapper.readValue(response.body(),
                mapper.getTypeFactory().constructCollectionType(List.class, Supplier.class));
    }

    public Supplier createSupplier(Supplier supplier) throws Exception {
        String json = mapper.writeValueAsString(supplier);
        HttpRequest request = newRequest(SUPPLIER_URL + "/save")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        check(response);

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new Exception("Failed to add supplier. Server returned " + response.statusCode());
        }
        return mapper.readValue(response.body(), Supplier.class);
    }

    public void deleteSupplier(String id) throws Exception {
        HttpRequest request = newRequest(SUPPLIER_URL + "/delete?SupplierId=" + id).DELETE().build();
        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        check(response);
    }

    // ---------- employees ---------- //

    public List<Employee> getAllEmployees() throws Exception {
        HttpRequest request = newRequest(EMPLOYEE_URL + "/getAll").GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        check(response);
        return mapper.readValue(response.body(),
                mapper.getTypeFactory().constructCollectionType(List.class, Employee.class));
    }

    public Employee createEmployee(Employee employee) throws Exception {
        String json = mapper.writeValueAsString(employee);
        HttpRequest request = newRequest(EMPLOYEE_URL + "/save")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        check(response);

        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new Exception("Failed to add employee. Server returned " + response.statusCode());
        }
        return mapper.readValue(response.body(), Employee.class);
    }

    // The backend's delete takes @RequestParam Long id, so the parameter must be "id"
    public void deleteEmployee(String id) throws Exception {
        HttpRequest request = newRequest(EMPLOYEE_URL + "/delete?id=" + id).DELETE().build();
        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        check(response);
    }

    private static class RegisterRequest {
        public String username;
        public String email;
        public String password;
        public RegisterRequest(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }

    public static void main(String[] args) {
        ClientApp client = new ClientApp();
        new LoginGUI(client).setGUI();
    }
}
