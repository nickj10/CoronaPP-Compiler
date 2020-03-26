package model;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CompilerManager {
    private static final String PATH="data/dictionary.json";
    private Dictionary model;
    private HashMap<String, Word> dictionary;

    public CompilerManager() {
        dictionary = new HashMap<String, Word>();
        readJSON();
        addToDictionary();
    }

    /**
     * Proc that reads the json file containing the dictionary words
     */
    public void readJSON() {
        model = new Dictionary();
        Gson gson = new Gson();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(PATH));
            model = gson.fromJson(br, Dictionary.class);
        }catch (FileNotFoundException e) {
            System.out.println("\nError, file can't be found.");
            e.printStackTrace();
        }finally {
            if (br!=null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Proc that adds the words read from the file to the dictionary
     */
    public void addToDictionary() {
        for (Word word : model.getWords()) {
            dictionary.put(word.getLexeme(),word);
        }

    }

    /**
     * You have been hacked by mushimushibongbong *
     */

    public String getWord(String key) {
        Word word = dictionary.get(key);
        if (word == null) {
            return "";
        }
        return word.getToken();
    }

    /**
     * Proc that shows all the words existing in the dictionary
     */
    public void showList() {
        Set set = dictionary.entrySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()) {
            Map.Entry entry = (Map.Entry)iterator.next();
            System.out.print("Key is: "+ entry.getKey() + " - Token is: ");
            Word value = (Word) entry.getValue();
            System.out.println(value.getToken());
        }
    }
}
