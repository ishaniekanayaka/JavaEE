import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


@WebServlet(urlPatterns = "/dbcp")
public class DBCPServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BasicDataSource ds = new BasicDataSource();
        ds.setDriverClassName("com.mysql.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/book");
        ds.setUsername("root");
        ds.setPassword("Ijse@123");
        ds.setMaxTotal(5);
        ds.setInitialSize(5);

        //comming interface to all servlet
        ServletContext servletContext = req.getServletContext();
        //BasicDataSource ds = (BasicDataSource)servletContext.getAttribute("dataSource");
      servletContext.setAttribute("datasourse",ds);
        System.out.println("DBCP servlet doGet");

        try  {

            Connection connection = ds.getConnection();

            ResultSet resultSet = connection.prepareStatement("SELECT * FROM customer").executeQuery();


            while (resultSet.next()) {
                String customerId = resultSet.getString("customerId");
                String address = resultSet.getString("address");
                String customerName = resultSet.getString("customerName");
                System.out.println(customerId + ""+ customerName + "" + address);
                // Create a JSON object for each customer and add to the array
            }

            // Set response content type to JSON and send the response
            resp.setContentType("application/json");


        } catch ( SQLException e) {
          throw new RuntimeException();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //comming interface to all servlet
        ServletContext servletContext = req.getServletContext();
        BasicDataSource ds = (BasicDataSource)servletContext.getAttribute("dataSource");

        System.out.println("DBCP servlet doGet");

        try  {

            Connection connection = ds.getConnection();

            ResultSet resultSet = connection.prepareStatement("SELECT * FROM customer").executeQuery();


            while (resultSet.next()) {
                String customerId = resultSet.getString("customerId");
                String address = resultSet.getString("address");
                String customerName = resultSet.getString("customerName");
                System.out.println(customerId + ""+ customerName + "" + address);

            }
            // Set response content type to JSON and send the response
            resp.setContentType("application/json");
        } catch ( SQLException e) {
            throw new RuntimeException();
        }
    }
}
