package eu.tsystems.mms.tic.testframework.mobile.monkey;

import eu.tsystems.mms.tic.testframework.mobile.monkey.actions.MonkeyAbility;
import eu.tsystems.mms.tic.testframework.mobile.monkey.actions.MonkeyAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnhb on 19.08.2016.
 */
public class MonkeyBrain implements Serializable{

    private List<MonkeyAction> executedActions;
    private List<MonkeyAbility> learnedAbilities;

    public MonkeyBrain() {
        executedActions = new ArrayList<>();
        learnedAbilities = new ArrayList<>();
    }

    public void learnNewAbility(MonkeyAbility monkeyAbility) {
        learnedAbilities.add(monkeyAbility);
    }

    public MonkeyAbility rememberAnAbility() {
        int index = (int) (Math.random() * learnedAbilities.size());
        MonkeyAbility monkeyAbility = learnedAbilities.get(index);
        return monkeyAbility;
    }

    public void memorizeMonkeyAction(MonkeyAction monkeyAction) {
        executedActions.add(monkeyAction);
    }

}
