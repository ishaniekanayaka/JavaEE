import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    // Method to handle the GET request
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book", "root", "Ijse@123");

            // Fetch all customer data
            ResultSet resultSet = connection.prepareStatement("SELECT * FROM customer").executeQuery();

            // Create a JSON array to store all customers
            JsonArrayBuilder allCustomer = Json.createArrayBuilder();

            while (resultSet.next()) {
                String customerId = resultSet.getString("customerId");
                String address = resultSet.getString("address");
                String customerName = resultSet.getString("customerName");

                // Create a JSON object for each customer and add to the array
                JsonObjectBuilder customer = Json.createObjectBuilder();
                customer.add("customerId", customerId);
                customer.add("address", address);
                customer.add("customerName", customerName);
                allCustomer.add(customer);
            }

            // Set response content type to JSON and send the response
            resp.setContentType("application/json");
            resp.getWriter().write(allCustomer.build().toString());

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // Method to handle the POST request
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder jsonData = new StringBuilder();
        String line;

        // Read the JSON data from the request body
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        }

        // Parse the received JSON data
        JsonObject inputData = Json.createReader(new StringReader(jsonData.toString())).readObject();
        String customerId = inputData.getString("customerId");
        String address = inputData.getString("address");
        String customerName = inputData.getString("customerName");

        // Database connection details
        String dbUrl = "jdbc:mysql://localhost:3306/book";
        String dbUser = "root";
        String dbPassword = "Ijse@123";

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to the database
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // SQL query to insert a new customer
            String insertQuery = "INSERT INTO customer (customerId, address, customerName) VALUES (?, ?, ?)";

            // Create a PreparedStatement to execute the insert query
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, customerId);
                preparedStatement.setString(2, address);
                preparedStatement.setString(3, customerName);

                // Execute the query
                int rowsAffected = preparedStatement.executeUpdate();

                // Respond with a success message
                if (rowsAffected > 0) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    JsonObjectBuilder responseObject = Json.createObjectBuilder();
                    responseObject.add("message", "Customer added successfully");
                    resp.getWriter().write(responseObject.build().toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    JsonObjectBuilder responseObject = Json.createObjectBuilder();
                    responseObject.add("message", "Error adding customer");
                    resp.getWriter().write(responseObject.build().toString());
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObjectBuilder responseObject = Json.createObjectBuilder();
            responseObject.add("message", "Error: " + e.getMessage());
            resp.getWriter().write(responseObject.build().toString());
        }
    }

    // Method to handle the DELETE request
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder jsonData = new StringBuilder();
        String line;

        // Read the JSON data from the request body
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        }

        // Parse the received JSON data
        JsonObject inputData = Json.createReader(new StringReader(jsonData.toString())).readObject();
        String customerId = inputData.getString("customerId");

        // Database connection details
        String dbUrl = "jdbc:mysql://localhost:3306/book";
        String dbUser = "root";
        String dbPassword = "Ijse@123";

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to the database
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // SQL query to delete the customer
            String deleteQuery = "DELETE FROM customer WHERE customerId = ?";

            // Create a PreparedStatement to execute the delete query
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, customerId);

                // Execute the query
                int rowsAffected = preparedStatement.executeUpdate();

                // Respond with a success message
                if (rowsAffected > 0) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    JsonObjectBuilder responseObject = Json.createObjectBuilder();
                    responseObject.add("message", "Customer deleted successfully");
                    resp.getWriter().write(responseObject.build().toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    JsonObjectBuilder responseObject = Json.createObjectBuilder();
                    responseObject.add("message", "Error deleting customer");
                    resp.getWriter().write(responseObject.build().toString());
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObjectBuilder responseObject = Json.createObjectBuilder();
            responseObject.add("message", "Error: " + e.getMessage());
            resp.getWriter().write(responseObject.build().toString());
        }
    }


    // Method to handle the PUT request
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder jsonData = new StringBuilder();
        String line;

        // Read the JSON data from the request body
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        }

        // Parse the received JSON data
        JsonObject inputData = Json.createReader(new StringReader(jsonData.toString())).readObject();
        String customerId = inputData.getString("customerId");
        String address = inputData.getString("address");
        String customerName = inputData.getString("customerName");

        // Database connection details
        String dbUrl = "jdbc:mysql://localhost:3306/book";
        String dbUser = "root";
        String dbPassword = "Ijse@123";

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to the database
            Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // SQL query to update the customer details
            String updateQuery = "UPDATE customer SET address = ?, customerName = ? WHERE customerId = ?";

            // Create a PreparedStatement to execute the update query
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, address);
                preparedStatement.setString(2, customerName);
                preparedStatement.setString(3, customerId);

                // Execute the query
                int rowsAffected = preparedStatement.executeUpdate();

                // Respond with a success message
                if (rowsAffected > 0) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    JsonObjectBuilder responseObject = Json.createObjectBuilder();
                    responseObject.add("message", "Customer updated successfully");
                    resp.getWriter().write(responseObject.build().toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    JsonObjectBuilder responseObject = Json.createObjectBuilder();
                    responseObject.add("message", "Error updating customer");
                    resp.getWriter().write(responseObject.build().toString());
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObjectBuilder responseObject = Json.createObjectBuilder();
            responseObject.add("message", "Error: " + e.getMessage());
            resp.getWriter().write(responseObject.build().toString());
        }
    }
}
