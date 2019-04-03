package eu.tsystems.mms.tic.testframework.mobile.monkey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rnhb on 18.08.2016.
 */
public class MonkeyNameGenerator {
    private static List<String> vocals = new ArrayList<>();
    private static List<String> startConsonants = new ArrayList<>();
    private static List<String> endConsonants = new ArrayList<>();
    private static List<String> nameInstructions = new ArrayList<>();

    static {
        vocals.addAll(Arrays.asList("a", "e", "i", "o", "u", "ei", "ai", "ou", "j",
                "ji", "yu", "oi", "au", "oo"));

        startConsonants.addAll(Arrays.asList("b", "c", "d", "f", "g", "h", "k",
                "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x", "z",
                "ch", "bl", "br", "fl", "gl", "gr", "kl", "pr", "st", "sh",
                "th", "sch"));

        endConsonants.addAll(Arrays.asList("b", "d", "f", "g", "h", "k", "l", "m",
                "n", "p", "r", "s", "t", "v", "w", "z", "ch", "gh", "nn", "st",
                "sh", "th", "tt", "ss", "pf", "nt", "ng"));

        MonkeyNameGenerator.nameInstructions.addAll(Arrays.asList("ve", "cveve", "cve", "veve"));
    }


    public static String getName() {
        return firstCharUppercase(getNameByInstructions(getRandomElementFrom(nameInstructions)));
    }

    private static int randomInt(int min, int max) {
        return (int) (min + (Math.random() * (max + 1 - min)));
    }

    private static String getNameByInstructions(String nameInstructions) {
        String name = "";
        int l = nameInstructions.length();

        for (int i = 0; i < l; i++) {
            char x = nameInstructions.charAt(0);
            switch (x) {
                case 'v':
                    name += getRandomElementFrom(vocals);
                    break;
                case 'c':
                    name += getRandomElementFrom(startConsonants);
                    break;
                case 'e':
                    name += getRandomElementFrom(endConsonants);
                    break;
            }
            nameInstructions = nameInstructions.substring(1);
        }
        return name;
    }

    private static String firstCharUppercase(String name) {
        return Character.toString(name.charAt(0)).toUpperCase() + name.substring(1);
    }

    private static String getRandomElementFrom(List v) {
        return v.get(randomInt(0, v.size() - 1)).toString();
    }

    public static void main(String[] args){
        for (int i = 0; i < 100; i++) {
            System.out.println(getName());
        }
    }
}
