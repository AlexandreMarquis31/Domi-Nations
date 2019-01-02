package UI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;


final class CrownUI {
    private static BufferedImage INSTANCE = null;

    private CrownUI() {
    }
    public static BufferedImage getInstance() {
        if (INSTANCE == null){
            try {
                INSTANCE = ImageIO.read(CrownUI.class.getClassLoader().getResourceAsStream("crown.png"));
            } catch (Exception e){
                return null;
            }
        }
        return INSTANCE;
    }
}
