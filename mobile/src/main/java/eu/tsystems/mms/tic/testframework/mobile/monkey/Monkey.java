package eu.tsystems.mms.tic.testframework.mobile.monkey;

import eu.tsystems.mms.tic.testframework.mobile.driver.MobileDriver;
import eu.tsystems.mms.tic.testframework.mobile.monkey.actions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by rnhb on 18.08.2016.
 *
 * Ximpanse
 */
public class Monkey {
    private static final Logger LOGGER = LoggerFactory.getLogger(Monkey.class);
    private final String name;
    private final Logger logger;
    private final MonkeyBrain monkeyBrain;

    private Monkey() {
        monkeyBrain = new MonkeyBrain();
        monkeyBrain.learnNewAbility(new ClickRandomPointAbility());
        name = MonkeyNameGenerator.getName();
        logger = LoggerFactory.getLogger(this.toString());
    }

    public static Monkey giveBirthToMonkey() {
        return new Monkey();
    }

    public void learnNewAbility(MonkeyAbility monkeyAction) {
        monkeyBrain.learnNewAbility(monkeyAction);
    }

    public void learnWhatEveryMonkeyShouldKnow() {
        monkeyBrain.learnNewAbility(new BackAbility());
        monkeyBrain.learnNewAbility(new ClickRandomElementAbility());
        monkeyBrain.learnNewAbility(new RandomSwipeAbility());
    }

    public void playWithDevice(MobileDriver mobileDriver, int playTimeInSeconds) {
        long endTime = System.currentTimeMillis() + playTimeInSeconds * 1000;
        while (System.currentTimeMillis() < endTime) {
            MonkeyAbility monkeyAbility = monkeyBrain.rememberAnAbility();
            try {
                MonkeyAction monkeyAction = monkeyAbility.performMonkeyAction(mobileDriver);
                monkeyBrain.memorizeMonkeyAction(monkeyAction);
            } catch (Exception e) {
                LOGGER.debug(e.getMessage());
                logger.info(this + " failed to perform action of " + monkeyAbility);
            }
        }

    }

    @Override
    public String toString() {
        return "Monkey " + name;
    }
}
