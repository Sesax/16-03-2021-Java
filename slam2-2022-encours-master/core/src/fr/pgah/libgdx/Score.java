package fr.pgah.libgdx;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Score {
    
    double temps;

    public Score (){
        this.temps = 0;
    }

    public Score (double temps){
        this.temps = temps;
    }

    public void dessiner(BitmapFont font, SpriteBatch batch, int coordX, int coordY){
        font.draw(batch, String.valueOf(temps)+" Secondes de jeu", coordX, coordY);
    }
}
