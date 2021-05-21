
/**
 * Builds troops based on given parameters and simulates a battle between these troops.
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Simulation {

    private List<Soldier> soldiers;
    private List<ForceSensitive> forceSensitives;

    private List<Soldier> empire;
    private List<Soldier> rebellion;

    private int stalemateCount;

    private final Stormtrooper customTrooper;
    private final RebelSoldier customRebel;

    private static final Random RANDOM = new Random();
    private static final String[] UNIQUE_NAMES = { "Darth Vader", "Yoda", "Boba Fett", "Leia Organa",
            "Luke Skywalker", "Padmé Amidala", "R2D2", "Obi-Wan Kenobi", "Chewbacca", "Jar Jar Binks", "C-3PO",
            "Mace Windu", "Qui-Gon Jinn" };

    /**
     * Constructs a Simulation with the given number of Soldiers.
     *
     *
     * @param numRebels     The number of RebelSoldiers to include.
     * @param numJedi       The number of Jedi to include.
     * @param numTroopers   The number of Stormtroopers to include.
     * @param numSith       The number of Sith to include.
     * @param customRebel   An 'average' custom RebelSoldier (a subclass of
     *                      RebelSoldier)
     * @param customTrooper An 'average' custom Stormtrooper (a subclass of
     *                      Stormtrooper)
     */
    public Simulation(int numRebels, int numJedi, int numTroopers, int numSith, RebelSoldier customRebel,
            Stormtrooper customTrooper) {

        this.customRebel = customRebel;
        this.customTrooper = customTrooper;

        List<Jedi> jedi = generateJedi(numJedi);
        List<Sith> sith = generateSith(numSith);

        rebellion = new ArrayList<Soldier>(numRebels + numJedi);
        rebellion.addAll(generateRebelSoldiers(numRebels));
        rebellion.addAll(jedi);

        empire = new ArrayList<Soldier>(numTroopers + numSith);
        empire.addAll(generateTroopers(numTroopers));
        empire.addAll(sith);

        soldiers = new ArrayList<Soldier>(numRebels + numJedi + numTroopers + numSith);
        soldiers.addAll(empire);
        soldiers.addAll(rebellion);

        forceSensitives = new ArrayList<ForceSensitive>(numJedi + numSith);
        soldiers.stream().filter(s -> s instanceof ForceSensitive)
                .forEach(s -> forceSensitives.add((ForceSensitive) s));

    }

    /**
     * Simulates one round of attacks from every Soldier to another Soldier on the
     * opposing side.
     *
     * Before attacks are made, all ForceSensitives in the simulation are given a
     * 50/50 change to useTheForce.
     *
     * If an attack reports negative damage, the attack has failed and has not
     * harmed the Soldier being attacked.
     *
     * A report of each attack is printed to the console followed by the lsit of
     * Soldiers killed in this skirmish.
     *
     * If there are no Soldiers left in either faction, or no Soldiers have died in
     * the past three skirmishes, false is returned signaling the end of the
     * simulation.
     *
     * @param verbose Verbose output on or off.
     * @return True if the simulation can continue, false otherwise.
     */
    public boolean simulateSkirmish(boolean verbose) {
        // ForceSensitives have a 50/50 chance to useTheForce.
        forceSensitives.stream().filter(s -> RANDOM.nextBoolean()).forEach(s -> s.useTheForce());

        // Mix up who attacks when
        Collections.shuffle(soldiers);
        double totalDamage = 0.0;
        for (Soldier soldier : soldiers) {
            Soldier target;
            if (soldier instanceof RebelSoldier || soldier instanceof Jedi) {
                target = empire.get(RANDOM.nextInt(empire.size()));
            } else {
                target = rebellion.get(RANDOM.nextInt(rebellion.size()));
            }
            double damage = soldier.attack(target);
            if (verbose) {
                if (damage > 0.0) {
                    System.out.printf(soldier.getName() + " inflicted %.2f damage to " + target.getName(), damage);
                    if (target.isDead()) {
                        System.out.print(" and killed him");
                    }
                    System.out.println("!");
                } else {
                    System.out.println(soldier.getName() + " failed to damage " + target.getName());
                }
            }
            if (damage > 0.0) {
                totalDamage += damage;
            }
        }

        
        List<Soldier> deceased = removeDeceased();
        if (verbose) {
            deceased.stream().forEach(s -> System.out.println(s.getName() + " was killed."));
        }

        int rebelDeaths = (int) deceased.stream().filter(s -> s instanceof RebelSoldier || s instanceof Jedi).count();
        int empireDeaths = (int) deceased.stream().filter(s -> s instanceof Stormtrooper || s instanceof Sith).count();

        String rebelStats = "| The Rebellion lost " + rebelDeaths + " soldiers during this skirmish! " + rebellion.size()
                + " remain.";
        String empireStats = "| The Empire lost " + empireDeaths + " soldiers during this skirmish! " + empire.size()
                + " remain.";

        int chartWidth = Math.max(rebelStats.length(), empireStats.length()) + 2;
        rebelStats = rebelStats + new String(new char[chartWidth - rebelStats.length() - 1]).replace("\0", " ") + "|";
        empireStats = empireStats + new String(new char[chartWidth - empireStats.length() - 1]).replace("\0", " ")
                + "|";

        String bar = "+" + new String(new char[chartWidth - 2]).replace("\0", "-") + "+";
        String complete = "|" + new String(new char[(chartWidth - 19) / 2]).replace("\0", " ") + "Skirmish Complete"
                + new String(new char[(chartWidth - 19) / 2]).replace("\0", " ")
                + (((chartWidth % 2) == 0) ? " |" : "|");

        System.out.println(
                bar + "\n" + complete + "\n" + bar + "\n" + rebelStats + "\n" + empireStats + "\n" + bar + "\n");

        if (totalDamage < 1.0) {
            stalemateCount++;
        } else {
            stalemateCount = 0;
        }

        return (rebellion.size() > 0) && (empire.size() > 0) && stalemateCount < 3;
    }

    /**
     * @return The number of alive RebelSoldiers.
     */
    public int getNumRebelsRemaining() {
        return (int) rebellion.stream().filter(s -> s instanceof RebelSoldier).count();
    }

    /**
     * @return The number of alive Jedi.
     */
    public int getNumJediRemaining() {
        return (int) rebellion.stream().filter(s -> s instanceof Jedi).count();
    }

    /**
     * @return The number of alive Stormtroopers.
     */
    public int getNumTroopersRemaining() {
        return (int) empire.stream().filter(s -> s instanceof Stormtrooper).count();
    }

    /**
     * @return The number of alive Sith.
     */
    public int getNumSithRemaining() {
        return (int) empire.stream().filter(s -> s instanceof Sith).count();
    }

    /**
     * @return The number of alive Empire Soldiers.
     */
    public int getNumEmpireRemaining() {
        return empire.size();
    }

    /**
     * @return The number of alive Rebellion Soldiers.
     */
    public int getNumRebellionRemaining() {
        return rebellion.size();
    }

    /**
     * @return a RebelSolder with RANDOMly generated stats.
     */
    private RebelSoldier generateRandomRebel() {
        String name = generateRandomIdentifier();
        if (RANDOM.nextBoolean()) {
            double health = getRandomNormalNumber(15.0, 85.0, 50.0, 100.0);
            double attack = getRandomNormalNumber(15.0, 50.0, 0.0, 100.0);
            double defense = getRandomNormalNumber(15.0, 50.0, 0.0, 100.0);
            return new RebelSoldier(health, attack, defense, name);
        } else {
            try {
                Class<? extends RebelSoldier> custom = customRebel.getClass();
                Class<?> soldierClass = custom.getSuperclass().getSuperclass();
                Constructor ctor = custom.getDeclaredConstructor(double.class, double.class, double.class,
                        String.class);
                Field h = soldierClass.getDeclaredField("health");
                h.setAccessible(true);
                double meanHealth = h.getDouble(customRebel);
                Field a = soldierClass.getDeclaredField("attack");
                a.setAccessible(true);
                double meanAttack = a.getDouble(customRebel);
                Field d = soldierClass.getDeclaredField("defense");
                d.setAccessible(true);
                double meanDefense = d.getDouble(customRebel);
                double health = getRandomNormalNumber(15.0, meanHealth, 50.0, 100.0);
                double attack = getRandomNormalNumber(15.0, meanAttack, 0.0, 100.0);
                double defense = getRandomNormalNumber(15.0, meanDefense, 0.0, 100.0);
                return (RebelSoldier) ctor.newInstance(health, attack, defense, name);

            } catch (NoSuchMethodException | InstantiationException e) {
                String className = customTrooper.getName();
                System.err.println("Could not find the constructor " + className + "(double health, double attack, "
                        + "double defense, String identifier)!");
                System.err.println(
                        "MAKE SURE YOUR CONSTRUCTOR IN " + className + " TAKES THE SAME PARAMETERS AS SOLDIER!");
                System.exit(1);
            } catch (NoSuchFieldException e) {
                String className = customTrooper.getName();
                System.err.println("Something went wrong! BE SURE " + className + " EXTENDS RebelSoldier DIRECTLY!");
                System.exit(1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                System.exit(1);
            }

        }
        return null;
    }

    /**
     * @return a Stormtrooper with randomly generated stats.
     */
    private Stormtrooper generateRandomTrooper() {
        String name = generateRandomIdentifier();
        if (RANDOM.nextBoolean()) {
            double health = getRandomNormalNumber(15.0, 85.0, 50.0, 100.0);
            double attack = getRandomNormalNumber(20.0, 35.0, 0.0, 100.0);
            double defense = getRandomNormalNumber(15.0, 60.0, 0.0, 100.0);
            return new Stormtrooper(health, attack, defense, name);
        } else {
            try {
                Class<? extends Stormtrooper> custom = customTrooper.getClass();
                Class<?> soldierClass = custom.getSuperclass().getSuperclass();
                Constructor ctor = custom.getDeclaredConstructor(double.class, double.class, double.class,
                        String.class);
                Field h = soldierClass.getDeclaredField("health");
                h.setAccessible(true);
                double meanHealth = h.getDouble(customTrooper);
                Field a = soldierClass.getDeclaredField("attack");
                a.setAccessible(true);
                double meanAttack = a.getDouble(customTrooper);
                Field d = soldierClass.getDeclaredField("defense");
                d.setAccessible(true);
                double meanDefense = d.getDouble(customTrooper);
                double health = getRandomNormalNumber(15.0, meanHealth, 50.0, 100.0);
                double attack = getRandomNormalNumber(15.0, meanAttack, 0.0, 100.0);
                double defense = getRandomNormalNumber(15.0, meanDefense, 0.0, 100.0);
                return (Stormtrooper) ctor.newInstance(health, attack, defense, name);

            } catch (NoSuchMethodException | InstantiationException e) {
                String className = customTrooper.getName();
                System.err.println("Could not find the constructor " + className + "(double health, double attack, "
                        + "double defense, String identifier)!");
                System.err.println(
                        "MAKE SURE YOUR CONSTRUCTOR IN " + className + " TAKES THE SAME PARAMETERS AS SOLDIER!");
                System.exit(1);
            } catch (NoSuchFieldException e) {
                String className = customTrooper.getName();
                System.err.println("Something went wrong! BE SURE " + className + " EXTENDS Stormtrooper DIRECTLY!");
                System.exit(1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (InvocationTargetException e) {
                e.getCause().printStackTrace();
                System.exit(1);
            }
        }
        return null;
    }

    /**
     * Generates an array of RebelSoldiers
     * 
     * @param total The number of RebelSoldiers to generate.
     * @return an array of RANDOMly generated RebelSoldiers.
     */
    private List<RebelSoldier> generateRebelSoldiers(int total) {
        List<RebelSoldier> rebels = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            rebels.add(generateRandomRebel());
        }
        return rebels;
    }

    /**
     * Generates an array of Stormtroopers
     * 
     * @param total The number of Stormtroopers to generate.
     * @return an array of RANDOMly generated Stormtroopers.
     */
    private List<Stormtrooper> generateTroopers(int total) {
        List<Stormtrooper> troopers = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            troopers.add(generateRandomTrooper());
        }
        return troopers;
    }

    /**
     * Generates an array of Jedi
     * 
     * @param total The number of Jedi to generate.
     * @return an array of RANDOMly generated Jedi.
     */
    private List<Jedi> generateJedi(int total) {
        List<Jedi> jedi = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            double health = getRandomNormalNumber(10.0, 60.0, 50.0, 100.0);
            double attack = getRandomNormalNumber(30.0, 55.0, 0.0, 100.0);
            double defense = getRandomNormalNumber(15.0, 55.0, 0.0, 100.0);
            String identifier = generateRandomIdentifier();

            jedi.add(new Jedi(health, attack, defense, identifier));
        }
        return jedi;
    }

    /**
     * Generates an array of Sith
     * 
     * @param total The number of Sith to generate.
     * @return an array of RANDOMly generated Sith.
     */
    private List<Sith> generateSith(int total) {
        List<Sith> sith = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            double health = getRandomNormalNumber(10.0, 60.0, 50.0, 100.0);
            double attack = getRandomNormalNumber(30.0, 65.0, 0.0, 100.0);
            double defense = getRandomNormalNumber(15.0, 45.0, 0.0, 100.0);
            String identifier = generateRandomIdentifier();

            sith.add(new Sith(health, attack, defense, identifier));
        }
        return sith;
    }

    /**
     * Returns a RANDOM double from a normal distribution that has the given mean
     * and standard deviation and is contained in the range from min to max
     * inclusive.
     *
     * @param stdDev The standard deviation of the normal distrubition
     * @param mean   The mean of the normal distribution
     * @param min    The minimum value (inclusive).
     * @param max    The maximum value (inclusive).
     * @return The RANDOM double.
     */
    private static double getRandomNormalNumber(double stdDev, double mean, double min, double max) {
        double num;
        do {
            num = RANDOM.nextGaussian() * stdDev + mean;
        } while (num < min || num > max);
        return num;
    }

    /**
     * Generates a RANDOM identifer with the form XX-##. (e.g. AQ-72).
     *
     * @return The identifier.
     */
    private static String generateRandomIdentifier() {
        if (RANDOM.nextInt(100) > 1) {
            return "" + (char) (RANDOM.nextInt(26) + 'A') + (char) (RANDOM.nextInt(26) + 'A') + "-" + RANDOM.nextInt(9)
                    + +RANDOM.nextInt(9);
        } else {
            return UNIQUE_NAMES[RANDOM.nextInt(UNIQUE_NAMES.length)];
        }
    }

    /**
     * Removes all dead Soldiers from all arrays and returns an array containg these
     * removed Soldiers.
     *
     * @return An array of Soldiers who are dead.
     */
    private List<Soldier> removeDeceased() {
        List<Soldier> deceased = soldiers.stream().filter(Soldier::isDead).collect(Collectors.toList());

        forceSensitives = forceSensitives.stream().filter(s -> !deceased.contains(s)).collect(Collectors.toList());

        soldiers = soldiers.stream().filter(s -> !s.isDead()).collect(Collectors.toList());

        empire = empire.stream().filter(s -> !s.isDead()).collect(Collectors.toList());

        rebellion = rebellion.stream().filter(s -> !s.isDead()).collect(Collectors.toList());

        return deceased;
    }
}