package com.noleme.nlp;

import com.noleme.flow.io.input.Input;

import java.util.List;
import java.util.Locale;

/**
 * @author Pierre Lecerf (plecerf@lumiomedical.com)
 * Created on 2020/12/23
 */
public class Main
{
    private static final List<String> sampleTitles = List.of(
        "Rome", "France", "Greece", "Sandro_Botticelli", "DNA", "medication",
        "Wikipedia", "Wikidata", "Apache_Beam", "Photography", "Rabbit", "Lion",
        "Ancient_Greece", "Julius_Caesar", "Caravaggio", "Joseph_Mallord_William_Turner",
        "Biophysics", "Philosophy", "Astrophysics", "Rocket", "Lorem_Ipsum", "Fish",
        "Japan", "Kii_Peninsula", "Mediterranean", "Earth", "Corinth", "Horse", "Chemistry",
        "Natural_Language_Processing", "Extract-transform-load", "Pfizer", "Christmas", "Amphictiony",
        "Barack_Obama", "Lanzarote", "Iceland", "Greenland", "Scotland", "Norway", "Athens", "Sophist"
    );

    public static void main(String[] args) throws Exception
    {
        List<String> titles = args.length == 0 ? sampleTitles : List.of(args);

        var pipe = new SampleNLP("sample-nlp/", 16, Locale.ENGLISH).compile();
        pipe.run(Input.of("titles", titles));
    }
}
