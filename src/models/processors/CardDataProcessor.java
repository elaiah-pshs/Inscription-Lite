package models.processors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Stack;

import models.Card;
import models.Character;
import models.Spell;

/**
 * <code>CardDataProcessor</code> contains functions to process data from the CSV files listing card information.
 */
public class CardDataProcessor {

    /** 
     * Returns a shuffled array of cards as read from a CSV file.
     * 
     * @param csv           the CSV file to read card information from.
     */
    public static Stack<Card> read(File csv) {
        Stack<Card> out = new Stack<Card>();
        ArrayList<Card> shuffler = new ArrayList<Card>();

        try {
            BufferedReader fr = new BufferedReader(new FileReader(csv));
            String line = "";
            String[] card_data;
            boolean skipped_headers = false;

            while ((line = fr.readLine()) != null) {
                if (!skipped_headers) {
                    skipped_headers = true;
                    continue;
                }
                
                card_data = Arrays.stream(line.split(","))
                    .filter(x -> !x.isEmpty())
                    .toArray(String[]::new);

                switch (card_data[0].charAt(0)) {
                    case 'C':
                        if (card_data.length == 7) {
                            shuffler.add(new Character(
                                card_data[1],
                                "src/static/images/card_fronts/" + card_data[2],
                                Integer.parseInt(card_data[3]),
                                Integer.parseInt(card_data[4]),
                                Integer.parseInt(card_data[5]),
                                card_data[6]
                            ));
                        }
                        else {
                            shuffler.add(new Character(
                                card_data[1],
                                "src/static/images/card_fronts/" + card_data[2],
                                Integer.parseInt(card_data[3]),
                                Integer.parseInt(card_data[4]),
                                Integer.parseInt(card_data[5])
                            ));
                        }
                        break;
                    case 'S':
                        shuffler.add(new Spell(
                            card_data[1],
                            "src/static/images/card_fronts/" + card_data[2],
                            Integer.parseInt(card_data[3]),
                            Boolean.parseBoolean(card_data[4]),
                            Boolean.parseBoolean(card_data[5]),
                            card_data[6]
                        ));
                        break;
                }
            }

            fr.close();

            Collections.shuffle(shuffler);

            for (int i = 0; i < shuffler.size(); i++)
                out.push(shuffler.get(i));

            return out;
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }
}
