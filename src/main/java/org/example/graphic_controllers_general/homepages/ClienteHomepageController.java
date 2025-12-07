package org.example.graphic_controllers_general.homepages;

import java.awt.Desktop;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteHomepageController {
    private static ClienteHomepageController instance;
    private static final String GOOGLE_MAPS_URL = "https://www.google.com/maps/d/u/0/embed?mid=1rOu3jHBshR_ZiVIPd46f6ncQaHpjRvQ&ehbc=2E312F";
    private static final Logger logger = Logger.getLogger(ClienteHomepageController.class.getName());

    private ClienteHomepageController() {}

    public static synchronized ClienteHomepageController getInstance() {
        if (instance == null) {
            instance = new ClienteHomepageController();
        }
        return instance;
    }

    public void apriMappa() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(GOOGLE_MAPS_URL));
            } else {
                logger.log(Level.WARNING, "Desktop browsing non supportato su questo sistema");
                // Fallback: tentativo con Runtime.exec (per sistemi dove Desktop non è supportato)
                String os = System.getProperty("os.name").toLowerCase();
                Runtime runtime = Runtime.getRuntime();

                if (os.contains("win")) {
                    runtime.exec("rundll32 url.dll,FileProtocolHandler " + GOOGLE_MAPS_URL);
                } else if (os.contains("mac")) {
                    runtime.exec("open " + GOOGLE_MAPS_URL);
                } else if (os.contains("nix") || os.contains("nux")) {
                    runtime.exec("xdg-open " + GOOGLE_MAPS_URL);
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore durante l'apertura della mappa: " + e.getMessage(), e);
        }
    }
}