package es.ies.puerto.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.transform.Rotate;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

public class JugarController {

    @FXML private GridPane grid;
    @FXML private Label timeLabel;
    @FXML private Label movesLabel;
    
    private int pairCount = 8;
    private int moves = 0;
    private int secondsElapsed = 0;
    private Timeline timer;

    private Card selected1 = null;
    private Card selected2 = null;
    
    private boolean isProcessing = false;

    private List<Card> allCards = new ArrayList<>();

    public void setPairCount(int pairCount) {
        this.pairCount = pairCount;
        setupGame();
    }

    private void setupGame() {
        List<String> paths = new ArrayList<>();
        for (char c = 'A'; c < 'A' + pairCount; c++) {
            String path = "/images/" + c + ".png";
            paths.add(path); paths.add(path);
        }
        Collections.shuffle(paths);

        grid.getChildren().clear();
        allCards.clear();
        moves = 0; secondsElapsed = 0;
        updateMoveLabel(); timeLabel.setText("Tiempo: 0s");

        int cols = (pairCount == 18) ? 6 : 4;
        int rows = (int) Math.ceil((paths.size()) / (double) cols);
        
        int index = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols && index < paths.size(); c++) {
                Card card = new Card(paths.get(index++));
                card.setOnMouseClicked(this::handleClick);
                grid.add(card, c, r);
                allCards.add(card);
            }
        }

        startTimer();
    }

    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            timeLabel.setText("Tiempo: " + secondsElapsed + "s");
        }));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    private void handleClick(MouseEvent e) {
        if (isProcessing) return; 
    
        Card clicked = (Card) e.getSource();
        if (clicked.isMatched() || clicked == selected1 || clicked == selected2 || clicked.isFlipped()) return;
    
        clicked.flip();
        SoundPlayer.play("flip.mp3",1,0);
    
        if (selected1 == null) {
            selected1 = clicked;
        } else if (selected2 == null) {
            selected2 = clicked;
            moves++;
            updateMoveLabel();
            isProcessing = true; 
    
            if (selected1.getImagePath().equals(selected2.getImagePath())) {
                SoundPlayer.play("match.mp3",1,0.7);
                selected1.setMatched(true);
                selected2.setMatched(true);
                selected1 = null;
                selected2 = null;
                isProcessing = false;
    
                if (allCards.stream().allMatch(Card::isMatched)) {
                    showWinAnimation();
                }
            } else {
                SoundPlayer.play("fail.mp3",0.1,0.7);
                PauseTransition wait = new PauseTransition(Duration.seconds(1));
                wait.setOnFinished(ev -> {
                    selected1.flip();
                    selected2.flip();
                    selected1 = null;
                    selected2 = null;
                    isProcessing = false; 
                });
                wait.play();
            }
        }
    }
    

    private void updateMoveLabel() {
        movesLabel.setText("Movimientos: " + moves);
    }

    private void showWinAnimation() {
        timer.stop();
        SoundPlayer.play("win.mp3",1,0);

        for (Card c : allCards) {
            TranslateTransition shake = new TranslateTransition(Duration.millis(100), c);
            shake.setByX(5); shake.setCycleCount(6); shake.setAutoReverse(true);
            shake.play();
        }

        Label win = new Label("🎉 ¡Ganaste! 🎉");
        win.setStyle("-fx-font-size: 36px; -fx-text-fill: green; -fx-font-weight: bold;");
        ((StackPane) grid.getScene().getRoot()).getChildren().add(win);

        PauseTransition wait = new PauseTransition(Duration.seconds(7));
        wait.setOnFinished(e -> returnToMenu());
        wait.play();
    }

    private void returnToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/ies/puerto/menu.fxml"));
            Scene scene = new Scene(loader.load(), 450, 860);
            Stage stage = (Stage) grid.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---- CARTA INTERNA ----

    private static class Card extends ImageView {
        private final String imagePath;
        private boolean matched = false;
        private boolean flipped = false;
        private final Image back = new Image(Card.class.getResource("/images/back.png").toString());

        public Card(String path) {
            this.imagePath = path;
            setImage(back);
            setFitWidth(100); setFitHeight(100);
        }

        public void flip() {
            RotateTransition rt = new RotateTransition(Duration.millis(200), this);
            rt.setAxis(Rotate.Y_AXIS);
            rt.setFromAngle(0);
            rt.setToAngle(90);
            rt.setOnFinished(e -> {
                flipped = !flipped;
        
                Image imgToShow = flipped
                        ? new Image(Card.class.getResource(imagePath).toString())
                        : back;
                setImage(imgToShow);
        
                RotateTransition rt2 = new RotateTransition(Duration.millis(200), this);
                rt2.setAxis(Rotate.Y_AXIS);
                rt2.setFromAngle(270);
                rt2.setToAngle(360);
                rt2.play();
            });
            rt.play();
        }
        
        

        public boolean isFlipped() { return flipped; }
        public boolean isMatched() { return matched; }
        public void setMatched(boolean b) { matched = b; }
        public String getImagePath() { return imagePath; }
    }
}
