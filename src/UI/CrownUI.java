package UI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

// a unique crown image used in dominos
final class CrownUI {
    private static BufferedImage INSTANCE = null;

    private CrownUI() {
    }

    static BufferedImage getInstance() {
        if (INSTANCE == null) {
            try {
                InputStream in = CrownUI.class.getResourceAsStream("crown.png");
                INSTANCE = ImageIO.read(in);
            } catch (Exception e) {
                return null;
            }
        }
        return INSTANCE;
    }
}
