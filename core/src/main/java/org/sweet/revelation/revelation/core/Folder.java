package org.sweet.revelation.revelation.core;

import java.io.File;

public class Folder {

    private final File file;

    public Folder(String pathname) {
        this.file = new File(pathname);
    }

    public Folder(File file) {
        if (file == null) {
            throw new IllegalArgumentException("File is mandatory");
        }

        this.file = file;
    }

    public File asFile() {
        return file;
    }

    public File mkdirs() {
        if (!file.exists() && !file.mkdirs()) {
            throw new IllegalArgumentException(String.format("Failed to create folder <%s>", file.getAbsolutePath()));
        }

        return file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Folder folder = (Folder) o;

        if (!file.equals(folder.file)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return file.hashCode();
    }

    @Override
    public String toString() {
        return file.toString();
    }
}
