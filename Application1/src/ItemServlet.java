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
}
