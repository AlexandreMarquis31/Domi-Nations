package UI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Objects;

// a unique crown image used in dominos
final class CrownUI {
    private static BufferedImage INSTANCE = null;

    private CrownUI() {
    }

    static BufferedImage getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = ImageIO.read(Objects.requireNonNull(CrownUI.class.getClassLoader().getResourceAsStream("crown.png")));
            } catch (Exception e) {
                return null;
            }
        }
        return INSTANCE;
    }
}
