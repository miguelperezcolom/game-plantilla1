package io.mateu.gamemaker;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.XmlReader;

public class Pantalla extends ScreenAdapter {

    private final XmlReader.Element xml;
    private AssetManager assetManager;

    public Pantalla(XmlReader.Element xml) {
        assetManager = new AssetManager();
        this.xml = xml;
    }

    @Override
    public void show() {
        super.show();
        cargarAssets();
    }

    @Override
    public void resume() {
        super.resume();
        cargarAssets();
    }

    public void cargarAssets() {

        if (xml.hasAttribute("sonidoRecord")) {
            assetManager.load(xml.getAttribute("sonidoRecord"), Sound.class);
        }

        for (XmlReader.Element en : xml.getChildrenByName("nivel")) {
            if (en.hasAttribute("musica")) {
                assetManager.load(xml.getChildByName("musica").getAttribute("src"), Music.class);
            }

            for (String n : new String[] {"jugador", "malo", "balaamiga", "balaenemiga"}) {

                for (XmlReader.Element ex : en.getChildrenByName(n)) {
                    if (ex.hasAttribute("sonidoCreado")) {
                        assetManager.load(ex.getAttribute("sonidoCreado"), Sound.class);
                    }
                    if (ex.hasAttribute("sonidoDestruido")) {
                        assetManager.load(ex.getAttribute("sonidoDestruido"), Sound.class);
                    }
                }

            }


        }

        assetManager.finishLoading();

    }


    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }


    public XmlReader.Element getXml() {
        return xml;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
