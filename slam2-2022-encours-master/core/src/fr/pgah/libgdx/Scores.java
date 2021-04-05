package fr.pgah.libgdx;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Scores {
    
    ArrayList<Score> scores;

    public Scores (){
        scores = new ArrayList<>();
    }

    public void addScore(Score score){
        scores.add(score);
    }

    public void delScore(Score score){
        scores.remove(score);
    }

    public void dessinerScores(BitmapFont font, SpriteBatch batch, int coordX, int coordY){
        if (scores.size() < 5){
            int i = 0;
            for (Score score : scores){
                score.dessiner(font, batch, coordX, (coordY+i));
                i=i+20;
            }
        } else {
            for (int i = 0; i<5; i++){
                scores.get(i).dessiner(font, batch, coordX, (coordY+i*20));
            }
        }
    }
}
