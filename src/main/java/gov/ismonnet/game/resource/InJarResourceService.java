package gov.ismonnet.game.resource;

import gov.ismonnet.util.SneakyThrow;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class InJarResourceService implements ResourceService {

    @Inject InJarResourceService() {}

    @Override
    public InputStream getResourceAsStream(String res) {
        return InJarResourceService.class.getClassLoader().getResourceAsStream(res);
    }

    @Override
    public BufferedImage getImageResources(String res) {
        return SneakyThrow.callUnchecked(() -> ImageIO.read(getResourceAsStream(res)));
    }
}
