package Calc;

import com.sun.jndi.toolkit.url.Uri;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

//class for opening a website, later it should be the function of a button to get to the next website

//***we´re thinking about putting this function in the website...
//is not working yet, probably because the URI is not recognized as one***
public class openWebpage {
    private static openWebpage ourInstance;
    final java.net.URI uri;

    public openWebpage(Uri uri) throws URISyntaxException {
        this.uri = new URI("http://www.google.com");
    }

    public static openWebpage getInstance() {
        return ourInstance;
    }

        public static boolean openWebpage(URI uri) {
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(uri);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        public static boolean openWebpage(java.net.URL url) {
            try {
                return openWebpage(url.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return false;
        }
        
        
    }

