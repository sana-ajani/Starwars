/**
* This class represents Stormtrooper class which extends to Soldier class.
*/

import java.util.Random;

public class Stormtrooper extends Soldier {

    private double attackValue;
    private double dmgBonus;

    /**
     * This constructs a Stormtrooper.
     * 
     * @param health     is the health of the Stormtrooper.
     * @param attack     is the attack damage in double of the Stormtrooper.
     * @param defense    is the defense amount in double of the Stormtrooper.
     * @param identifier is the identification of the Stormtrooperr.
     */
    public Stormtrooper(double health, double attack, double defense, String identifier) {
        super(health, attack, defense, identifier);
    }

    /**
     * This method creates identification for Stormtrooper.
     * 
     * @return Stormtrooper with his own identification.
     */
    public String getName() {
        return "Stormtrooper " + getIdentifier();
    }

    /**
     * This method represents how Stormtrooper attacks.
     * 
     * @param target is the target that Stormtrooper attacks.
     * @return attackValue is the damange tha has been dealt to the target. If no
     *         damage has been dealt, it returns 0.0.
     */
    public double attack(Soldier target) {
        Random random = new Random();
        int randomNum = random.nextInt(10) + 1;
        if (randomNum <= 6) {
            attackValue = getAttack();
            dmgBonus = attackValue * 0.25;
            target.hurt(attackValue + dmgBonus);
            return attackValue;
        }
        return 0.0;
    }
}