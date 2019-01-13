import UI.Application;
import UI.MenuUI;
import javafx.application.Platform;


class Main {
    public static void main(String[] args) {
        Platform.setImplicitExit(false);
        javax.swing.SwingUtilities.invokeLater(() -> {
            Application app = new Application("Domi-Nations");
            MenuUI menu = new MenuUI(app.getWidth(), app.getHeight());
            app.setGM(menu);
        });
    }
}

