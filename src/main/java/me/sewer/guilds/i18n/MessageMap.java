package me.sewer.guilds.i18n;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;

public class MessageMap extends HashMap<String, String> {

    private final Locale locale;
    private final Function<String, String> undefined;

    public MessageMap(Function<String, String> undefined, Locale locale) {
        this.locale = Objects.requireNonNull(locale);
        this.undefined = Objects.requireNonNull(undefined, "undefined");
    }

    public String get(String key) {
        String result = super.get(key);
        if (result == null) {
            result = this.undefined.apply(key);
        }
        return result;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public Function<String, String> getUndefined() {
        return this.undefined;
    }
}