package org.sweet.revelation.revelation.core.convert.impl;

import java.io.File;

public class StringToFileConverter extends SafeStringConverter<File> {

    public String getUsage() {
        return "FILE";
    }

    @Override
    public String[] complete(String s) {
        return new FilenameCompleter(s).complete();
    }

    @Override
    protected File doConvert(String s) {
        return new File(s);
    }
}
