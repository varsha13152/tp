package seedu.innsync.model.util;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;

import seedu.innsync.Main;

/**
 * Contains utility methods to check if a country code is valid
 */
public class CountryCodeUtil {
    private final HashSet<String> codes = new HashSet<>();

    public CountryCodeUtil() {
        this.init();
    }

    /**
     * Reads from a file containing all valid country codes
     * and saves it into local HashSet
     */
    private void init() {
        String filename = "countrycodes.txt";
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filename);
        if (inputStream == null) {
            System.out.println("CountryCodeUtil: Country Codes Data File not found!");
            return;
        }
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            codes.add(scanner.nextLine());
        }
        scanner.close();
        System.out.println("CountryCodeUtil: Found " + codes.size() + " codes.");
    }

    /**
     * Checks against list of codes to see if a country code is valid
     * @param code code to check
     * @return True if it is valid, false otherwise
     */
    public boolean existsCountryCode(String code) {
        return this.codes.contains(code);
    }
}
