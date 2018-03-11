package io.mateu.gamemaker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;
import io.mateu.gamemaker.test.TestScreenAdapter;

public class MyGdxGame extends Game {
	SpriteBatch batch;
	BitmapFont font;

	private XmlReader.Element xml;
	private Juego juego;
	public SuperMenu supermenu;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();

		Gdx.graphics.setTitle("Mallorca mini games");


		supermenu = new SuperMenu(this);

		setScreen(supermenu);

	}

	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
