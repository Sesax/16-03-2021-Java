package fr.pgah.libgdx;

import java.util.ArrayList;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Intro extends ApplicationAdapter {

  final int NB_SPRITES = 5;
  int nb_vie;
  SpriteBatch batch;
  int longueurFenetre;
  int hauteurFenetre;
  ArrayList<Sprite> sprites;
  boolean gameOver;
  Texture gameOverTexture;

  @Override
  public void create() {
    batch = new SpriteBatch();
    longueurFenetre = Gdx.graphics.getWidth();
    hauteurFenetre = Gdx.graphics.getHeight();

    nb_vie = NB_SPRITES;
    gameOver = false;
    gameOverTexture = new Texture("game_over.png");

    initialisationSprites();
  }

  private void initialisationSprites() {
    sprites = new ArrayList<>();
    for (int i = 0; i < NB_SPRITES; i++) {
      sprites.add(new Sprite("chien.png"));
    }
  }

  @Override
  public void render() {
    // gameOver est mis à TRUE dès qu'un "hit" est repéré
    if (!gameOver) {
      reinitialiserArrierePlan();
      checkCollide();
      majEtatProtagonistes();
      majEtatJeu();
      dessiner();
    }
  }


  private void reinitialiserArrierePlan() {
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
  }

  private void checkCollide(){
    for (int i = 0; i < sprites.size(); i++) {
      if (sprites.get(i).isClicked()){
        sprites.get(i).coordX = -100;
        sprites.get(i).coordY = -100;
        sprites.get(i).zoneDeHit.setPosition(sprites.get(i).coordX, sprites.get(i).coordY);
        sprites.get(i).vie = false;
        nb_vie -= 1;
      }
    }
  }

  private void majEtatProtagonistes() {
    // Sprites
    for (int i = 0; i < sprites.size(); i++) {
      sprites.get(i).majEtat();
    }
  }

  private void majEtatJeu() {
    // On vérifie si le jeu continue ou pas
    if (nb_vie <= 0){
      gameOver = true;
    }
  }

  private void dessiner() {
    batch.begin();
    if (gameOver) {
      // cet affichage GAME OVER ne se fera qu'une fois
      // à la fin de la dernière frame au moment du "hit"
      // puisqu'ensuite le render ne fera plus rien
      batch.draw(gameOverTexture, 100, 100);
    } else {
      // Affichage "normal", jeu en cours
      for (int i = 0; i < NB_SPRITES; i++) {
        sprites.get(i).dessiner(batch);
      }
    }
    batch.end();
  }
}
