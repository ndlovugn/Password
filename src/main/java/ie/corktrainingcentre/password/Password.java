/**
 * The password must meet the following strength requirements:
 *    - Minimum 8 characters
 *    - Maximum 16 characters
 *    - Have at least one upper case (A - Z) character
 *    - Have at least one lower case (a - z) character
 *    - Have at least one number (0 - 9)
 *    - Have at least one symbol or special character from this list:
 *         ~ ! @ # $ % ^ & * ( ) _ + - = { } | [ ] \ : " ; ' < > ? , . /
 *    - Must not contain spaces or tabs
 *    - Must be free of consecutive identical characters
 *
 * An additional second pass may be used to substitute letters
 * with numbers, see the following table:
 *      Number	Letter
 *         1       L
 *         2       Z
 *         3       E
 *         4       A
 *         5       S
 *         6       b
 *         7       T
 *         8       B
 *         9       g
 *         0       O
 *
 * Any password entered by the user or generated randomly
 * must conform to the requirements above.
 */

package ie.corktrainingcentre.password;

public class Password {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 16;

    private static final int CHARACTER_COUNT = 0;
    private static final int CAPITAL_COUNT = 1;
    private static final int LOWERCASE_COUNT = 2;
    private static final int NUMBER_COUNT = 3;
    private static final int SYMBOL_COUNT = 4;
    private static final int OTHER_COUNT = 5;
    private static final int REPEAT_SEQUENCE_COUNT = 6;

    private static java.util.Random rand = new java.util.Random();

    private Password() { }

    public static boolean isPassword(String pw) {
        int[] counts = getCharacterCounts(pw);
        boolean valid = true;
        if (counts[CHARACTER_COUNT] < MIN_LENGTH) valid = false;
        else if (counts[CHARACTER_COUNT] > MAX_LENGTH) valid = false;
        else if (counts[CAPITAL_COUNT] < 1) valid = false;
        else if (counts[LOWERCASE_COUNT] < 1) valid = false;
        else if (counts[NUMBER_COUNT] < 1) valid = false;
        else if (counts[SYMBOL_COUNT] < 1) valid = false;
        else if (counts[OTHER_COUNT] > 0) valid = false;
        else if (counts[REPEAT_SEQUENCE_COUNT] > 0) valid = false;
        return valid;
    }

    /* substitute letters with numbers
     * the password must exist before calling this method
     * if the password with characters substituted
     * is not a valid password then "" is returned */
    public static String substituteNumsForLetters(String pw) {
        StringBuilder work = new StringBuilder();
        String passW;
        for(int i = 0; i < pw.length(); i++) {
            work.append( substituteNumberForLetter( pw.charAt(i) ) );
        }
        passW = work.toString();
        if ( isPassword( passW ) ) {
            return passW;
        } else {
            return new String();
        }
    }

    /* always returns a valid password */
    public static String generatePassword() {
        String passW;
        do {
            int numChars = rand.nextInt(MAX_LENGTH - MIN_LENGTH + 1) + MIN_LENGTH;
            char[] pass = new char[numChars];
            for (int i = 0; i < numChars; i++) {
                switch( rand.nextInt(4) ) {
                    case 0:
                        pass[i] = getRandomUppercase();
                        break;
                    case 1:
                        pass[i] = getRandomLowercase();
                        break;
                    case 2:
                        pass[i] = getRandomNumber();
                        break;
                    case 3:
                        pass[i] = getRandomSymbol();
                        break;
                    default:
                        pass[i] = (char)(0);
                }
            }
            passW = new String(pass);
        } while ( !isPassword(passW) );
        return passW;
    }

    private static char substituteNumberForLetter(char letter) {
        char number = letter;
        switch( letter ) {
            case 'L' : case 'l' : number = '1'; break;
            case 'Z' : case 'z' : number = '2'; break;
            case 'E' : case 'e' : number = '3'; break;
            case 'A' : case 'a' : number = '4'; break;
            case 'S' : case 's' : number = '5'; break;
            case 'b' : number = '6'; break;
            case 'T' : case 't' : number = '7'; break;
            case 'B' : number = '8'; break;
            case 'g' : number = '9'; break;
            case 'O' : case 'o' : number = '0'; break;
        }
        return number;
    }

    /*
     * CHARACTER_COUNT = 0;
     * CAPITAL_COUNT = 1;
     * LOWERCASE_COUNT = 2;
     * NUMBER_COUNT = 3;
     * SYMBOL_COUNT = 4;
     * OTHER_COUNT = 5;
     * REPEAT_SEQUENCE_COUNT = 6;
     */
    private static int[] getCharacterCounts(String pw) {
        int[] counts = new int[7];
        counts[CHARACTER_COUNT] = pw.length();
        boolean validChar = false;
        char current = 0;
        char next = 0;
        for(int i = 0; i < counts[CHARACTER_COUNT]; i++) {
            validChar = true;
            current = pw.charAt(i);
            if ( Character.isUpperCase(current) ) counts[CAPITAL_COUNT]++;
            else if ( Character.isLowerCase(current) ) counts[LOWERCASE_COUNT]++;
            else if ( Character.isDigit(current) ) counts[NUMBER_COUNT]++;
            else if ( isSymbol(current) ) counts[SYMBOL_COUNT]++;
            else {
                counts[OTHER_COUNT]++;
                validChar = false;
            }
            if (validChar && (i < (counts[CHARACTER_COUNT] - 1)) ) {
                if (current == pw.charAt(i+1)) counts[REPEAT_SEQUENCE_COUNT]++;
            }
        }
        return counts;
    }

    private static String toDebugString(String pw) {
        int[] counts = getCharacterCounts( pw );
        StringBuilder sbr = new StringBuilder();
        sbr.append("                  Password: " + pw + "\n" );
        sbr.append("                     Valid: " + isPassword( pw ) + "\n");
        sbr.append("              # Characters: " + counts[CHARACTER_COUNT] + "\n");
        sbr.append("                # Capitals: " + counts[CAPITAL_COUNT] + "\n");
        sbr.append("               # Lowercase: " + counts[LOWERCASE_COUNT] + "\n");
        sbr.append("                  # Number: " + counts[NUMBER_COUNT] + "\n");
        sbr.append("                  # Symbol: " + counts[SYMBOL_COUNT] + "\n");
        sbr.append("                 # Invalid: " + counts[OTHER_COUNT] + "\n");
        sbr.append("      # Repeating Sequence: " + counts[REPEAT_SEQUENCE_COUNT] + "\n");
        String sub = substituteNumsForLetters( pw );
        int[] countsSub = getCharacterCounts( sub );
        sbr.append("     With Letter Substitution: " + sub + "\n");
        sbr.append("                        Valid: " + isPassword( sub ) + "\n");
        sbr.append("                 # Characters: " + countsSub[CHARACTER_COUNT] + "\n");
        sbr.append("                   # Capitals: " + countsSub[CAPITAL_COUNT] + "\n");
        sbr.append("                  # Lowercase: " + countsSub[LOWERCASE_COUNT] + "\n");
        sbr.append("                     # Number: " + countsSub[NUMBER_COUNT] + "\n");
        sbr.append("                     # Symbol: " + countsSub[SYMBOL_COUNT] + "\n");
        sbr.append("                    # Invalid: " + countsSub[OTHER_COUNT] + "\n");
        sbr.append("         # Repeating Sequence: " + countsSub[REPEAT_SEQUENCE_COUNT] + "\n");
        return sbr.toString();
    }

    private static boolean isSymbol(char ch) {
        switch (ch){
            case '~': case '!': case '@': case '#': case '$': case '%':
            case '^': case '&': case '*': case '(': case ')': case '_':
            case '+': case '-': case '=': case '{': case '}': case '|':
            case '[': case ']': case '\\': case ':': case '"': case ';':
            case '\'': case '<': case '>': case '?': case ',': case '.': case '/':
                return true;
            default:
                return false;
        }
    }

    private static char getRandomUppercase() {
        return (char)(rand.nextInt(90 - 65 + 1) + 65);
    }

    private static char getRandomLowercase() {
        return (char)(rand.nextInt(122 - 97 + 1) + 97);
    }

    private static char getRandomNumber() {
        return (char)(rand.nextInt(57 - 48 + 1) + 48);
    }

    /*
     * There are 4 groups of symbols, below are
     * the groups, their ASCII codes and symbols:
     *
     *    Group 0 ASCII: 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47
     *           Symbol:  !  "  #  $  %  &  '  (  )  *  +  ,  -  .  /
     *
     *    Group 1 ASCII: 58 59 60 61 62 63 64
     *           Symbol:  :  ;  <  =  >  ?  @
     *
     *    Group 2 ASCII: 91 92 93 94 95
     *           Symbol:  [  \  ]  ^  _
     *
     *    Group 3 ASCII: 123 124 125 126
     *           Symbol:   {   |   }   ~
     */
    private static char getRandomSymbol() {
        int symbol = 0;
        switch( rand.nextInt(4) ) { // Group: 0, 1, 2, 3
            case 0:
                symbol = (rand.nextInt(47 - 33 + 1) + 33);
                break;
            case 1:
                symbol = (rand.nextInt(64 - 58 + 1) + 58);
                break;
            case 2:
                symbol = (rand.nextInt(95 - 91 + 1) + 91);
                break;
            case 3:
                symbol = (rand.nextInt(123 - 123 + 1) + 123);
                break;
            default:
                symbol = 0;
        }
        return (char)(symbol);
    }

} // end of class Password