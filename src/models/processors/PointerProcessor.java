package models.processors;

import models.App;
import models.Avatar;
import models.Entity;

import models.enums.Locations;
import models.enums.Pointers;

import models.exceptions.PointerConversionException;

/**
 * <code>PointerProcessor</code> contains functions to process pointers.
 */
public class PointerProcessor {
    /** 
     * A list containing all pointers pointing to entities.
     */
    private static Pointers[] entity_pointers = {
        Pointers.PA, Pointers.P1, Pointers.P2, Pointers.P3, Pointers.P4,
        Pointers.OA, Pointers.O1, Pointers.O2, Pointers.O3, Pointers.O4
    };

    /** 
     * Returns the pointer associated with an <code>Entity</code>.
     * 
     * @param e             the <code>Entity</code> to convert.
     * @return              the <code>Pointer</code> associated with <code>e</code>.
     */
    public static Pointers entityToPointer(Entity e) throws PointerConversionException {
        Pointers out = null;

        for (Pointers pointer : entity_pointers) { 
            if (toEntity(pointer) == e) {
                out = pointer;
                break;
            }
        }

        return out;
    }

    /** 
     * Returns the slot number of a pointer pointing to a slot on the board.
     * 
     * @param pointer       the <code>Pointers</code> to convert.
     * @return              the slot number (1-4) of <code>pointer</code>.
     */
    public static int toInt(Pointers pointer) {
        return Character.getNumericValue(pointer.name().charAt(1));
    }

    /** 
     * Converts <code>Pointers</code> to <code>Locations</code>.
     * 
     * @param pointer       the <code>Pointers</code> to convert.
     * @return              the <code>Locations</code> equivalent to <code>pointer</code>.
     */
    public static Locations toLocation(Pointers pointer) {
        char playing_side = App.getSession().getPlayingSide();
        Locations out = null;

        if (playing_side == 'h') {
            if (pointer.toString().charAt(0) == 'P')
                out = Locations.valueOf("H" + pointer.toString().charAt(1));
            else
                out = Locations.valueOf("A" + pointer.toString().charAt(1));
        }
        else {
            if (pointer.toString().charAt(0) == 'P')
                out = Locations.valueOf("A" + pointer.toString().charAt(1));
            else
                out = Locations.valueOf("H" + pointer.toString().charAt(1));
        }

        return out;
    }

    /** 
     * Returns the entity referenced by <code>Pointers</code>.
     * 
     * @param pointer       the <code>Pointers</code> to convert.
     * @return              the <code>Entity</code> referenced by <code>pointer</code>.
     * @throws PointerConversionException   if <code>pointer</code> does not point to an entity.
     */
    public static Entity toEntity(Pointers pointer) throws PointerConversionException {
        switch (pointer) {
            case PA:
                return App.getSession().getPlayingAvatar();
            case P1:
                return App.getSession().getPlayingAvatar().getCharInSlot(0);
            case P2:
                return App.getSession().getPlayingAvatar().getCharInSlot(1);
            case P3:
                return App.getSession().getPlayingAvatar().getCharInSlot(2);
            case P4:
                return App.getSession().getPlayingAvatar().getCharInSlot(3);
            case OA:
                return App.getSession().getObservingAvatar();
            case O1:
                return App.getSession().getObservingAvatar().getCharInSlot(0);
            case O2:
                return App.getSession().getObservingAvatar().getCharInSlot(1);
            case O3:
                return App.getSession().getObservingAvatar().getCharInSlot(2);
            case O4:
                return App.getSession().getObservingAvatar().getCharInSlot(3);
            default:
                throw new PointerConversionException(pointer.name() + " does not point to an entity");
        }
    }

    /** 
     * Returns the avatar that owns a location referenced by a <code>Pointers</code>.
     * 
     * @param pointer       the <code>Pointers</code> referencing a location.
     * @return              the <code>Avatar</code> that owns the referenced location.
     */
    public static Avatar getAvatar(Pointers pointer) throws PointerConversionException {
        if (pointer.name().charAt(0) == 'P')
            return (Avatar)toEntity(Pointers.PA);
        else
            return (Avatar)toEntity(Pointers.OA);
    }

    /** 
     * Returns a <code>Pointers</code> pointing to the location directly in front of a given slot.
     * 
     * @param pointer       the <code>Pointers</code> referencing a slot.
     * @return              the <code>Pointers</code> of the opposite slot if there is a <code>Character</code> in it; else, the <code>Pointers</code> of the opposite avatar.
     */
    public static Pointers getOppositePointer(Pointers pointer) {
        String pointer_name = pointer.name();
        char avatar_code = pointer_name.charAt(0);
        int slot = toInt(pointer);

        if (avatar_code == 'P') {
            Avatar opposite = App.getSession().getObservingAvatar();
            
            if (opposite.getCharInSlot(slot - 1) == null)
                return Pointers.OA;
            else
                return Pointers.valueOf(pointer_name.replace('P', 'O'));
        }
        else {
            Avatar opposite = App.getSession().getPlayingAvatar();

            if (opposite.getCharInSlot(slot - 1) == null)
                return Pointers.PA;
            else
                return Pointers.valueOf(pointer_name.replace('O', 'P'));
        }
    }

    /** 
     * Returns an <code>Entity</code> in the location directly in front of a given slot.
     * 
     * @param pointer       the <code>Pointers</code> referencing a slot.
     * @return              the <code>Character</code> at the opposite slot if there is a <code>Character</code> in it; else, the opposite <code>Avatar</code>.
     */
    public static Entity getOppositeEntity(Pointers pointer) throws PointerConversionException {
        return toEntity(getOppositePointer(pointer));
    }
}
