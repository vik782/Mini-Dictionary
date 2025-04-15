/**
 * Vincent Kurniawan
 *
 * DictionaryFunctions.java: The interface to define abstract method operations of search, add, update, and remove
 *
 */

package Dictionary;

import Response.DuplicateWord;
import Response.InvalidSearch;

public interface DictionaryFunctions {

    String search(String keyword) throws InvalidSearch;
    void add(String keyword, String meaning) throws DuplicateWord;
    void remove(String keyword) throws InvalidSearch;
    void update(String keyword, String meaning ) throws InvalidSearch;

}
