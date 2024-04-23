package com.example.praktikum_4;

import java.util.ArrayList;

public class DataSource {
    public static ArrayList<Instagram> instagrams = generateDummyInstagrams();

    private static ArrayList<Instagram> generateDummyInstagrams() {
        ArrayList<Instagram> instagrams1 = new ArrayList<>();
        instagrams1.add(new Instagram("seventeen.17","SEVENTEEN","South Korean boy band formed by Pledis Entertainment",R.drawable.svtlogo,R.drawable.svtgroup2));
        instagrams1.add(new Instagram("cat_lovers", "CATLOV"," #CatLovers",R.drawable.cat1,R.drawable.cat2));
        instagrams1.add(new Instagram("informa.id", "INFORMA","ingin perabotan baru?",R.drawable.informa, R.drawable.informa2));
        instagrams1.add(new Instagram("beauties", "beauty.y","설이와 수안이의 겨울",R.drawable.mu1,R.drawable.mu2));
        instagrams1.add(new Instagram("nail.art","NAILSART","yuhuuuu",R.drawable.nail1, R.drawable.nail2));
        return instagrams1;
    }

    public static ArrayList<Instagram> getInstagrams() {
        return instagrams;
    }
}
