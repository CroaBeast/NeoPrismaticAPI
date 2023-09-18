package me.croabeast.neoprismatic.rgb;

import lombok.var;
import me.croabeast.neoprismatic.NeoPrismaticAPI;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Pattern;

public final class MultipleRGB extends RGBParser {

    static final String[] RAINBOW_PATTERN_STRINGS = {
            "<rainbow:(\\d{1,3})>(.+?)</rainbow>", "<r:(\\d{1,3})>(.+?)</r>"
    };

    static String gradientPattern(String prefix) {
        var hex = prefix + "([\\da-f]{6})";
        return "<" + hex + ">(.+?)</" + hex + ">";
    }

    static RGBAction gradientParser(String prefix) {
        return (pattern, string, isLegacy) -> {
            var match = pattern.matcher(string);

            while (match.find()) {
                String x = match.group(1), text = match.group(2),
                        z = match.group(3),
                        r = "(?i)<" + prefix + "([\\da-f]{6})>";

                var inside = Pattern.compile(r).matcher(text);
                var array = text.split(r);

                var ids = new ArrayList<String>();

                ids.add(x);
                while (inside.find()) ids.add(inside.group(1));

                ids.add(z);

                var result = new StringBuilder();
                int i = 0;

                while (i < ids.size() - 1) {
                    result.append(NeoPrismaticAPI.applyGradient(
                            array[i],
                            getColor(ids.get(i)),
                            getColor(ids.get(i + 1)),
                            isLegacy
                    ));
                    i++;
                }

                string = string.replace(match.group(), result);
            }

            return string;
        };
    }

    static RGBAction gradientStrip(String s) {
        return (pattern, string, b) -> {
            var match = pattern.matcher(string);

            while (match.find()) {
                var array = match.group(2).split("(?i)<" + s + "([\\da-f]{6})>");
                string = string.replace(match.group(), String.join("", array));
            }

            return string;
        };
    }

    public MultipleRGB() {
        parserMap.
                put(gradientPattern("g:"), gradientParser("g:")).
                put(gradientPattern("#"), gradientParser("#")).
                putAll((p, s, b) -> {
                    var matcher = p.matcher(s);

                    while (matcher.find()) {
                        String g = matcher.group(), c = matcher.group(2);
                        float f = Float.parseFloat(matcher.group(1));

                        s = s.replace(g, NeoPrismaticAPI.applyRainbow(c, f, b));
                    }

                    return s;
                }, RAINBOW_PATTERN_STRINGS);

        stripMap.
                put(gradientPattern("g:"), gradientStrip("g:")).
                put(gradientPattern("#"), gradientStrip("#")).
                putAll((p, s, b) -> {
                    var matcher = p.matcher(s);

                    while (matcher.find())
                        s = s.replace(matcher.group(), matcher.group(2));

                    return s;
                }, RAINBOW_PATTERN_STRINGS);
    }
}
