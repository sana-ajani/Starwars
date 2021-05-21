
/**
* This class represents Rebel Soldier class which extends to Soldier class.
*/

import java.util.Random;

public class RebelSoldier extends Soldier {

    private double attackValue;

    /**
     * This constructs a Rebel Soldier.
     * 
     * @param health     is the health of the Rebel Soldier.
     * @param attack     is the attack damage in double of the Rebel Soldier.
     * @param defense    is the defense amount in double of the Rebel Soldier.
     * @param identifier is the identification of the Rebel Soldier.
     */
    public RebelSoldier(double health, double attack, double defense, String identifier) {
        super(health, attack, defense, identifier);

    }

    /**
     * This method creates identification for Rebel Soldier.
     * 
     * @return Rebel Soldier with his own identification.
     */
    public String getName() {
        return "RebelSoldiers " + getIdentifier();
    }

    /**
     * This method represents how Rebel Soldier attacks.
     * 
     * @param target is the target that Rebel Soldier attacks.
     * @return attackValue is the damange tha has been dealt to the target. If no
     *         damage has been dealt, it returns 0.0.
     */
    public double attack(Soldier target) {
        Random random = new Random();
        int randomNum = random.nextInt(10) + 1;
        if (randomNum <= 8) {
            attackValue = getAttack();
            target.hurt(attackValue);
            return attackValue;
        }
        return 0.0;
    }
}