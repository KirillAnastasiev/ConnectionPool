import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionServlet extends HttpServlet {
    private ServletContext context;
    private HikariDataSource ds;
    private Connection connection;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = config.getServletContext();
        ds = (HikariDataSource) context.getAttribute("dataSource");

        try {
            connection = ds.getConnection();
            System.out.println(connection.isClosed());
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("text/html");

        String sql = "SELECT * FROM country;";

        try (Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String shortName = rs.getString("short_name");
                writer.println(id + " : " + name + " : " + shortName + "<br/>");
            }
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            connection.close();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
}
