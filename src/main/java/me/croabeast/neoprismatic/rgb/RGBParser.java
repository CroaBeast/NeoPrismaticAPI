package me.croabeast.neoprismatic.rgb;

import lombok.AccessLevel;
import lombok.Setter;
import lombok.var;
import me.croabeast.neoprismatic.util.MapBuilder;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Pattern;

public abstract class RGBParser {

    @Setter(AccessLevel.PROTECTED)
    private MapBuilder<Pattern, RGBAction> parserMap = null, stripMap = null;

    public String parse(String string, boolean isLegacy) {
        if (StringUtils.isEmpty(string)) return string;
        if (MapBuilder.isEmpty(parserMap)) return string;

        for (var e : parserMap.entries())
            string = e.getValue().apply(e.getKey(), string, isLegacy);

        return string;
    }

    public String strip(String string) {
        if (StringUtils.isEmpty(string)) return string;
        if (MapBuilder.isEmpty(parserMap)) return string;

        for (var e : stripMap.entries())
            string = e.getValue().apply(e.getKey(), string);

        return string;
    }

    @FunctionalInterface
    protected interface RGBAction {
        String apply(Pattern p, String s, boolean isLegacy);

        default String apply(Pattern p, String s) {
            return apply(p, s, false);
        }
    }

    protected static class RGBMapBuilder extends MapBuilder<Pattern, RGBAction> {

        public RGBMapBuilder put(String s, RGBAction a) {
            super.put(Pattern.compile("(?i)" + s), a);
            return this;
        }

        public RGBMapBuilder putAll(RGBAction a, String... strings) {
            for (String s : strings) put(s, a);
            return this;
        }
    }
}
