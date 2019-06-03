package ch.hepia.model.program;

import ch.hepia.model.kata.KataSubscription;

import java.util.ArrayList;

public class ProgramSubscription {

    public String _id,id, iduser, idprogram;
    public boolean status;
    public int nbKataDone;
    public ArrayList<KataSubscription> katas;
}
