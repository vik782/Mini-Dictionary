/**
 * Vincent Kurniawan
 *
 * CustomDictionary.java: The program to parse dictionary and store method operations of search, add, update, and remove
 *
 */

package Dictionary;

import Response.DuplicateWord;
import Response.ErrorExceptions;
import Response.InvalidSearch;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomDictionary implements DictionaryFunctions {

    private JSONObject dictionary;
    public CustomDictionary(String filePath){
        parseDictionary(filePath);
    }

    public void parseDictionary(String filePath) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            //Read dictionary in JSON format
            dictionary = (JSONObject) parser.parse(reader);

        } catch (ParseException e) {
            ErrorExceptions.dictionaryParseError(e);
        } catch (IOException e) {
            ErrorExceptions.dictionaryPathError(e);
        }

    }

    public synchronized String search(String keyword) throws InvalidSearch {

        if (dictionary.containsKey(keyword)) {
            return dictionary.get(keyword).toString();
        }
        else {
            throw new InvalidSearch();
        }

    }

    public synchronized void add(String keyword, String meaning) throws DuplicateWord {

        if (dictionary.containsKey(keyword)) {
            throw new DuplicateWord();
        }
        else {
            ArrayList<String> definitions = new ArrayList<String>();
            String[] meanings = meaning.split("\n");
            definitions.addAll(Arrays.asList(meanings));
            dictionary.put(keyword, definitions);
        }

    }

    public synchronized void remove(String keyword) throws InvalidSearch {
        if (!dictionary.containsKey(keyword)) {
            throw new InvalidSearch();
        }
        else {
            dictionary.remove(keyword);
        }
    }

    public synchronized void update(String keyword, String meaning) throws InvalidSearch {

        if (!dictionary.containsKey(keyword)) {
            throw new InvalidSearch();
        }
        else {
            ArrayList<String> definitions = new ArrayList<String>();
            String[] meanings = meaning.split("\n");
            definitions.addAll(Arrays.asList(meanings));
            dictionary.put(keyword, definitions);
        }

    }



}

