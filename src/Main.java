import UI.Application;
import UI.MenuUI;
import javafx.application.Platform;



public class Main {
    public static void main(String[] args) {
        Platform.setImplicitExit(false);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Application app = new Application("Domi-Nations");
                MenuUI menu = new MenuUI(app.getWidth(),app.getHeight());
                app.setGM(menu);
            }
        });
    }
}

