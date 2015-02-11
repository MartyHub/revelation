package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.Folder;
import org.sweet.revelation.revelation.core.convert.ConvertException;

import java.io.File;

public class StringToFolderConverter extends SafeStringConverter<Folder> {

    public String getUsage() {
        return "FOLDER";
    }

    @Override
    public String[] complete(String s) {
        return new FilenameCompleter(s).onlyFolder()
                                       .complete();
    }

    @Override
    protected Folder doConvert(String s) {
        File file = new File(s);

        if (file.exists() && !file.isDirectory()) {
            throw new ConvertException(String.format("Expecting a folder : <%s>", file.getAbsolutePath()));
        }

        return new Folder(file);
    }
}
