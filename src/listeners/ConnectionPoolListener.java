package listeners;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPoolListener implements ServletContextListener {
    private HikariConfig config;
    private HikariDataSource ds;
    private Driver driver;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        driver = new org.postgresql.Driver();

        try {
            DriverManager.registerDriver(driver);
        } catch (SQLException exc) {
            exc.printStackTrace();
        }

        config = new HikariConfig();
        config.setJdbcUrl( "jdbc:postgresql://localhost:5432/watches" );
        config.setUsername( "postgres" );
        config.setPassword( "splurgeola01" );
        ds = new HikariDataSource( config );

        context.setAttribute("dataSource", ds);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (ds != null) {
            ds.close();
        }

        try {
            DriverManager.deregisterDriver(driver);
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
}
