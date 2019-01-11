package UI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;

// a unique crown image used in dominos
final class CrownUI {
    private static BufferedImage INSTANCE = null;

    private CrownUI() {
    }

    static BufferedImage getInstance() {
        if (INSTANCE == null) {
            try {
                URL resource = ClassLoader.getSystemResource("crown.png");
                INSTANCE = ImageIO.read(resource);
            } catch (Exception e) {
                return null;
            }
        }
        return INSTANCE;
    }
}
