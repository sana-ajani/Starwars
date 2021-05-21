/**
 * This class represents Jedi class which extends to Soldier class and
 * implements ForceSensitive class.
 */

public class Jedi extends Soldier implements ForceSensitive {

    private double power;
    private boolean force;
    private double attackValue;
    private double boostDefense;

    /**
     * This constructs a Jedi.
     * 
     * @param health     is the health of the Jedi.
     * @param attack     is the attack damage in double of the Jedi.
     * @param defense    is the defense amount in double of the Jedi.
     * @param identifier is the identification of the Jedi.
     */
    public Jedi(double health, double attack, double defense, String identifier) {

        super(health, attack, defense, identifier);
        power = defense * 0.25;
    }

    /**
     * This method creates identification for Stormtrooper.
     * 
     * @return Jedi with his own identification.
     */
    public String getName() {
        return "Jedi " + getIdentifier();
    }

    /**
     * This method helps the Jedi to use the Force.
     */
    public void useTheForce() {
        heal(power * (1 / 4));
        changeDefense(power);
        force = false;
    }

    /**
     * This method represents how Jedi attacks.
     * 
     * @param target is the target that Jedi attacks.
     * @return attackValue is the damange tha has been dealt to the target. If no
     *         damage has been dealt, it returns 0.0.
     */
    public double attack(Soldier target) {
        if (!force) {
            attackValue = getAttack();
            target.hurt(attackValue);
            force = true;
            return attackValue;
        }
        return 0.0;
    }
}