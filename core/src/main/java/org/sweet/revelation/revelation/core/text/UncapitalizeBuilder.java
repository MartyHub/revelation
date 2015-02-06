package org.sweet.revelation.revelation.core.text;

public class UncapitalizeBuilder {

    public static String build(String s) {
        return new UncapitalizeBuilder().add(s)
                                        .build();
    }

    private final StringBuilder sb;

    public UncapitalizeBuilder() {
        sb = new StringBuilder();
    }

    public UncapitalizeBuilder add(String s) {
        if (s != null && s.length() > 0) {
            if (sb.length() == 0) {
                sb.append(Character.toLowerCase(s.charAt(0)))
                  .append(s.substring(1));
            } else {
                sb.append(s);
            }
        }

        return this;
    }

    public String build() {
        return sb.toString();
    }
}
