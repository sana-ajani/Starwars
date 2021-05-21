/**
 * This class represents Sith class which extends to Soldier class and
 * implements ForceSensitive class.
 */

public class Sith extends Soldier implements ForceSensitive {

    private double power;
    private boolean force;
    private double attackValue;
    private double boostAttack;
    private double health;

    /**
     * This constructs a Sith.
     * 
     * @param health     is the health of the Sith.
     * @param attack     is the attack damage in double of the Sith.
     * @param defense    is the defense amount in double of the Sith.
     * @param identifier is the identification of the Sith.
     */
    public Sith(double health, double attack, double defense, String identifier) {

        super(health, attack, defense, identifier);
        power = (attack + defense) * (1 / 6);
    }

    /**
     * This method creates identification for Stormtrooper.
     * 
     * @return Sith with his own identification.
     */
    public String getName() {
        return "Sith " + getIdentifier();
    }

    /**
     * This method helps the Sith to use the Force.
     */
    public void useTheForce() {
        health = health - power * (1 / 10);
        changeAttack(power);
        force = false;
    }

    /**
     * This method represents how Sith attacks.
     * 
     * @param target is the target that Sith attacks.
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