import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/inBuildDBCP")
public class inBuildDBCPServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Connection connection = dataSource.getConnection();
            ResultSet resultSet = connection.prepareStatement("SELECT * FROM CUSTOMER").executeQuery();

            while (resultSet.next()) {
                String customerId = resultSet.getString("customerId");
                String address = resultSet.getString("address");
                String customerName = resultSet.getString("customerName");
                System.out.println(customerId + "  "+ customerName + "  " + address);

                System.out.println(resultSet.getString(1));
                System.out.println(resultSet.getString(2));
                System.out.println(resultSet.getString(3));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
