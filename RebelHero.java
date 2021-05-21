/**
 * This class represents Rebel Hero class which extends to Rebel Soldier class.
 */

public class RebelHero extends RebelSoldier {

    private double attackValue;

    /**
     * This constructs a Rebel Hero.
     * 
     * @param health     is the health of the Rebel Hero.
     * @param attack     is the attack damage in double of the Rebel Hero.
     * @param defense    is the defense amount in double of the Rebel Hero.
     * @param identifier is the identification of the Rebel Hero.
     */
    public RebelHero(double health, double attack, double defense, String identifier) {
        super(health, attack, defense, identifier);
    }

    /**
     * This method creates identification for Stormtrooper.
     * 
     * @return Stormtrooper with his own identification.
     */
    public String getName() {
        return "RebelHero " + getIdentifier();
    }

    /**
     * This method represents how Rebel Hero attacks.
     * 
     * @param target is the target that Rebel Hero attacks.
     * @return attackValue is the damange tha has been dealt to the target. If no
     *         damage has been dealt, it returns 0.0.
     */
    public double attack(Soldier target) {
        changeDefense(getDefense() + 20);
        attackValue = getAttack();
        target.hurt(attackValue);
        return attackValue;
    }
}