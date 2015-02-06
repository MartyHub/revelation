package org.sweet.revelation.revelation.core.text;

public class MaxLengthBuilder {

    public static MaxLengthBuilder builder() {
        return new MaxLengthBuilder();
    }

    private int maxLength;

    public MaxLengthBuilder() {
    }

    public MaxLengthBuilder add(String s) {
        if (s != null) {
            final int length = s.length();

            if (length > maxLength) {
                maxLength = length;
            }
        }

        return this;
    }

    public MaxLengthBuilder add(Iterable<String> strings) {
        if (strings != null) {
            for (String s : strings) {
                add(s);
            }
        }

        return this;
    }

    public MaxLengthBuilder add(String... strings) {
        if (strings != null) {
            for (String s : strings) {
                add(s);
            }
        }

        return this;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public String build() {
        return build("");
    }

    public String build(String s) {
        StringBuilder sb = new StringBuilder(s);

        for (int i = s.length(); i < maxLength; ++i) {
            sb.append(' ');
        }

        return sb.toString();
    }
}
