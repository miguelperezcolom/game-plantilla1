package io.mateu.gamemaker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;

public class MyGdxGame extends Game {
	SpriteBatch batch;
	BitmapFont font;

	private XmlReader.Element xml;
	private Juego juego;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();

		try {
			xml = new XmlReader().parse(Gdx.files.internal("juego.xml"));

			juego = new Juego(this, xml);

			juego.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Juego=" + xml.getAttribute("nombre"));

	}

	
	@Override
	public void dispose () {
		batch.dispose();
		//imgJugador.dispose();
	}
}
