package gov.ismonnet.resource;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

public interface ResourceService {

    URL getResourceUrl(String res);

    InputStream getResourceAsStream(String res);

    BufferedImage getImageResources(String res);
}
