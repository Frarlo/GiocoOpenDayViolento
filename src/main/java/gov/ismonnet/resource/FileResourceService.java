package gov.ismonnet.resource;

import gov.ismonnet.util.SneakyThrow;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileResourceService implements ResourceService {

    private final Path rootFolder;

    @Inject FileResourceService() {
        rootFolder = Paths.get("./").toAbsolutePath();
    }

    @Override
    public URL getResourceUrl(String res) {
        return SneakyThrow.callUnchecked(() -> rootFolder.resolve(res).toUri().toURL());
    }

    @Override
    public InputStream getResourceAsStream(String res) {
        return SneakyThrow.callUnchecked(() -> Files.newInputStream(rootFolder.resolve(res)));
    }

    @Override
    public BufferedImage getImageResources(String res) {
        return SneakyThrow.callUnchecked(() -> ImageIO.read(getResourceAsStream(res)));
    }
}
