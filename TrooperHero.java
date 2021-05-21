/**
 * This class represents TrooperHero class which extends to Stormtrooper class.
 */

public class TrooperHero extends Stormtrooper {

    private double attackValue;

    /**
     * This constructs a TrooperHero.
     * 
     * @param health     is the health of the TrooperHero.
     * @param attack     is the attack damage in double of the TrooperHero.
     * @param defense    is the defense amount in double of the TrooperHero.
     * @param identifier is the identification of the TrooperHero.
     */
    public TrooperHero(double health, double attack, double defense, String identifier) {
        super(health, attack, defense, identifier);

    }

    /**
     * This method creates identification for TrooperHero.
     * 
     * @return TrooperHero with his own identification.
     */
    public String getName() {
        return "TrooperHero " + getIdentifier();
    }

    /**
     * This method represents how TrooperHero attacks.
     * 
     * @param target is the target that TrooperHero attacks.
     * @return attackValue is the damange tha has been dealt to the target. If no
     *         damage has been dealt, it returns 0.0.
     */
    public double attack(Soldier target) {
        changeAttack(getAttack() + 10);
        attackValue = getAttack();
        target.hurt(attackValue);
        return attackValue;
    }
}