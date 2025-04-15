/**
 * Vincent Kurniawan
 *
 * DuplicateWord.java: Exception for duplicate keywords in dictionary
 *
 */

package Response;

public class DuplicateWord extends Exception {

    private final String message = ResponseCodes.DUPLICATE;

    public String errorMessage (String keyword) {
        return keyword + ": " + message;
    }

    public String getMessage(){
        return message;
    }
}
