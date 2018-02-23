package io.mateu.gamemaker;

import com.badlogic.gdx.utils.XmlReader;

public class Peon extends Actor {


    public Peon(XmlReader.Element xml) {
        super(xml);
    }

    public Peon(Plantilla plantilla) {
        super(plantilla);
    }
}
