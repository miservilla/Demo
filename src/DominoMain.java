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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;

import static java.lang.Integer.parseInt;

public class DominoMain extends Application {
   private static GridPane humanHand;
    GridPane topTile;
    GridPane bottomTile;
    GridPane computerHand;
    GridPane boneyard;
    ClipboardContent content = new ClipboardContent();
    ImageView imageView;
    ImageView clickedImageView;
    Image clickedImage;
    int columnIndex;
    boolean dragging;//State of mouse movement across the board.
    boolean rotating = false;//State of rotation of tile.
    boolean legal;//State of move of tile, legal move or not.
    int m = 0;//humanHand tile count.
    int[] topTileArray;
    int[] bottomTileArray;
    int n;//Tile value.
    int o;//Tmp tile value (used if tile is rotated).
    Map<Integer, ImageView> tileMap;//Key = tile value, Value = tile image view.
    List<Integer> humanHandList;//List holding human tile values.
    List<Integer> computerHandList;//List holding computer tile values.
    List<Integer> boneyardList;//List holding boneyard tile values.
    AnchorPane root;







    @Override
    public void start(Stage primaryStage) throws Exception {

        root = new AnchorPane();
        tileMap = new HashMap<>();
        humanHandList = new ArrayList<>();
        computerHandList = new ArrayList<>();
        boneyardList = new ArrayList<>();
        humanHand = new GridPane();
        topTile = new GridPane();
        bottomTile = new GridPane();
        computerHand = new GridPane();
        boneyard = new GridPane();
        topTileArray = new int[]{99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
                99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
                99};
        bottomTileArray = new int[]{99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
                99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99, 99,
                99, 99};



//        root.getChildren().removeAll();
        root.setStyle("-fx-background-color: yellow");
        Label humanHandLabel = new Label();
        humanHandLabel.setFont(Font.font(24));
        Label computerHandLabel = new Label();
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
        Button newGame = new Button("New Game");
        newGame.setFont(Font.font(24));
        newGame.setStyle(("-fx-background-color: lightgrey"));
        quit.setStyle("-fx-background-color: lightgray");
        gridConstraints(28, 1, topTile);
        gridConstraints(28, 1, bottomTile);
        HBox buttonGroup = new HBox(rotateButton, drawCard, quit, newGame);
        buttonGroup.setSpacing(20);

        newGame.setOnMouseClicked(event -> {
            try {
                start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        List<Integer> tileValue = new ArrayList<>();
        List<ImageView> tiles = new ArrayList<>();
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
            humanHandList.add(tileValue.get(0));
            humanHand.add(tileMap.get(tileValue.remove(0)), m, 0);
        }
        humanHandLabel.setText("Human Hand " + getScore(humanHandList));

        //Building the computer hand.
        for (int i = 0; i < 7; i++) {
            computerHandList.add(tileValue.remove(0));
            imageView = new ImageView("back.png");
            imageView.setFitWidth(50);
            imageView.setFitHeight(26);
            computerHand.add(imageView, i, 0);
        }
        computerHandLabel.setText("Computer Hand "
                + getScore(computerHandList));

        topTileArray[13] = computerHandList.get(0);
        topTile.add(tileMap.get(computerHandList.remove(0)), 13, 0);
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
                rotateButton.setStyle("-fx-background-color: #dc143c");
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
            o = n;
            if (rotating && clickedImage != null) {
                clickedImageView.setRotate(180);
                rotateButton.setStyle("-fx-background-color: lightgrey");
                n = (n / 10) + ((n % 10) * 10);
                rotating = false;
            }
            legal = topTileLegal(columnIndex, n, bottomTileArray);
            if (dragging && legal) {
                topTile.add(clickedImageView, columnIndex, 0);
                for (int i = 0; i < humanHandList.size(); i++) {
                    if (o == humanHandList.get(i)) {
                        humanHandList.remove(i);
                    }
                }
                humanHand.getChildren().remove(clickedImageView);
                topTileArray[columnIndex] = n;
                if (boneyardList.size() == 0) {
                    checkWin(getScore(computerHandList), getScore(humanHandList));
                }
                computerPlay(topTileArray, bottomTileArray);
                humanHandLabel.setText("Human Hand " + getScore(humanHandList));
                computerHandLabel.setText("Computer Hand "
                        + getScore(computerHandList));
            }
            dragging = false;
        });

        bottomTile.setOnMouseEntered(event -> {
            o = n;
            if (rotating && clickedImage != null) {
                clickedImageView.setRotate(180);
                rotateButton.setStyle("-fx-background-color: lightgrey");
                n = (n / 10) + ((n % 10) * 10);
                rotating = false;
            }
            legal = bottomTileLegal(columnIndex, n, topTileArray);
            if (dragging && legal) {
                bottomTile.add(clickedImageView, columnIndex, 0);
                for (int i = 0; i < humanHandList.size(); i++) {
                    if (o == humanHandList.get(i)) {
                        humanHandList.remove(i);
                    }
                }
                    humanHand.getChildren().remove(clickedImageView);
                bottomTileArray[columnIndex] = n;
                if (boneyardList.size() == 0) {
                    checkWin(getScore(computerHandList), getScore(humanHandList));
                }
                computerPlay(topTileArray, bottomTileArray);
                humanHandLabel.setText("Human Hand " + getScore(humanHandList));
                computerHandLabel.setText("Computer Hand "
                        + getScore(computerHandList));
            }
            dragging = false;
        });

        drawCard.setOnMousePressed(event -> {
            drawCard.setStyle("-fx-background-color: crimson");
            drawCard.setOnMouseReleased(event1 ->
                    drawCard.setStyle("-fx-background-color: lightgray"));
            if (boneyardList.size() > 0) {
                humanHandList.add(boneyardList.get(0));
                humanHand.add(tileMap.get(boneyardList.remove(0)), m++,
                        0);
                boneyard.getChildren().remove(0);
            }
            humanHandLabel.setText("Human Hand " + getScore(humanHandList));
//            if (boneyardList.size() == 0) {
//                checkWin(getScore(computerHandList), getScore(humanHandList));
//            }
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

        primaryStage.setScene(new Scene(root, 1600, 410));
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

    private boolean topTileLegal(int columnIndex, int n, int[] bottomTileArray) {
        if (topTileArray[columnIndex] == 99) {
            if ((bottomTileArray[columnIndex] % 10 == n / 10) ||
                    (bottomTileArray[columnIndex] % 10 == 0) ||
                    (n / 10 == 0 && bottomTileArray[columnIndex] != 99)) {
                return true;
            }
            if (columnIndex < 27) {
                if ((bottomTileArray[columnIndex + 1] / 10 == n % 10) ||
                        (bottomTileArray[columnIndex + 1] / 10 == 0) ||
                        (n % 10 == 0 && bottomTileArray[columnIndex + 1] != 99)) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean bottomTileLegal(int columnIndex, int n, int[] topTileArray) {
        if (bottomTileArray[columnIndex] == 99) {
            if ((topTileArray[columnIndex] / 10  == n % 10) ||
                    (topTileArray[columnIndex] / 10 == 0) ||
                    (n % 10 == 0 && topTileArray[columnIndex] != 99)) {
                return true;
            }
            if (columnIndex > 0) {
                if ((topTileArray[columnIndex - 1] % 10 == n / 10) ||
                        (topTileArray[columnIndex - 1] % 10 == 0) ||
                        (n / 10 == 0 && topTileArray[columnIndex - 1] != 99)) {
                    return true;
                }
            }
        }
        return false;
    }
    private void computerPlay(int[] topTileArray, int[] bottomTileArray) {
        for (int i = 0; i < computerHandList.size(); i++) {
           if (computerLegalMove(i, topTileArray, bottomTileArray)) {
               return;
            }
        }
        while (boneyardList.size() > 0) {
            computerHandList.add(boneyardList.remove(0));
            imageView = new ImageView("back.png");
            imageView.setFitWidth(50);
            imageView.setFitHeight(26);
            computerHand.add(imageView, computerHandList.size(), 0);
            boneyard.getChildren().remove(0);
            if (computerLegalMove(computerHandList.size() - 1, topTileArray,
                    bottomTileArray)) {
                return;
            }
        }
        checkWin(getScore(computerHandList), getScore(humanHandList));
    }
    private int rotateIt(int z) {
        return (z / 10) + ((z % 10) * 10);
    }
    private boolean computerLegalMove(int i, int[] topTileArray, int[] bottomTileArray){
        for (int j = 0; j < 14; j++) {
            if (topTileLegal(j, computerHandList.get(i), bottomTileArray)) {
                topTileArray[j] = computerHandList.get(i);
                topTile.add(tileMap.get(computerHandList.remove(i)), j, 0);
                computerHand.getChildren().remove(i);
                return true;
            }
            else if (topTileLegal(j, rotateIt(computerHandList.get(i)), bottomTileArray)) {
                topTileArray[j] = rotateIt(computerHandList.get(i));
                ImageView imageView = tileMap.get(computerHandList.remove(i));
                imageView.setRotate(180);
                topTile.add(imageView, j, 0);
                computerHand.getChildren().remove(i);
                return true;
            }
            else if (bottomTileLegal(j, computerHandList.get(i), topTileArray)) {
                bottomTileArray[j] = computerHandList.get(i);
                bottomTile.add(tileMap.get(computerHandList.remove(i)), j, 0);
                computerHand.getChildren().remove(i);
                return true;
            }
            else if (bottomTileLegal(j, rotateIt(computerHandList.get(i)), topTileArray)) {
                bottomTileArray[j] = rotateIt(computerHandList.get(i));
                ImageView imageView = tileMap.get(computerHandList.remove(i));
                imageView.setRotate(180);
                bottomTile.add(imageView, j, 0);
                computerHand.getChildren().remove(i);
                return true;
            }
        }
        return false;
    }
    private void computerWins() {
        Label computerWinsLabel = new Label("Computer Wins!");
        computerWinsLabel.setFont(Font.font(50));
        computerWinsLabel.setTextFill(Color.RED);
        computerWinsLabel.setAlignment(Pos.CENTER);
        AnchorPane.setLeftAnchor(computerWinsLabel, 0.0);
        AnchorPane.setRightAnchor(computerWinsLabel, 0.0);
        root.getChildren().add(computerWinsLabel);
        for (int x :
                computerHandList) {
            System.out.printf("%d ", x);
        }
        dragging = false;
    }
    private void humanHandWins() {
        Label humanWinsLabel = new Label("Human Wins!");
        humanWinsLabel.setFont(Font.font(50));
        humanWinsLabel.setTextFill(Color.RED);
        humanWinsLabel.setAlignment(Pos.CENTER);
        AnchorPane.setLeftAnchor(humanWinsLabel, 0.0);
        AnchorPane.setRightAnchor(humanWinsLabel, 0.0);
        root.getChildren().add(humanWinsLabel);
        for (int x :
                computerHandList) {
            System.out.printf("%d ", x);
        }
        dragging = false;
    }

    private int getScore(List<Integer> list) {
        int value = 0;
        for (int tokenValue :
                list) {
            value += (tokenValue / 10) + (tokenValue % 10);
        }
        return value;
    }
    private void checkWin(int computerScore, int humanScore) {
        if (computerScore < humanScore) {
            computerWins();
        }
        else humanHandWins();
    }
}

/*TODO 1) Try changing grid pane to something more akin to linked list. 2) Add
drop sound to moves, and buzz sound for illegal move. 3) Add 2 player option in
addition to computer player option. 4) Add play new game option to reset board
and start anew.
 */