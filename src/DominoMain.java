import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DominoMain extends Application {
    GridPane humanHand = new GridPane();
    GridPane topTile = new GridPane();
    GridPane bottomTile = new GridPane();
    GridPane computerHand = new GridPane();
    GridPane boneyard = new GridPane();

    ClipboardContent content = new ClipboardContent();
    ImageView imageView;
    ImageView clickedImageView;
    Image clickedImage;
    int columnIndex;
    boolean dragging;
    boolean rotating = false;
    int m = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();
        root.setStyle("-fx-background-color: yellow");
        Label humanHandLabel = new Label("Human Hand");
        humanHandLabel.setFont(Font.font(24));
        Label computerHandLabel = new Label("Computer Hand");
        computerHandLabel.setFont(Font.font(24));
        Label boneyardLabel = new Label("Boneyard");
        boneyardLabel.setFont(Font.font(24));
        Button rotate = new Button("Rotate");
        rotate.setFont(Font.font(24));
        rotate.setStyle("-fx-background-color: lightgray");
        Button drawCard = new Button("Draw Card");
        drawCard.setFont(Font.font(24));
        drawCard.setStyle("-fx-background-color: lightgray");
        Button quit = new Button("Quit");
        quit.setFont(Font.font(24));
        quit.setStyle("-fx-background-color: lightgray");
        gridConstraints(14, 1, topTile);
        gridConstraints(14, 1, bottomTile);
        List<ImageView> computerHandList = new ArrayList<>();
        List<ImageView> boneyardList = new ArrayList<>();
        HBox buttonGroup = new HBox(rotate, drawCard, quit);
        buttonGroup.setSpacing(20);


        List<ImageView> tiles = new ArrayList<>();
        String[] imageResources = new String[] {
                "00.jpg", "10.jpg", "11.jpg", "20.jpg", "21.jpg", "22.jpg",
                "30.jpg", "31.jpg", "32.jpg", "33.jpg", "40.jpg", "41.jpg",
                "42.jpg", "43.jpg", "44.jpg", "50.jpg", "51.jpg", "52.jpg",
                "53.jpg", "54.jpg", "55.jpg", "60.jpg", "61.jpg", "62.jpg",
                "63.jpg", "64.jpg", "65.jpg", "66.jpg"};


        final String[] imageString = {null};
        for(final String imageResource : imageResources) {
            imageView = new ImageView(imageResource);
            imageView.setFitWidth(50);
            imageView.setFitHeight(26);
            imageView.setOnDragDetected(event -> {
                imageString[0] = imageResource;
            });
            tiles.add(imageView);
        }

        //Assign mouse press handler.
        for (ImageView imageView :
                tiles) {
            imageView.setOnMousePressed(event -> {
                clickedImageView = imageView;
                clickedImage = imageView.getImage();
            });
        }

        Collections.shuffle(tiles);
        for (m = 0; m < 7; m++) {
            GridPane.setHalignment(tiles.get(0), HPos.CENTER);
            humanHand.add(tiles.remove(0), m, 0);
        }

        for (int i = 0; i < 7; i++) {
            computerHandList.add(tiles.remove(0));
            imageView = new ImageView("back.png");
            imageView.setFitWidth(50);
            imageView.setFitHeight(26);
            computerHand.add(imageView, i, 0);
        }

        for (int i = 0; i < 14; i++) {
            boneyardList.add(tiles.remove(0));
            imageView = new ImageView("back.png");
            imageView.setFitWidth(50);
            imageView.setFitHeight(26);
            boneyard.add(imageView, i, 0);
        }

        rotate.setOnMouseClicked(event -> {
            rotate.setStyle("-fx-background-color: crimson");
            rotating = true;

        });


        humanHand.setOnDragDetected(event -> {
            System.out.println(rotating);
            if (rotating) {
                rotate.setStyle("-fx-background-color: lightgray");
                System.out.println("I am rotating");
                humanHand.setRotate(180);
                rotating = false;
            }
            Dragboard db = humanHand.startDragAndDrop(TransferMode.COPY_OR_MOVE);
            content.putImage(clickedImage);
            db.setContent(content);
            db.setDragView(clickedImage);
            dragging = true;
            System.out.println(imageString[0]);
            humanHand.getChildren().remove(clickedImageView);

            event.consume();
        });

        topTile.setOnMouseEntered(event -> {
//            System.out.println(topTile.getChildren().get(columnIndex));
//            System.out.println("colIndex is " + columnIndex);
            if (dragging) {
                topTile.add(clickedImageView, columnIndex, 0);
            }
            dragging = false;
            humanHand.setRotate(0);
        });

        bottomTile.setOnMouseEntered(event -> {
//            System.out.println(bottomTile.getChildren().get(columnIndex));
//            System.out.println("colIndex is " + columnIndex);
            if (dragging) {
                bottomTile.add(clickedImageView, columnIndex, 0);
            }
            dragging = false;
            humanHand.setRotate(0);
        });

        drawCard.setOnMousePressed(event -> {
            drawCard.setStyle("-fx-background-color: crimson");
            drawCard.setOnMouseReleased(event1 ->
                    drawCard.setStyle("-fx-background-color: lightgray"));
            if (boneyardList.size() > 0) {
                humanHand.add(boneyardList.remove(0), m++,
                        0);
                boneyard.getChildren().remove(0);
            }
        });

        quit.setOnMouseClicked(event -> Platform.exit());

        humanHand.setAlignment(Pos.BOTTOM_CENTER);
        AnchorPane.setBottomAnchor(humanHand, 263.0);
        AnchorPane.setLeftAnchor(humanHand, 0.0);
        AnchorPane.setRightAnchor(humanHand, 0.0);
        humanHandLabel.setAlignment(Pos.BOTTOM_CENTER);
        AnchorPane.setBottomAnchor(humanHandLabel, 232.0);
        AnchorPane.setLeftAnchor(humanHandLabel, 0.0);
        AnchorPane.setRightAnchor(humanHandLabel, 0.0);
        humanHandLabel.setMaxWidth(Double.MAX_VALUE);

        computerHand.setAlignment(Pos.BOTTOM_CENTER);
        AnchorPane.setBottomAnchor(computerHand, 183.0);
        AnchorPane.setLeftAnchor(computerHand, 0.0);
        AnchorPane.setRightAnchor(computerHand, 0.0);
        computerHandLabel.setAlignment(Pos.BOTTOM_CENTER);
        AnchorPane.setBottomAnchor(computerHandLabel, 152.0);
        AnchorPane.setLeftAnchor(computerHandLabel, 0.0);
        AnchorPane.setRightAnchor(computerHandLabel, 0.0);
        computerHandLabel.setMaxWidth(Double.MAX_VALUE);

        boneyard.setAlignment(Pos.BOTTOM_CENTER);
        AnchorPane.setBottomAnchor(boneyard, 108.0);
        AnchorPane.setLeftAnchor(boneyard, 0.0);
        AnchorPane.setRightAnchor(boneyard, 0.0);
        boneyardLabel.setAlignment(Pos.BOTTOM_CENTER);
        AnchorPane.setBottomAnchor(boneyardLabel, 72.0);
        AnchorPane.setLeftAnchor(boneyardLabel, 0.0);
        AnchorPane.setRightAnchor(boneyardLabel, 0.0);
        boneyardLabel.setMaxWidth(Double.MAX_VALUE);

        buttonGroup.setAlignment(Pos.BOTTOM_CENTER);
        AnchorPane.setBottomAnchor(buttonGroup, 13.0);
        AnchorPane.setLeftAnchor(buttonGroup, 0.0);
        AnchorPane.setRightAnchor(buttonGroup, 0.0);

        topTile.setAlignment(Pos.TOP_CENTER);
        AnchorPane.setTopAnchor(topTile, 25.0);
        AnchorPane.setLeftAnchor(topTile, 0.0);
        AnchorPane.setRightAnchor(topTile, 0.0);
        bottomTile.setAlignment(Pos.TOP_CENTER);
        AnchorPane.setTopAnchor(bottomTile, 51.0);
        AnchorPane.setLeftAnchor(bottomTile, 0.0);
        AnchorPane.setRightAnchor(bottomTile, 50.0);


        root.getChildren().addAll(humanHand, topTile, bottomTile,
                humanHandLabel, computerHand, computerHandLabel, boneyard,
                boneyardLabel, buttonGroup);

        primaryStage.setScene(new Scene(root, 1100, 410));
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
//            System.out.printf("Mouse entered cell [%d, %d]%n", colIndex, rowIndex);
            columnIndex = colIndex;
        });
        grid.add(pane, colIndex, rowIndex);
    }
}