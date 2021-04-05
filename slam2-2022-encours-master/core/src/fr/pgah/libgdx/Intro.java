package fr.pgah.libgdx;

import java.util.ArrayList;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Intro extends ApplicationAdapter {

  final int NB_SPRITES = 5;
  int nb_vie;
  SpriteBatch batch;
  int longueurFenetre;
  int hauteurFenetre;
  ArrayList<Sprite> sprites;
  Scores scores;
  int gameMode;
  boolean gameOver;
  Texture gameOverTexture;
  Sprite barreMenu;
  Sprite barreMenu2;
  Sprite back;
  BitmapFont font;
  Json json;
  FileHandle file;
  double timer;

  @Override
  public void create() {
    batch = new SpriteBatch();
    longueurFenetre = Gdx.graphics.getWidth();
    hauteurFenetre = Gdx.graphics.getHeight();

    json = new Json();
    file = Gdx.files.local("scores.json");
    font = new BitmapFont();
    nb_vie = NB_SPRITES;
    gameMode = 0;
    gameOver = false;
    gameOverTexture = new Texture("game_over.png");
    barreMenu = new Sprite("barreMenu.png", 150, 280);
    barreMenu2 = new Sprite("barreMenu.png", 150, 200);
    back = new Sprite("back.png", 20, 400);
    sprites = new ArrayList<>();
    scores = new Scores();
  }

  private void initialisationSprites() {
    for (int i = 0; i < NB_SPRITES; i++) {
      sprites.add(new Sprite("chien.png"));
    }
  }

  private void initialiserScores() {
    String text = file.readString();
    scores = json.fromJson(Scores.class, text);
  }

  @Override
  public void render() {
    // gameOver est mis à TRUE dès qu'un "hit" est repéré
    if (gameMode == 0){
      reinitialiserArrierePlan();
      checkClick();
      dessinerMenu();
    } else if (gameMode == 1) {
      timer+=1;
      reinitialiserArrierePlan();
      checkCollide();
      majEtatProtagonistes();
      majEtatJeu();
      dessiner();
    } else if (gameMode == 2){
      reinitialiserArrierePlan();
      dessinerTableau();
      checkBack();
    }
  }

  private void reinitialiserArrierePlan() {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
  }

  private void checkCollide(){
    Sprite spriteTouch = null;
    for (Sprite sprite : sprites) {
      if (sprite.isClicked()){
        spriteTouch = sprite;
        nb_vie -= 1;
        break;
      }
    }
    if (spriteTouch != null){
      sprites.remove(spriteTouch);
    }
  }
  
  public void checkClick(){
    if (barreMenu.isClicked()){
      gameOver = false;
      nb_vie = NB_SPRITES;
      gameMode = 1;
      timer = 0;
      initialisationSprites();
    } else if (barreMenu2.isClicked()){
      gameMode = 2;
      gameOver = false;
      nb_vie = NB_SPRITES;
      initialiserScores();
    }
  }

  public void checkBack(){
    if (back.isClicked()){
      gameMode = 0;
    }
  }

  private void majEtatProtagonistes() {
    // Sprites
    for (Sprite sprite : sprites) {
      sprite.majEtat();
    }
  }

  private void majEtatJeu() {
    // On vérifie si le jeu continue ou pas
    if (nb_vie <= 0){
      gameOver = true;
      scores.addScore(new Score(timer / 60));
      file.writeString(json.prettyPrint(scores), false);
    }
  }

  private void dessiner() {
    batch.begin();
    if (gameOver) {
      // cet affichage GAME OVER ne se fera qu'une fois
      // à la fin de la dernière frame au moment du "hit"
      // puisqu'ensuite le render ne fera plus rien
      batch.draw(gameOverTexture, 100, 100);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException ie) {

      }
      if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
        gameMode = 0;
      }
    } else {
      // Affichage "normal", jeu en cours
      for (Sprite sprite : sprites) {
        sprite.dessiner(batch);
      }
    }
    batch.end();
  }

  private void dessinerMenu() {
    batch.begin();
    barreMenu.dessiner(batch);
    font.draw(batch, "Jouer", 200, 305);
    barreMenu2.dessiner(batch);
    font.draw(batch, "Tableau des scores", 200, 225);
    batch.end();
  }

  private void dessinerTableau() {
    batch.begin();
    back.dessiner(batch);
    scores.dessinerScores(font, batch, 200, 200);
    batch.end();
  }
}
