import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.*;

import static java.lang.Integer.parseInt;

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
    boolean legal;
    int m = 0;
    int[] topTileArray = new int[]{99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99};
    int[] bottomTileArray = new int[]{99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99};
    int n;


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
        ToggleButton rotateButton = new ToggleButton("Rotate");
        rotateButton.setFont(Font.font(24));
        rotateButton.setStyle("-fx-background-color: lightgray");
        Button drawCard = new Button("Draw Card");
        drawCard.setFont(Font.font(24));
        drawCard.setStyle("-fx-background-color: lightgray");
        Button quit = new Button("Quit");
        quit.setFont(Font.font(24));
        quit.setStyle("-fx-background-color: lightgray");
        gridConstraints(14, 1, topTile);
        gridConstraints(14, 1, bottomTile);
        List<Integer> computerHandList = new ArrayList<>();
        List<Integer> boneyardList = new ArrayList<>();
        HBox buttonGroup = new HBox(rotateButton, drawCard, quit);
        buttonGroup.setSpacing(20);

        List<Integer> tileValue = new ArrayList<>();
        List<ImageView> tiles = new ArrayList<>();
        Map<Integer, ImageView> tileMap = new HashMap<>();
        String[] imageResources = new String[] {
                "00", "10", "11", "20", "21", "22",
                "30", "31", "32", "33", "40", "41",
                "42", "43", "44", "50", "51", "52",
                "53", "54", "55", "60", "61", "62",
                "63", "64", "65", "66"};


        int imageResourceValue;
        final String[] imageString = {null};
        for(final String imageResource : imageResources) {
            imageView = new ImageView(imageResource);
            imageView.setFitWidth(50);
            imageView.setFitHeight(26);
            imageView.setOnDragDetected(event -> {
                imageString[0] = imageResource;
            });
            imageResourceValue = Integer.parseInt(imageResource);
            tileValue.add(imageResourceValue);
            tileMap.put(imageResourceValue, imageView);
            tiles.add(imageView);
        }

        for (ImageView imageView :
                tiles) {
            imageView.setOnMousePressed(event -> {
                clickedImageView = imageView;
                clickedImage = imageView.getImage();
            });
        }

        Collections.shuffle(tiles);
        Collections.shuffle(tileValue);
        for (m = 0; m < 7; m++) {
            GridPane.setHalignment(tileMap.get(tileValue.get(0)), HPos.CENTER);
            humanHand.add(tileMap.get(tileValue.remove(0)), m, 0);
        }

        for (int i = 0; i < 7; i++) {
            computerHandList.add(tileValue.remove(0));
            imageView = new ImageView("back.png");
            imageView.setFitWidth(50);
            imageView.setFitHeight(26);
            computerHand.add(imageView, i, 0);
        }

        System.out.println(computerHandList.get(0));
        topTileArray[6] = computerHandList.get(0);
        topTile.add(tileMap.get(computerHandList.remove(0)), 6, 0);

        computerHand.getChildren().remove(0);


        for (int i = 0; i < 14; i++) {
            boneyardList.add(tileValue.remove(0));
            imageView = new ImageView("back.png");
            imageView.setFitWidth(50);
            imageView.setFitHeight(26);
            boneyard.add(imageView, i, 0);
        }

        rotateButton.setOnAction(event -> {
            if (rotateButton.isSelected()) {
                rotateButton.setStyle("-fx-background-color: crimson");
                rotating = true;
            }
            else {
                rotating = false;
                rotateButton.setStyle("-fx-background-color: lightgrey");
            }
        });


        humanHand.setOnDragDetected(event -> {
            Dragboard db = humanHand.startDragAndDrop(TransferMode.COPY_OR_MOVE);
            content.putImage(clickedImage);
            db.setContent(content);
            db.setDragView(clickedImage);
            dragging = true;
            n = Integer.parseInt(imageString[0]);
            event.consume();
        });

        topTile.setOnMouseEntered(event -> {
            if (rotating) {
                clickedImageView.setRotate(180);
                rotateButton.setStyle("-fx-background-color: lightgrey");
                n = (n / 10) + ((n % 10) * 10);
                rotating = false;
            }
            if (topTileArray[columnIndex] == 99) {
                if (dragging) {
                    topTile.add(clickedImageView, columnIndex, 0);
                    humanHand.getChildren().remove(clickedImageView);
                    topTileArray[columnIndex] = n;
                }
            }
            System.out.print("Top tile ");
            for (int x :
                    topTileArray) {
                System.out.printf(" %d", x);
            }
            System.out.println();
            dragging = false;
        });

        bottomTile.setOnMouseEntered(event -> {
            legal = false;
            if (rotating) {
                clickedImageView.setRotate(180);
                rotateButton.setStyle("-fx-background-color: lightgrey");
                n = (n / 10) + ((n % 10) * 10);
                rotating = false;
            }
            if (columnIndex > 0) {
                System.out.println("Top tile is " + topTileArray[columnIndex - 1] + ", /10 is ," + topTileArray[columnIndex - 1]/10 + ", %10 is " + topTileArray[columnIndex - 1]%10);
                System.out.println("Bottom tile is " + n + ", /10 is " + n/10 + ", %10 is " + n%10);
                if (bottomTileArray[columnIndex] == 99 &&
                        (topTileArray[columnIndex] / 10  == n % 10) ||
                        (topTileArray[columnIndex] / 10 == 0 ||
                                n % 10 == 0) ||
                        (topTileArray[columnIndex - 1] % 10 == n / 10 ||
                                (topTileArray[columnIndex - 1] % 10 == 0 ||
                                        n / 10 == 0))) {
                    legal = true;
                }
            }
            if (dragging && legal) {
                bottomTile.add(clickedImageView, columnIndex, 0);
                humanHand.getChildren().remove(clickedImageView);
                bottomTileArray[columnIndex] = n;
            }
            System.out.print("Bottom tile ");
            for (int x :
                    bottomTileArray) {
                System.out.printf(" %d", x);
            }
            System.out.println();
            dragging = false;
        });

        drawCard.setOnMousePressed(event -> {
            drawCard.setStyle("-fx-background-color: crimson");
            drawCard.setOnMouseReleased(event1 ->
                    drawCard.setStyle("-fx-background-color: lightgray"));
            if (boneyardList.size() > 0) {
                humanHand.add(tileMap.get(boneyardList.remove(0)), m++,
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

    private void fillArray(int[] array) {
        for (int i = 0; i < 14; i++) {
            array[i] = 99;
        }
    }
}