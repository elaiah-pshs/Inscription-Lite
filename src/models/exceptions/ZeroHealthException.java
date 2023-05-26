/**
*
* @throws ZeroHealthExcepttion < when the target of an attack already has zero health >
*
**/

package models.exceptions;

public class ZeroHealthException extends Exception {
    private char type;

    public ZeroHealthException(String t) {
        super("Error: " + t + " has no more health left!");
        this.type = t.charAt(0);
    }

    
    /** 
     * @return char
     */
    public char getType() {
        return this.type;
    }
}
