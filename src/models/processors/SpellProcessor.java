package models.processors;

import models.enums.SigilCodes;
import models.patterns.SpellEffect;

/**
 * <code>SpellProcessor</code> is in charge of assigning all sigil cards in the game to their sigil effects.
 * 
 * <strong>Note: this class is still in development and is not completed! It was excluded from the first release of this project as our group determined that we do not have enough time to complete this class and its functions.</strong>
 */
public class SpellProcessor {
    
    /** 
     * @param e
     * @return SpellEffect
     */
    public static SpellEffect assignEffect(String e) {
        SigilCodes effect_code = SigilCodes.valueOf(e);

        switch (effect_code) {
            default:
                return null;
        }
    }
}
