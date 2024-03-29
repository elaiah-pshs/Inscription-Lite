/*
 * SigilEffect.java
 * 
 * This model interface serves as the template for every sigil effect that can
 * be invoked in a game. When a character or avatar attacks or takes damage,
 * their sigil effects arrays will be checked. From there, every sigil effect
 * whose trigger matches the most recent event will be applied. All possible
 * sigil effects will be stored in a static array in Sigil.java as anonymous
 * subclasses of this interface, and the value of a sigil's effect variable
 * will be taken from this array. As in Events.java, we did this to make future
 * development easier, as updates to the game will inevitably add more sigil
 * effects.
 */

package models.patterns;

import models.enums.Pointers;

public interface SpellEffect {
    public void applyEffect(Pointers target);
}

/*
 * Details on the implementation of sigil effects:
 * - All sigil effects will be stored as separate classes, each being in their
 *   own file at the sigil_effects directory
 * - Every sigil effect will have their own corresponding effect code, which is
 *   unique to the sigil effect it corresponds to
 * - When the cards are being read, every sigil will be assigned their sigil
 *   effect using the sigil effects processor based on sigil effect codes
 * - Sigil effects will use data from the most recent event when applied
 * - The apply effects method will be the one to add the sigil effect event
 */
