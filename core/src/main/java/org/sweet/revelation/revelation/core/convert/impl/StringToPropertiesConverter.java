package org.sweet.revelation.revelation.core.convert.impl;

import org.sweet.revelation.revelation.core.convert.ConvertException;

import java.io.*;
import java.util.Properties;

public class StringToPropertiesConverter extends SafeStringConverter<Properties> {

    public String getUsage() {
        return "PROPERTIES FILE";
    }

    @Override
    public String[] complete(String s) {
        return new FilenameCompleter(s).suffix(".properties")
                                       .complete();
    }

    @Override
    protected Properties doConvert(String s) {
        InputStream is;
        File propertiesFile = new File(s);

        if (propertiesFile.exists()) {
            try {
                is = new BufferedInputStream(new FileInputStream(propertiesFile));

                return load(propertiesFile.getAbsolutePath(), is);
            } catch (FileNotFoundException e) {
                throw new ConvertException(String.format("Failed to find properties file <%s>", propertiesFile.getAbsolutePath()), e);
            }
        } else {
            is = ClassLoader.getSystemResourceAsStream(s);

            if (is == null) {
                throw new ConvertException(String.format("Failed to read properties from <%s> : neither a file nor a classpath resource", s));
            }

            return load(s, is);
        }
    }

    private Properties load(String source, InputStream is) {
        try {
            Properties result = new Properties();

            result.load(is);

            return result;
        } catch (IOException e) {
            throw new ConvertException(String.format("Failed to read properties from <%s>", source), e);
        } finally {
            try {
                is.close();
            } catch (IOException ioe) {
            }
        }
    }
}
