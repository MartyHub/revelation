package org.sweet.revelation.revelation.core.convert.impl;

import java.io.File;
import java.io.FilenameFilter;

public class FilenameCompleter {

    private final String filename;

    private File folderToScan;

    private String prefix;

    private String suffix;

    private String pathToPrepend;

    public FilenameCompleter(String filename) {
        this.filename = filename;
    }

    public FilenameCompleter suffix(String suffix) {
        this.suffix = suffix;

        return this;
    }

    public String[] complete() {
        File[] files = listFiles();
        final int length = files.length;
        String[] result = new String[length];

        for (int i = 0; i < length; ++i) {
            if (files[i].isDirectory()) {
                result[i] = appendSeparator(pathToPrepend + files[i].getName());
            } else {
                result[i] = pathToPrepend + files[i].getName();
            }

            if (isWindows()) {
                result[i] = result[i].replace('\\', '/');
            }
        }

        return result;
    }

    private void init() {
        String cleanFilename = clean(filename);

        if (cleanFilename.endsWith(File.separator)) {
            folderToScan = new File(cleanFilename);
            prefix = "";
            pathToPrepend = appendSeparator(folderToScan.getPath());
        } else {
            File file = new File(cleanFilename);

            folderToScan = file.getParentFile();
            prefix = file.getName();

            if (folderToScan == null) {
                folderToScan = new File(System.getProperty("user.dir"));
                prefix = cleanFilename;
                pathToPrepend = "";
            } else {
                pathToPrepend = appendSeparator(folderToScan.getPath());
            }
        }

        if (suffix == null) {
            suffix = "";
        }
    }

    private File[] listFiles() {
        init();

        File[] files = folderToScan.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return prefix.regionMatches(true, 0, name, 0, prefix.length()) && name.endsWith(suffix);
            }
        });

        if (files == null) {
            return new File[0];
        }

        return files;
    }

    private boolean isWindows() {
        return System.getProperty("os.name")
                     .toLowerCase()
                     .startsWith("windows");
    }

    private String clean(String s) {
        String result = s;

        if (isWindows()) {
            result = result.replace('/', '\\');
        }

        if (result.startsWith("~" + File.separator)) {
            result = new File(System.getProperty("user.home")).getPath() + result.substring(1);
        } else if (result.startsWith("~")) {
            result = appendSeparator(new File(System.getProperty("user.home")).getParentFile()
                                                                              .getPath()) + result.substring(1);
        }

        return result;
    }

    private String appendSeparator(String path) {
        if (path.charAt(path.length() - 1) != File.separatorChar) {
            return path + File.separator;
        }

        return path;
    }
}
