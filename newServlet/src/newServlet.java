import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/Isha")
public class newServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String servletPath = req.getServletPath();
        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();
        String method = req.getMethod();
        String pathInfo = req.getPathInfo();

        System.out.println("Servlet path: " + servletPath);
        System.out.println("RequestURI: " + requestURI);
        System.out.println("Context path: " + contextPath);
        System.out.println("Method: " + method);
        System.out.println("PathInfo: pathInfo");

       /* PrintWriter out = resp.getWriter();
        out.println("<h1 style=\"color:pink\">Name:Ishani Ekanayake</h1>");
        System.out.println("hiiiii");*/
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("Post  World...");
        System.out.println("podt get World");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("Do  World...");
        System.out.println("Do get World");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("Delete  World...");
        System.out.println("Delete post get World");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("<h1>Hello</h1>");
        System.out.println("Option get World");
    }
}