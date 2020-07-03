import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DominoMain extends Application {
    GridPane humanHand = new GridPane();
    ImageView imageView;
    int column;

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();

        List<ImageView> tiles = new ArrayList<>();
        String[] imageResources = new String[] {
                "00.jpg", "10.jpg", "11.jpg", "20.jpg", "21.jpg", "22.jpg",
                "30.jpg", "31.jpg", "32.jpg", "33.jpg", "40.jpg", "41.jpg",
                "42.jpg", "43.jpg", "44.jpg", "50.jpg", "51.jpg", "52.jpg",
                "53.jpg", "54.jpg", "55.jpg", "60.jpg", "61.jpg", "62.jpg",
                "63.jpg", "64.jpg", "65.jpg", "66.jpg"};


        for(final String imageResource : imageResources) {
            imageView = new ImageView(imageResource);
            imageView.setFitWidth(50);
            imageView.setFitHeight(26);
            tiles.add(imageView);
        }

        //Assign mouse press handler.
        for (ImageView imageView :
                tiles) {
            imageView.setOnMousePressed(this::onPress);
        }

        Collections.shuffle(tiles);
        for (int i = 0; i < 7; i++) {
            GridPane.setHalignment(tiles.get(0), HPos.CENTER);
            humanHand.add(tiles.remove(0), i, 0);
        }

        humanHand.setOnMousePressed(event -> {
            System.out.printf("Index to be removed %d \n", column);
            humanHand.getChildren().remove(column);
        });

        humanHand.setAlignment(Pos.BOTTOM_CENTER);
        AnchorPane.setBottomAnchor(humanHand, 25.0);
        AnchorPane.setLeftAnchor(humanHand, 0.0);
        AnchorPane.setRightAnchor(humanHand, 0.0);

        root.getChildren().addAll(humanHand);

        primaryStage.setScene(new Scene(root, 800, 350));
        primaryStage.show();
    }

    //Handler for mouse press event.
    private void onPress(MouseEvent event) {
        column = GridPane.getColumnIndex((Node) event.getSource());
        int row = GridPane.getRowIndex((Node) event.getSource());
        System.out.printf("Node clicked at: column=%d, row=%d \n", column, row);
        System.out.println(humanHand.getChildren().get(column));
    }
}