package es.ies.puerto.controller;



import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class Card extends ImageView {
    private final String imagePath;
    private boolean matched = false;
    private boolean flipped = false;
    private final Image back = new Image(Card.class.getResource("/images/back.png").toString());

    public Card(String path) {
        this.imagePath = path;
        setImage(back);
        setFitWidth(100);
        setFitHeight(100);
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

    public boolean isFlipped() {
        return flipped;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean b) {
        matched = b;
    }

    public String getImagePath() {
        return imagePath;
    }
}
