package gov.ismonnet.client.resource;

import gov.ismonnet.util.SneakyThrow;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileResourceService implements ResourceService {

    private final Path rootFolder;

    @Inject FileResourceService() {
        rootFolder = Paths.get("./").toAbsolutePath();
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
