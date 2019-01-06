package UI;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;



public class Menu extends Application {
    GameMenu gamemenu = new GameMenu();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        Pane root = new Pane();
        root.setPrefSize(900, 700);

        GameMenu gamemenu = new GameMenu();
        gamemenu.setVisible(false);

        InputStream is = Files.newInputStream(Paths.get("res/background.jpg"));
        Image img = new Image(is);
        is.close();
        ImageView imgV = new ImageView(img);
        imgV.setFitHeight(700);
        imgV.setFitWidth(900);


        root.getChildren().addAll(imgV, gamemenu);
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(event ->{
            if (event.getCode() == KeyCode.ESCAPE){
                if(!gamemenu.isVisible()){
                    FadeTransition ft = new FadeTransition(Duration.seconds(0.7), gamemenu);
                    ft.setFromValue(0);
                    ft.setToValue(1);
                    ft.setOnFinished(evt-> gamemenu.setVisible(true));
                    ft.play();
                }else{
                    FadeTransition ft = new FadeTransition(Duration.seconds(0.7), gamemenu);
                    ft.setFromValue(1);
                    ft.setToValue(0);
                    ft.setOnFinished(evt-> gamemenu.setVisible(false));
                    ft.play();
                }
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    public class GameMenu extends Parent {
        GameMenu() {
            Pane root = new Pane();


            Rectangle rt = new Rectangle(900, 700);
            rt.setFill(Color.WHITE);
            rt.setOpacity(0.7);

            root.setPrefSize(900, 700);

            VBox menu = new VBox(10);

            Button play = new Button(("PLAY"));
            play.setOnMouseClicked(event -> {
                FadeTransition ft = new FadeTransition(Duration.seconds(0.75), gamemenu);
                ft.setFromValue(1);
                ft.setToValue(0);
                ft.setOnFinished(evt -> gamemenu.setVisible(false));
                ft.play();
            });


            Button exit = new Button(("EXIT"));
            exit.setOnMouseClicked(event -> {
                System.exit(0);
            });

            Button ajout = new Button(("AJOUT DE JOUEUR"));
            ajout.setOnMouseClicked(event -> {

                    });

            Button option = new Button(("OPTIONS"));
            option.setOnMouseClicked(event -> {


            });

            Button règle = new Button(("SELECTION REGLES"));
            règle.setOnMouseClicked(event -> {


            });

            Button règle_opt = new Button(("REGLES OPTIONNELLES"));
            règle.setOnMouseClicked(event -> {


            });



            menu.setTranslateX(325);
            menu.setTranslateY(325);



            menu.getChildren().addAll(play,ajout,règle,option,règle_opt,exit);
            root.getChildren().addAll(menu);
            getChildren().addAll(root);

        }
    }

    public static class Button extends StackPane {
        Button(String name) {
            Text text = new Text(name);
            text.setFont(Font.font(20));
            text.setFill(Color.WHITE);

            Rectangle rt = new Rectangle(250, 30);
            rt.setOpacity(0.8);
            rt.setFill(Color.BLACK);
            setAlignment(Pos.CENTER);
            getChildren().addAll(rt, text);

            setOnMouseEntered(event -> {
                rt.setFill(Color.WHITE);
                text.setFill(Color.BLACK);
            });
            setOnMouseExited(event -> {
                rt.setFill(Color.BLACK);
                text.setFill(Color.WHITE);
            });

        }
    }
}
