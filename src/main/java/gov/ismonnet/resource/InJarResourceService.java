package gov.ismonnet.resource;

import gov.ismonnet.util.SneakyThrow;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;

public class InJarResourceService implements ResourceService {

    @Inject InJarResourceService() {}

    @Override
    public URL getResourceUrl(String res) {
        return InJarResourceService.class.getClassLoader().getResource(res);
    }

    @Override
    public InputStream getResourceAsStream(String res) {
        return InJarResourceService.class.getClassLoader().getResourceAsStream(res);
    }

    @Override
    public BufferedImage getImageResources(String res) {
        return SneakyThrow.callUnchecked(() -> ImageIO.read(getResourceAsStream(res)));
    }
}
