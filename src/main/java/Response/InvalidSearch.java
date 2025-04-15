/**
 * Vincent Kurniawan
 *
 * InvalidSearch.java: Exception for non-existing keywords in dictionary
 *
 */

package Response;

public class InvalidSearch extends Exception {
    private final String message = ResponseCodes.NON_EXISTENT;
    public String errorMessage(String keyword) {
        return keyword + ": " + message;
    }

    public String getMessage(){
        return message;
    }
}

