import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/test")
public class test1Servlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
