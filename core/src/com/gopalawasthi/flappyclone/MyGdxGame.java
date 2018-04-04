package com.gopalawasthi.flappyclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import sun.rmi.runtime.Log;


public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
//	ShapeRenderer renderer;
	Circle mycircle;
	Texture[] birds;
	int flapstate = 0;
	float birdY;
	float velocity=0;
	int gamestate =0;
	int gravity = 2;
	int score = 0;
	int scoringtube = 0;
    BitmapFont font;
	Texture toptube;
	Texture bottomtube;
	float gap =400;
	float maxoffset;
	Random randomgenertor;
	float tubevelocity  = 4;
	int numberoftubes = 4;
    float distancebetweentubes ;
	float tubeX[] = new float[numberoftubes];
    float tubeoffset[] = new  float[numberoftubes];
    Rectangle[] toptuberectangle ;
    Rectangle[] bottomtuberectangle;
    Texture gameover;


	@Override
	public void create () {
		batch = new SpriteBatch();
//		renderer = new ShapeRenderer();
	    font = new BitmapFont();
	    font.setColor(Color.WHITE);
	    font.getData().scale(10);

        mycircle = new Circle();
        background = new Texture("bg.png");
        gameover = new Texture("gameoverflappy.jpg");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        toptube = new Texture("toptube.png");
        bottomtube = new Texture("bottomtube.png");
        maxoffset = Gdx.graphics.getHeight()/2 -gap/2-100;
       distancebetweentubes =  Gdx.graphics.getWidth()*3/4;
       toptuberectangle = new Rectangle[numberoftubes];
       bottomtuberectangle = new Rectangle[numberoftubes];
        randomgenertor = new Random();

       startgame();

	}

	@Override
	public void render () {
        batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        if(gamestate == 1) {
             if (tubeX[scoringtube] < Gdx.graphics.getWidth()/2){
                score++;

            if(scoringtube < numberoftubes -1){
                scoringtube++;
            }else{
                scoringtube = 0;
            }
             }

            if(Gdx.input.justTouched()){
                velocity = -28;

            }
            for( int i =0 ; i< numberoftubes ; i++) {

                if(tubeX[i] < -toptube.getWidth()){

                   tubeX[i] += distancebetweentubes * numberoftubes ;
                    tubeoffset[i] = (randomgenertor.nextFloat() - 0.5f) *(Gdx.graphics.getHeight() - gap - 200);


                } else {

                    tubeX[i] = tubeX[i] - tubevelocity;

                }

                batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
                batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i]);
                toptuberectangle[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2+ gap/2 + tubeoffset[i],toptube.getWidth(),toptube.getHeight());
                bottomtuberectangle[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2 - gap/2 - bottomtube.getHeight() + tubeoffset[i],bottomtube.getWidth(),bottomtube.getHeight());
            }
            if(birdY > 0 ) {
                velocity += gravity;
                birdY = birdY - velocity;

            }else{
                gamestate = 2;
            }
        }else if (gamestate == 0){
            if(Gdx.input.justTouched()){
                gamestate =1;
            }
        } else if(gamestate == 2){
            batch.draw(gameover,Gdx.graphics.getWidth()/2- gameover.getWidth()/2,Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);

            if(Gdx.input.justTouched()){
                gamestate =1;
                startgame();
                score = 0;
                scoringtube = 0;
                velocity =0;
            }

        }

	    if(flapstate == 0){
	        flapstate =1;
        }else{
	        flapstate = 0;
        }

        batch.draw(birds[flapstate],Gdx.graphics.getWidth()/2 -birds[flapstate].getWidth()/2,birdY);
	    font.draw(batch,String.valueOf(score),100,200);

        mycircle.set(Gdx.graphics.getWidth()/2,birdY + birds[flapstate].getHeight()/2,birds[flapstate].getWidth()/2);
//        renderer.begin(ShapeRenderer.ShapeType.Filled);
//        renderer.setColor(Color.RED);
//        renderer.circle(mycircle.x,mycircle.y,mycircle.radius);
        for (int i=0 ; i < numberoftubes ; i++){
//            renderer.rect(tubeX[i],Gdx.graphics.getHeight()/2+ gap/2 + tubeoffset[i],toptube.getWidth(),toptube.getHeight());
//            renderer.rect(tubeX[i],Gdx.graphics.getHeight()/2 - gap/2 - bottomtube.getHeight() + tubeoffset[i],bottomtube.getWidth(),bottomtube.getHeight());

            if (Intersector.overlaps(mycircle,toptuberectangle[i]) || Intersector.overlaps(mycircle,bottomtuberectangle[i])){
              gamestate = 2;            }
        }
        batch.end();
//        renderer.end();
	}

    private void startgame() {
        birdY = Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;

        for (int  i =0 ; i< numberoftubes ; i++){
            tubeoffset[i] = (randomgenertor.nextFloat() - 0.5f) *(Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] =Gdx.graphics.getWidth()/2 - toptube.getWidth()/2 + i * distancebetweentubes +Gdx.graphics.getWidth()/2 ;

            toptuberectangle[i] = new Rectangle();
            bottomtuberectangle[i] = new Rectangle();

        }
    }


}
