package org.sweet.revelation.revelation.job.doc;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.ProcessorComponent;
import org.sweet.revelation.revelation.core.command.CommandMetadata;
import org.sweet.revelation.revelation.core.command.ParameterMetadata;
import org.sweet.revelation.revelation.core.concurrent.Task;
import org.sweet.revelation.revelation.core.concurrent.TaskResultProcessor;
import org.sweet.revelation.revelation.core.convert.StringConverter;
import org.sweet.revelation.revelation.core.convert.StringConverterRegistry;
import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.log.Work;
import org.sweet.revelation.revelation.core.main.RevelationMetadata;
import org.sweet.revelation.revelation.core.processor.ProcessorMessageBuilder;
import org.sweet.revelation.revelation.core.processor.ProcessorMetadata;
import org.sweet.revelation.revelation.core.processor.ProcessorStatus;
import org.sweet.revelation.revelation.core.processor.impl.ParallelProcessor;
import org.sweet.revelation.revelation.core.text.PrettyIntegerFormatter;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.ArrayList;
import java.util.Collection;

@ProcessorComponent(DocumentCommand.class)
@Description("Create commands documentation")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Document extends ParallelProcessor<DocumentCommand, ProcessorMetadata, Element> {

    @Autowired
    private RevelationMetadata revelationMetadata;

    @Autowired
    private StringConverterRegistry stringConverterRegistry;

    private org.w3c.dom.Document document;

    @Override
    protected Collection<ProcessorMetadata> preProcess(Activity activity, DocumentCommand command) throws Exception {
        this.document = DocumentBuilderFactory.newInstance()
                                              .newDocumentBuilder()
                                              .newDocument();

        return super.preProcess(activity, command);
    }

    @Override
    protected Collection<ProcessorMetadata> createTaskParams(Work work, DocumentCommand command) throws Exception {
        Collection<ProcessorMetadata> result = new ArrayList<ProcessorMetadata>();

        for (ProcessorMetadata processorMetadata : revelationMetadata.iterate()) {
            result.add(processorMetadata);
        }

        return result;
    }

    @Override
    protected Task<ProcessorMetadata, Element> newTask(DocumentCommand command, final int index) throws Exception {
        return new Task<ProcessorMetadata, Element>() {

            public Element run(Work work, ProcessorMetadata processorMetadata) throws Exception {
                ElementBuilder builder = ElementBuilder.build(document.createElement("command"))
                                                       .attribute("name", processorMetadata.getName())
                                                       .attribute("type", processorMetadata.getProcessorType()
                                                                                           .getName());

                ElementBuilder.build(document.createElement("description"))
                              .textContent(processorMetadata.getDescription())
                              .appendTo(builder);

                CommandMetadata commandMetadata = processorMetadata.createCommandMetadata(stringConverterRegistry);
                Object command = commandMetadata.create();

                for (ParameterMetadata parameterMetadata : commandMetadata) {
                    builder.appendChild(run(command, parameterMetadata));
                }

                return builder.build();
            }

            private Element run(Object command, ParameterMetadata parameterMetadata) throws Exception {
                ElementBuilder builder = ElementBuilder.build(document.createElement("param"))
                                                       .attribute("name", parameterMetadata.getName())
                                                       .attribute("mandatory", parameterMetadata.isMandatory());
                StringConverter<?> stringConverter = stringConverterRegistry.getConverter(parameterMetadata.getType());

                ElementBuilder.build(document.createElement("usage"))
                              .textContent(stringConverter.getUsage())
                              .appendTo(builder);

                for (String usageValue : stringConverter.getUsageValues()) {
                    ElementBuilder.build(document.createElement("value"))
                                  .textContent(usageValue)
                                  .appendTo(builder);
                }

                Object value = parameterMetadata.getValue(command);

                if (value != null) {
                    ElementBuilder.build(document.createElement("defaultValue"))
                                  .textContent(value.toString())
                                  .appendTo(builder);
                }

                ElementBuilder.build(document.createElement("description"))
                              .textContent(parameterMetadata.getDescription())
                              .appendTo(builder);

                return builder.build();
            }
        };
    }

    @Override
    protected TaskResultProcessor<DocumentCommand, Element> createTaskResultProcessor(DocumentCommand command, final int taskCount) throws Exception {
        return new TaskResultProcessor<DocumentCommand, Element>(command, taskCount) {

            private Element root;

            @Override
            protected void start(Work work) throws Exception {
                root = ElementBuilder.build(document.createElement("commands"))
                                     .attribute("creationDate", ISODateTimeFormat.dateTime()
                                                                                 .print(DateTime.now()))
                                     .attribute("title", command.getTitle())
                                     .attribute("version", command.getVersion())
                                     .attribute("buildNumber", command.getBuildNumber())
                                     .appendTo(document);
            }

            @Override
            protected void process(Element result) throws Exception {
                root.appendChild(result);
            }

            @Override
            protected void done(Work work) throws Exception {
                Transformer transformer = TransformerFactory.newInstance()
                                                            .newTransformer();

                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                transformer.transform(new DOMSource(document), new StreamResult(command.getOutputFileCommand()
                                                                                       .getOutputFile()));
            }

            @Override
            protected ProcessorMessageBuilder newProcessorMessageBuilder() {
                return new ProcessorMessageBuilder(taskCount) {

                    @Override
                    public String build(ProcessorStatus status) {
                        if (status == ProcessorStatus.SUCCESS) {
                            return new PrettyIntegerFormatter().format(taskCount) + " command(s) documented in " + command.getOutputFileCommand()
                                                                                                                          .getOutputFile()
                                                                                                                          .getAbsolutePath();
                        } else {
                            return super.build(status);
                        }
                    }
                };
            }
        };
    }
}
