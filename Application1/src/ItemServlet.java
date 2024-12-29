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
import java.sql.*;

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(PrintWriter out = resp.getWriter()) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book", "root", "Ijse@123");

            ResultSet resultSet = connection.prepareStatement("SELECT * FROM item").executeQuery();

            JsonArrayBuilder allItem = Json.createArrayBuilder();

            while (resultSet.next()){
                String code = resultSet.getString("code");
                String description = resultSet.getString("description");
                int qty = resultSet.getInt("qty");
                int unitPrice = resultSet.getInt("unitPrice");

                JsonObjectBuilder item = Json.createObjectBuilder();
                item.add("code", code);
                item.add("description", description);
                item.add("qty", qty);
                item.add("unitPrice", unitPrice);
                allItem.add(item);
            }

            resp.setContentType("application/json");
            resp.getWriter().write(allItem.build().toString());

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder jsonDate = new StringBuilder();
        String line;

        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonDate.append(line);
            }
        }

        JsonObject inputDate = Json.createReader(new StringReader(jsonDate.toString())).readObject();
        String code = inputDate.getString("code");
        String description = inputDate.getString("description");
        int qty = inputDate.getInt("qty");
        double unitPrice = inputDate.getJsonNumber("unitPrice").doubleValue(); // Ensure correct parsing

        String dbUrl = "jdbc:mysql://localhost:3306/book";
        String dbUser = "root";
        String dbPassword = "Ijse@123";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String insertQuery = "INSERT INTO item (code, description, qty, unitPrice) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, code);
                preparedStatement.setString(2, description);
                preparedStatement.setInt(3, qty);
                preparedStatement.setDouble(4, unitPrice); // Use double for price

                int rowsAffected = preparedStatement.executeUpdate();

                resp.setContentType("application/json");
                if (rowsAffected > 0) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    JsonObject responseObject = Json.createObjectBuilder()
                            .add("message", "Item added successfully")
                            .build();
                    resp.getWriter().write(responseObject.toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    JsonObject responseObject = Json.createObjectBuilder()
                            .add("message", "Failed to add item")
                            .build();
                    resp.getWriter().write(responseObject.toString());
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObject responseObject = Json.createObjectBuilder()
                    .add("message", "Error: " + e.getMessage())
                    .build();
            resp.getWriter().write(responseObject.toString());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      /*  String code = req.getPathInfo().substring(1); // Retrieve the item code from the path

        String dbUrl = "jdbc:mysql://localhost:3306/book";
        String dbUser = "root";
        String dbPassword = "Ijse@123";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {

                String deleteQuery = "DELETE FROM item WHERE code = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                    preparedStatement.setString(1, code);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        resp.setStatus(HttpServletResponse.SC_OK);
                        JsonObjectBuilder responseObject = Json.createObjectBuilder();
                        responseObject.add("message", "Item deleted successfully");
                        resp.getWriter().write(responseObject.build().toString());
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        JsonObjectBuilder responseObject = Json.createObjectBuilder();
                        responseObject.add("message", "Item not found");
                        resp.getWriter().write(responseObject.build().toString());
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObjectBuilder responseObject = Json.createObjectBuilder();
            responseObject.add("message", "Error: " + e.getMessage());
            resp.getWriter().write(responseObject.build().toString());
        }*/
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
        String code = inputData.getString("code");

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
            String deleteQuery = "DELETE FROM item WHERE code = ?";

            // Create a PreparedStatement to execute the delete query
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, code);

                // Execute the query
                int rowsAffected = preparedStatement.executeUpdate();

                // Respond with a success message
                if (rowsAffected > 0) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    JsonObjectBuilder responseObject = Json.createObjectBuilder();
                    responseObject.add("message", "Item deleted successfully");
                    resp.getWriter().write(responseObject.build().toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    JsonObjectBuilder responseObject = Json.createObjectBuilder();
                    responseObject.add("message", "Error deleting item");
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

  /*  @Override
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
        String code = inputData.getString("code");
        String description = inputData.getString("description");
        String qty = inputData.getString("qty");
        String unitPrice = inputData.getString("unitPrice");

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
            String updateQuery = "UPDATE item SET description = ?, qty = ?, unitPrice = ?  WHERE code = ?";

            // Create a PreparedStatement to execute the update query
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setString(1, description);
                preparedStatement.setInt(2, Integer.parseInt(qty));
                preparedStatement.setInt(3, Integer.parseInt(unitPrice));
                preparedStatement.setString(3, code);

                // Execute the query
                int rowsAffected = preparedStatement.executeUpdate();

                // Respond with a success message
                if (rowsAffected > 0) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    JsonObjectBuilder responseObject = Json.createObjectBuilder();
                    responseObject.add("message", "Item updated successfully");
                    resp.getWriter().write(responseObject.build().toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    JsonObjectBuilder responseObject = Json.createObjectBuilder();
                    responseObject.add("message", "Error updating item");
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

    }*/

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder jsonData = new StringBuilder();
        String line;

        // Reading JSON data from the request body
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        }

        try {
            // Parse JSON data
            JsonObject inputData = Json.createReader(new StringReader(jsonData.toString())).readObject();
            String code = inputData.getString("code");
            String description = inputData.getString("description");
            int qty = inputData.getInt("qty"); // Use JSON integer parsing
            double unitPrice = inputData.getJsonNumber("unitPrice").doubleValue(); // Ensure it's a double

            // Database connection
            String dbUrl = "jdbc:mysql://localhost:3306/book";
            String dbUser = "root";
            String dbPassword = "Ijse@123";

            // SQL query for updating the item
            String updateQuery = "UPDATE item SET description = ?, qty = ?, unitPrice = ? WHERE code = ?";

            try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                 PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

                // Setting query parameters
                preparedStatement.setString(1, description);
                preparedStatement.setInt(2, qty);
                preparedStatement.setDouble(3, unitPrice); // Double for unitPrice
                preparedStatement.setString(4, code);

                // Execute the update
                int rowsAffected = preparedStatement.executeUpdate();

                // Check update result and respond
                if (rowsAffected > 0) {
                    resp.setStatus(HttpServletResponse.SC_OK);
                    JsonObjectBuilder responseObject = Json.createObjectBuilder();
                    responseObject.add("message", "Item updated successfully");
                    resp.getWriter().write(responseObject.build().toString());
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    JsonObjectBuilder responseObject = Json.createObjectBuilder();
                    responseObject.add("message", "No matching item found to update");
                    resp.getWriter().write(responseObject.build().toString());
                }
            }
        } catch (Exception e) {
            // Handle errors
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonObjectBuilder errorResponse = Json.createObjectBuilder();
            errorResponse.add("message", "Error: " + e.getMessage());
            resp.getWriter().write(errorResponse.build().toString());
        }
    }

}
