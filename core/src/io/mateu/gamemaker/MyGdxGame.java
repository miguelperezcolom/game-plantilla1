package io.mateu.gamemaker;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyGdxGame extends Game {
	SpriteBatch batch;
	BitmapFont font;

	private Element xml;
	private Juego juego;

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();

		try {
			xml = new SAXBuilder().build(Gdx.files.internal("juego.xml").read()).getRootElement();

			juego = new Juego(this, xml);

			juego.start();

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Juego=" + xml.getAttributeValue("nombre"));

	}

	
	@Override
	public void dispose () {
		batch.dispose();
		//imgJugador.dispose();
	}
}
