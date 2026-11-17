import com.lowhpwarning.LowHealth;

public class LowHealthTest {
    private static int checks;
    private static void expect(boolean condition, String label) {
        if (!condition) throw new AssertionError(label);
        checks++;
    }

    private static boolean shows(float health, float threshold) {
        return LowHealth.shouldShow(true, health, threshold, true, false, false, false);
    }

    public static void main(String[] args) {
        expect(LowHealth.DEFAULT_THRESHOLD == 10f, "default threshold");
        expect(shows(7f, 10f), "7 below 10");
        expect(shows(5f, 10f), "5 below 10");
        expect(!shows(10f, 10f), "threshold equality is hidden");
        expect(!shows(20f, 10f), "full health is hidden");
        expect(shows(7.19f, 7.20f) && !shows(7.2f, 7.2f), "fractional threshold stays strict");
        expect(!shows(0f, 10f) && !shows(Float.NaN, 10f) && !shows(Float.POSITIVE_INFINITY, 10f),
                "dead or invalid health is hidden");
        expect(!LowHealth.shouldShow(false, 5f, 10f, true, false, false, false), "disabled");
        expect(!LowHealth.shouldShow(true, 5f, 10f, false, false, false, false), "death state");
        expect(!LowHealth.shouldShow(true, 5f, 10f, true, true, false, false), "spectator");
        expect(!LowHealth.shouldShow(true, 5f, 10f, true, false, true, false), "F1");
        expect(!LowHealth.shouldShow(true, 5f, 10f, true, false, false, true), "screen/menu");
        expect(LowHealth.display(7f).equals("7 HP"), "clean integer form");
        expect(LowHealth.display(7.5f).equals("7.5 HP"), "decimal HP");
        expect(LowHealth.display(7.13f).equals("7.13 HP"), "exact to two decimals");
        expect(LowHealth.display(9.999f).equals("9.99 HP"), "doesn't visually round up to threshold");
        expect(LowHealth.display(0.001f).equals("<0.01 HP"), "positive tiny health isn't 0");
        expect(LowHealth.parseThreshold("10") == 10f && LowHealth.parseThreshold("7.25") == 7.25f,
                "ordinary configuration");
        expect(LowHealth.parseThreshold("0.01") == 0.01f && LowHealth.parseThreshold("1024") == 1024f,
                "configured bounds are inclusive");
        for (String invalid : new String[]{"0", "1025", "", ".", "NaN", "-1", "1.123", "1e2", "99999"}) {
            expect(LowHealth.parseThreshold(invalid) == null, "reject: " + invalid);
        }
        System.out.println("Passed " + checks + " health predicate, formatting and settings checks.");
    }
}
