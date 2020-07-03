import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DominoMain extends Application {
    GridPane humanHand = new GridPane();
    GridPane topTile = new GridPane();
    ClipboardContent content = new ClipboardContent();
    ImageView imageView;
    ImageView clickedImageView;
    Image clickedImage;
    int columnIndex;
    boolean dragging;

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();
        gridConstraints(14, 1, topTile);

        List<ImageView> tiles = new ArrayList<>();
        String[] imageResources = new String[] {
                "00.jpg", "10.jpg", "11.jpg", "20.jpg", "21.jpg", "22.jpg",
                "30.jpg", "31.jpg", "32.jpg", "33.jpg", "40.jpg", "41.jpg",
                "42.jpg", "43.jpg", "44.jpg", "50.jpg", "51.jpg", "52.jpg",
                "53.jpg", "54.jpg", "55.jpg", "60.jpg", "61.jpg", "62.jpg",
                "63.jpg", "64.jpg", "65.jpg", "66.jpg", "back.png"};


        for(final String imageResource : imageResources) {
            imageView = new ImageView(imageResource);
            imageView.setFitWidth(50);
            imageView.setFitHeight(26);
            tiles.add(imageView);
        }

        //Assign mouse press handler.
        for (ImageView imageView :
                tiles) {
            imageView.setOnMousePressed(event -> {
                clickedImageView = imageView;
                clickedImageView.setOpacity(100);
                clickedImage = imageView.getImage();
            });
        }

        Collections.shuffle(tiles);
        for (int i = 0; i < 7; i++) {
            GridPane.setHalignment(tiles.get(0), HPos.CENTER);
            humanHand.add(tiles.remove(0), i, 0);
        }

        humanHand.setOnDragDetected(event -> {
            Dragboard db = humanHand.startDragAndDrop(TransferMode.COPY_OR_MOVE);
            content.putImage(clickedImage);
            db.setContent(content);
            db.setDragView(clickedImage);
            dragging = true;
            humanHand.getChildren().remove(clickedImageView);
            event.consume();
        });

        topTile.setOnMouseEntered(event -> {
            System.out.println("colIndex is " + columnIndex);
            topTile.add(clickedImageView, columnIndex, 0);
        });


        humanHand.setAlignment(Pos.BOTTOM_CENTER);
        AnchorPane.setBottomAnchor(humanHand, 25.0);
        AnchorPane.setLeftAnchor(humanHand, 0.0);
        AnchorPane.setRightAnchor(humanHand, 0.0);
        topTile.setAlignment(Pos.TOP_CENTER);
        AnchorPane.setTopAnchor(topTile, 25.0);
        AnchorPane.setLeftAnchor(topTile, 0.0);
        AnchorPane.setRightAnchor(topTile, 0.0);


        root.getChildren().addAll(humanHand, topTile);

        primaryStage.setScene(new Scene(root, 800, 350));
        primaryStage.show();
    }

    private void gridConstraints(int numCols, int numRows, GridPane grid) {
        for (int i = 0 ; i < numCols ; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints(50);
            colConstraints.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(colConstraints);
        }

        for (int i = 0 ; i < numRows ; i++) {
            RowConstraints rowConstraints = new RowConstraints(26);
            rowConstraints.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(rowConstraints);
        }

        grid.setMaxHeight(26);
        grid.setMaxWidth(50);
        grid.setMinSize(50, 26);
        grid.setGridLinesVisible(true);


        for (int i = 0 ; i < numCols ; i++) {
            for (int j = 0; j < numRows; j++) {
                addPane(i, j, grid);
            }
        }
    }

    private void addPane(int colIndex, int rowIndex, GridPane grid) {
        Pane pane = new Pane();
        pane.setOnDragEntered(event -> {
            System.out.printf("Mouse entered cell [%d, %d]%n", colIndex, rowIndex);
            columnIndex = colIndex;
        });
        grid.add(pane, colIndex, rowIndex);
    }


}