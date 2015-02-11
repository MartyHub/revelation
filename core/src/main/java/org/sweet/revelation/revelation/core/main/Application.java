package org.sweet.revelation.revelation.core.main;

import org.fusesource.jansi.AnsiConsole;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.sweet.revelation.revelation.core.convert.ConvertException;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistry;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistryBuilder;
import org.sweet.revelation.revelation.core.convert.impl.StringToPropertiesConverter;
import org.sweet.revelation.revelation.core.event.CommandNotifier;
import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.log.impl.CompositeActivity;
import org.sweet.revelation.revelation.core.processor.ProcessorReport;

import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

public class Application {

    public static void main(String[] args) {
        AnsiConsole.systemInstall();

        try {
            new Application().run(new ArrayIterator<String>(args));
        } finally {
            AnsiConsole.systemUninstall();
        }
    }

    private static final String PACKAGES_TO_SCAN_PROPERTY = "revelation.packages_to_scan";

    private static final String SYSTEM_EXIT_DISABLED_PROPERTY = "revelation.system_exit_disabled";

    private final Activity activity;

    private Properties properties;

    private String[] packagesToScan;

    private boolean systemExitDisabled;

    private AnnotationConfigApplicationContext context;

    public Application() {
        this.activity = CompositeActivity.main();
    }

    public void run(Iterator<String> args) {
        activity.info(String.format("Version : %s", getVersion()));

        if (!args.hasNext()) {
            new ProcessorFinalizer(false, activity).endAndExit(ProcessorReport.failure("configuration file parameter is mandatory"), getUsage());
        }

        readProperties(args.next());
        createContext();
        initStringConverterRegistry();
        addShutdownHook();

        new ProcessorRunner(context).run(new ProcessorFinalizer(systemExitDisabled, activity), args);
    }

    private void readProperties(String resourceName) {
        try {
            properties = new StringToPropertiesConverter().convert(resourceName);

            this.packagesToScan = getPackagesToScan(properties);
            this.systemExitDisabled = Boolean.parseBoolean(properties.getProperty(SYSTEM_EXIT_DISABLED_PROPERTY, "false"));
        } catch (ConvertException e) {
            new ProcessorFinalizer(false, activity).endAndExit(ProcessorReport.failure("invalid configuration file <%s>", e, resourceName), getUsage());
        }

        if (packagesToScan.length == 0) {
            new ProcessorFinalizer(false, activity).endAndExit(ProcessorReport.failure("invalid property <%s> in configuration file <%s>", null,
                    PACKAGES_TO_SCAN_PROPERTY, resourceName));
        }
    }

    private String[] getPackagesToScan(Properties properties) {
        String packagesToScan = properties.getProperty(PACKAGES_TO_SCAN_PROPERTY);

        if (packagesToScan == null) {
            return new String[0];
        } else {
            StringTokenizer st = new StringTokenizer(packagesToScan, ",");
            String[] result = new String[st.countTokens()];

            for (int index = 0; st.hasMoreTokens(); ++index) {
                result[index] = st.nextToken();
            }

            return result;
        }
    }

    private void createContext() {
        try {
            this.context = doCreateContext();
        } catch (Exception e) {
            new ProcessorFinalizer(false, activity).endAndExit(ProcessorReport.failure("failed to create application context", e));
        }
    }

    private AnnotationConfigApplicationContext doCreateContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.registerShutdownHook();
        context.addBeanFactoryPostProcessor(createPropertyConfigurer());
        context.register(RevelationMetadata.class);
        context.register(CommandNotifier.class);
        context.scan(packagesToScan);
        context.refresh();
        context.getBeanFactory()
               .registerSingleton(activity.getClass()
                                          .getName(), activity);

        return context;
    }

    private PropertyPlaceholderConfigurer createPropertyConfigurer() {
        PropertyPlaceholderConfigurer result = new PropertyPlaceholderConfigurer();

        result.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        result.setProperties(properties);

        return result;
    }

    private void initStringConverterRegistry() {
        try {
            if (createStringConverterRegistry()) {
                createDefaultStringConverterRegistry();
            }
        } catch (Exception e) {
            new ProcessorFinalizer(false, activity).endAndExit(ProcessorReport.failure("failed to create string converter registry", e));
        }
    }

    private boolean createStringConverterRegistry() {
        try {
            context.getBean(StringConverterRegistry.class);

            return false;
        } catch (NoSuchBeanDefinitionException e) {
            return true;
        }
    }

    private void createDefaultStringConverterRegistry() {
        StringConverterRegistry result = new StringConverterRegistryBuilder().build();

        context.getBeanFactory()
               .registerSingleton(StringConverterRegistry.class.getSimpleName(), result);
    }

    private void addShutdownHook() {
        Runtime.getRuntime()
               .addShutdownHook(new ShutdownHook(context.getBean(CommandNotifier.class)));
    }

    private String getVersion() {
        Properties properties = new StringToPropertiesConverter().convert("revelation-version.properties");

        return properties.getProperty("revelation.version", "unknown");
    }

    private String getUsage() {
        return "Usage : run FILE COMMAND [PARAMETER]";
    }
}
