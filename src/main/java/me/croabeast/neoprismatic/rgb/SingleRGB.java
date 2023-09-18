package me.croabeast.neoprismatic.rgb;

import lombok.var;
import me.croabeast.neoprismatic.NeoPrismaticAPI;

public final class SingleRGB extends RGBParser {

    public static final String[] PATTERNS = {
            "[{]#([a-f\\d]{6})[}]", "<#([a-f\\d]{6})>", "%#([a-f\\d]{6})%",
            "\\[#([a-f\\d]{6})]", "&?#([a-f\\d]{6})", "&x([a-f\\d]{6})"
    };

    public SingleRGB() {
        parserMap.putAll((p, s, b) -> {
            var m = p.matcher(s);

            while (m.find()) {
                var c = NeoPrismaticAPI.fromString(m.group(1), b);
                s = s.replace(m.group(), c.toString());
            }

            return s;
        }, PATTERNS);

        stripMap.putAll((p, s, b) -> {
            var m = p.matcher(s);

            while (m.find()) s = s.replace(m.group(), "");
            return s;
        }, PATTERNS);
    }
}
