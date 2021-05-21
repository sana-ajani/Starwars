/**
 * Represents basic Soldier functionality for Battlefront simulation
 */
public abstract class Soldier {
    private double health;
    private double attack;
    private double defense;
    private String identifier;

    /**
     * Constructs a Soldier with a given health, attack, defense, and identfier. `
     * NOTE: You cannot call this constructor directly as this class is abstract.
     * You must extend this class and call this constructor via a super call in the
     * subclass's constructor.
     *
     * @param health     The health value this Soldier should begin with. Starting
     *                   health must be at least 30.0 and up to 100.0
     * @param attack     The attack value this Soldier should begin with. Attack is
     *                   bounded by [0.0, 100.0];
     * @param defense    The defense value this Soldier should begin with. Defense
     *                   is bounded by [0.0, 100.0];
     * @param identifier The identifier of this Soldier.
     */
    public Soldier(double health, double attack, double defense, String identifier) {
        this.health = clamp(30, 100, health);
        this.attack = clamp(0, 100, attack);
        this.defense = clamp(0, 100, defense);
        this.identifier = identifier;
    }

    /**
     * Attacks a target soldier, using the hurt method to damage it. Concrete
     * subclasses implement the logic for how much to hurt target Soldiers by.
     * 
     * @param target The Soldier to attack.
     * @return The amount of damage dealt to the target Soldier.
     */
    public abstract double attack(Soldier target);

    /**
     * @return True if this Soldier is dead, false otherwise.
     */
    public boolean isDead() {
        return getHealth() <= 0.0;
    }

    /**
     * Heals this Soldier by the given amount.
     *
     * An amount which would raise this Soldier's health above 100.0 results in
     * health being set to 100.0.
     *
     * @param amount The amount to heal this Soldier by.
     */
    public void heal(double amount) {

        health = clamp(0, 100, health + amount);
    }

    /**
     * Hurts this Solider by the given amount.
     * 
     * @param amount The amount to hurt this Soldier by.
     */
    public void hurt(double amount) {
        double scale = 1 - ((defense - 1) / 100.0);
        health -= amount > 0.0 ? amount * scale : 0.0;
    }

    /**
     * Changes this Soldier's attack value by the given amount. The resulting attack
     * value will be bounded by [0.0, 100.0].
     *
     * A delta which would lower attack below 0.0, results in attack being set to
     * 0.0.
     *
     * A delta which would raise attack above 100.0, results in attack being set to
     * 100.0.
     *
     * @param delta The amount to change this Soldier's attack by.
     */
    public void changeAttack(double delta) {
        attack = clamp(0, 100, attack + delta);
    }

    /**
     * Changes this Soldier's defense value by the given amount. The resulting
     * defense value will be bounded by [0.0, 100.0].
     *
     * A delta which would lower defense below 0.0, results in defense being set to
     * 0.0.
     *
     * A delta which would raise defense above 100.0, results in defense being set
     * to 100.0.
     *
     * @param delta The amount to change this Soldier's defense by.
     */
    public void changeDefense(double delta) {
        defense = clamp(0, 100, defense + delta);
    }

    /**
     * @return This Soldier's health.
     */
    public double getHealth() {
        return health;
    }

    /**
     * @return This Soldier's attack.
     */
    public double getAttack() {
        return attack;
    }

    /**
     * @return This Soldier's defense.
     */
    public double getDefense() {
        return defense;
    }

    /**
     * @return This Soldier's Identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return This Solder's name with the format "Solder: [identifier]".
     */
    public String getName() {
        return "Soldier " + getIdentifier();
    }

    /**
     * @return value, clamped between start and end
     */
    private static double clamp(double start, double end, double value) {
        return value < start ? start : value > end ? end : value;
    }
}