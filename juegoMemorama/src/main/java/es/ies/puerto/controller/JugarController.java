package es.ies.puerto.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.*;

import es.ies.puerto.model.UsuarioEntity;
import es.ies.puerto.model.UsuarioEstadisticasEntity;
import es.ies.puerto.model.UsuarioServiceModel;
import es.ies.puerto.model.UsuarioSesion;

public class JugarController {

    @FXML
    private GridPane grid;
    @FXML
    private Label timeLabel;
    @FXML
    private Label movesLabel;

    private int pairCount = 8;
    private int moves = 0;
    private int secondsElapsed;
    private Timeline timer;

    private Card selected1 = null;
    private Card selected2 = null;

    private boolean isProcessing = false;

    private boolean contrareloj = false;
    private int tiempoLimite = 75;
    private List<Card> allCards = new ArrayList<>();

    private UsuarioEntity usuarioActual;

    public void setUsuarioActual(UsuarioEntity usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    private String dificultadActual;

    public void setDificultadActual(String dificultad) {
        this.dificultadActual = dificultad;
    }

    /**
     * Metodo que inicializa las variables del juego
     */
    public void inicializarJuego() {

        switch (pairCount) {
            case 4:
                dificultadActual="facil";
                break;
            case 8:
                dificultadActual="medio";
                break;
            case 18:
                dificultadActual="dificil";
                break;

          
        }
        usuarioActual = UsuarioSesion.getInstancia().getUsuario();
        setupGame();
    }

    public void setPairCount(int pairCount) {
        this.pairCount = pairCount;
    }

    public void setContrareloj(boolean contrareloj) {
        this.contrareloj = contrareloj;
    }

    /**
     * Metodo que que da comienzo al juego y aleatorizar las cartas
     */
    private void setupGame() {
        List<String> paths = new ArrayList<>();
        for (char c = 'A'; c < 'A' + pairCount; c++) {
            String path = "/images/" + c + ".png";
            paths.add(path);
            paths.add(path);
        }
        Collections.shuffle(paths);

        grid.getChildren().clear();
        allCards.clear();
        moves = 0;
        updateMoveLabel();
        startTimer();

        int cols = (pairCount == 18) ? 6 : 4;
        int rows = (int) Math.ceil((paths.size()) / (double) cols);

        int index = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols && index < paths.size(); c++) {
                Card card = new Card(paths.get(index++));
                card.setOnMouseClicked(event -> {
                    try {
                        handleClick(event);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                grid.add(card, c, r);
                allCards.add(card);
            }
        }

    }

    /**
     * Metodo para que el contador de tiempo y el modo contrarreloj funcione
     */
    private void startTimer() {
        secondsElapsed = contrareloj ? tiempoLimite : 0;
        timeLabel.setText("Tiempo: " + secondsElapsed + "s");

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (contrareloj) {
                secondsElapsed--;
                timeLabel.setText("Tiempo: " + secondsElapsed + "s");
                if (secondsElapsed <= 0) {
                    timer.stop();
                    try {
                        handleGameOver();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            } else {
                secondsElapsed++;
                timeLabel.setText("Tiempo: " + secondsElapsed + "s");
                this.secondsElapsed=secondsElapsed;            }
        }));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.play();
    }

    /**
     * Metodo que realiza la accion cuando hay un evento de click
     * @param e click del raton
     * @throws SQLException
     */
    private void handleClick(MouseEvent e) throws SQLException {
        if (isProcessing)
            return;

        Card clicked = (Card) e.getSource();
        if (clicked.isMatched() || clicked == selected1 || clicked == selected2 || clicked.isFlipped())
            return;

        clicked.flip();

        SoundPlayer.play("flip.mp3", 1, 0);

        if (selected1 == null) {
            selected1 = clicked;
        } else if (selected2 == null) {
            selected2 = clicked;
            moves++;
            updateMoveLabel();
            isProcessing = true;

            if (selected1.getImagePath().equals(selected2.getImagePath())) {
                SoundPlayer.play("match.mp3", 1, 0.7);
                selected1.setMatched(true);
                selected2.setMatched(true);
                selected1 = null;
                selected2 = null;
                isProcessing = false;

                if (allCards.stream().allMatch(Card::isMatched)) {
                    showWinAnimation();
                }
            } else {
                SoundPlayer.play("fail.mp3", 0.1, 0.7);
                PauseTransition wait = new PauseTransition(Duration.seconds(0.8));
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

    /**
     * Metodo que actualiza el contador de movimientos
     */
    private void updateMoveLabel() {
        movesLabel.setText("Movimientos: " + moves);
    }

    /**
     * Metodo que muestra la animacion de victoria
     * @throws SQLException
     */
    private void showWinAnimation() throws SQLException {
        timer.stop();
        SoundPlayer.play("win.mp3", 1, 0);

        for (Card c : allCards) {
            TranslateTransition shake = new TranslateTransition(Duration.millis(100), c);
            shake.setByX(5);
            shake.setCycleCount(6);
            shake.setAutoReverse(true);
            shake.play();
        }
        guardarEstadisticas(true);
        Text win = new Text(" ¡Ganaste! ");
        win.setFont(Font.font("System", 60));
        win.setFill(Color.YELLOW);
        win.setStroke(Color.BLACK);
        win.setStrokeWidth(0.7);
        ((StackPane) grid.getScene().getRoot()).getChildren().add(win);
        PauseTransition wait = new PauseTransition(Duration.seconds(7));
        wait.setOnFinished(e -> returnToMenu());
        wait.play();
    }

    private boolean isGameOver = false;

    /**
     * Metodo que muestra la animacion de derrota
     * @throws SQLException
     */
    private void handleGameOver() throws SQLException {
        isGameOver = true;
        for (Card c : allCards) {
            c.setDisable(true);
        }

        SoundPlayer.play("lose.mp3", 1, 0);
        guardarEstadisticas(false);
        Text lose = new Text(" ¡Perdiste! ");
        lose.setFont(Font.font("System", 60));
        lose.setFill(Color.RED);
        lose.setStroke(Color.BLACK);
        lose.setStrokeWidth(0.7);
        ((StackPane) grid.getScene().getRoot()).getChildren().add(lose);

        PauseTransition wait = new PauseTransition(Duration.seconds(1.5));
        wait.setOnFinished(e -> {
            SequentialTransition st = new SequentialTransition();

            for (Card c : allCards) {
                if (c.isFlipped()) {
                    PauseTransition pt = new PauseTransition(Duration.millis(200));
                    pt.setOnFinished(ev -> {
                        c.setMatched(false);
                        SoundPlayer.play("flip.mp3", 0.5, 0);
                        c.flip();
                    });
                    st.getChildren().add(pt);
                }
            }

            PauseTransition extraWait = new PauseTransition(Duration.seconds(3));
            extraWait.setOnFinished(ev -> returnToMenu());

            st.setOnFinished(ev -> extraWait.play());
            st.play();
        });

        wait.play();
    }

    /**
     * Metodo que devuelve a la pantalla de menu
     */
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

    /**
     * Metodo principal para guardar las estadisticas
     * @param victoria valor boleano que indica si la partida esta perdida o ganada
     */
    private void guardarEstadisticas(boolean victoria) {
        if (usuarioActual == null || dificultadActual == null) {
            return;
        }
    
        UsuarioServiceModel servicio = null;
    
        try {
            servicio = new UsuarioServiceModel("src/main/resources/usuarios.db");
    
            UsuarioEstadisticasEntity estadisticas = servicio.obtenerEstadisticasPorDificultad(usuarioActual.getEmail(), dificultadActual);
    
            if (estadisticas == null) {
                estadisticas = new UsuarioEstadisticasEntity();
                estadisticas.setDificultad(dificultadActual);
            }
    
            comprobarTipoVictoria(victoria, estadisticas);
    
            servicio.actualizarEstadisticasPorDificultad(usuarioActual.getEmail(), estadisticas);
            servicio.actualizarResumenUsuario(usuarioActual.getEmail(), usuarioActual);
    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (servicio != null) {
                try {
                    servicio.cerrar();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Metodo para comprobar de que manera se ha ganado
     * @param victoria verdadero o falso
     * @param estadisticas estadisticas del usuario
     */
    private void comprobarTipoVictoria(boolean victoria, UsuarioEstadisticasEntity estadisticas){
        if (victoria) {
            if (contrareloj) {
                estadisticas.setVictoriasContrareloj(estadisticas.getVictoriasContrareloj() + 1);
            } else {
                estadisticas.setVictoriasNormal(estadisticas.getVictoriasNormal() + 1);
                int tiempoActual = secondsElapsed;
                if (estadisticas.getMejorTiempoNormal() == 0 || tiempoActual < estadisticas.getMejorTiempoNormal()) {
                    estadisticas.setMejorTiempoNormal(tiempoActual); 
                    usuarioActual.setMejorTiempoNormal(tiempoActual); 
                }
            }
            usuarioActual.setRachaDerrota(0);
            usuarioActual.setRachaVictoria(usuarioActual.getRachaVictoria() + 1);
        } else {
            usuarioActual.setDerrotasTotales(usuarioActual.getDerrotasTotales() + 1);
            usuarioActual.setRachaVictoria(0);
            usuarioActual.setRachaDerrota(usuarioActual.getRachaDerrota() + 1);
        }
    }
    
}
