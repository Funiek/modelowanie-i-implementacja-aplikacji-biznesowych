package com.example.cudanawidelcu;

import com.example.cudanawidelcu.services.recipes.RecipesServer;
import com.example.cudanawidelcu.services.registration.RegistrationServer;
import com.example.cudanawidelcu.services.web.WebServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;

public class Main {

    public static final String NO_VALUE = "NO-VALUE";

    public static void main(String[] args) {

        String serverName = NO_VALUE;
        String port = null;

        System.setProperty("registration.server.hostname", "localhost");

        for (String arg : args) {
            if (arg.startsWith("--"))
                continue;

            if (serverName.equals(NO_VALUE))
                serverName = arg;
            else if (port == null)
                port = arg;
            else {
                System.out.println("Unexpected argument: " + arg);
                usage();
                return;
            }
        }

        if (serverName.equals(NO_VALUE)) {
            usage();
            return;
        }

        // Override port, if specified
        if (port != null)
            System.setProperty("server.port", port);

        // Get IP address, useful when running in containers
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.out.println("Running on IP: " + inetAddress.getHostAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Determine which role this application will run as
        if (serverName.equals("registration") || serverName.equals("reg")) {
            RegistrationServer.main(args);
        } else if (serverName.equals("recipes")) {
            RecipesServer.main(args);
        } else if (serverName.equals("web")) {
            WebServer.main(args);
        } else {
            // Unrecognized server type - print usage and exit
            System.out.println("Unknown server type: " + serverName);
            usage();
        }
    }

    /**
     * Print application usage information to console.
     */
    protected static void usage() {
        System.out.println();
        System.out.println("Usage: java -jar ... <server-name> [server-port]");
        System.out.println("     where");
        System.out.println("       server-name is 'reg', 'registration', " + "'accounts' or 'web'");
        System.out.println("       server-port > 1024");
        System.out.println(
                "     optionally specify --registration.server.hostname=<IP-address> if it is not running on localhost,");
        System.out.println();
    }
}
