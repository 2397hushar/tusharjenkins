package utilities;

import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.model.Label;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class AllureCucumberAdapter implements ConcurrentEventListener {

    private final ThreadLocal<Deque<String>> stepUuids = ThreadLocal.withInitial(ArrayDeque::new);
    private final ThreadLocal<String> scenarioUuid = new ThreadLocal<>();
    private final AllureLifecycle lifecycle = Allure.getLifecycle();

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
        publisher.registerHandlerFor(TestStepStarted.class, this::handleTestStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
        publisher.registerHandlerFor(WriteEvent.class, this::handleWrite);
        publisher.registerHandlerFor(EmbedEvent.class, this::handleEmbed);
        
        System.out.println("✅ AllureCucumberAdapter initialized");
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        String uuid = UUID.randomUUID().toString();
        scenarioUuid.set(uuid);

        TestCase testCase = event.getTestCase();
        
        // Extract the actual scenario name (not the feature name)
        String scenarioName = testCase.getName();
        
        // Extract feature name from URI
        String featureName = extractFeatureName(testCase.getUri().toString());
        
        // Create a proper test name
        String testName = scenarioName;
        String fullName = featureName + ": " + scenarioName;

        System.out.println("📊 Creating Allure test: " + testName);

        TestResult result = new TestResult()
                .setUuid(uuid)
                .setName(testName)  // This should be the scenario name
                .setFullName(fullName)
                .setStart(event.getInstant().toEpochMilli());

        // Add labels
        result.getLabels().add(createLabel("feature", featureName));
        result.getLabels().add(createLabel("story", scenarioName));
        result.getLabels().add(createLabel("test", scenarioName));
        result.getLabels().add(createLabel("language", "java"));
        result.getLabels().add(createLabel("framework", "cucumber"));

        // Add tags as labels
        for (String tag : testCase.getTags()) {
            result.getLabels().add(createLabel("tag", tag.replace("@", "")));
        }

        lifecycle.scheduleTestCase(result);
        lifecycle.startTestCase(uuid);
    }

    private void handleTestCaseFinished(TestCaseFinished event) {
        String uuid = scenarioUuid.get();
        if (uuid != null) {

            Status status = mapStatus(event.getResult().getStatus());

            lifecycle.updateTestCase(uuid, testResult ->
                    testResult.setStatus(status)
                            .setStop(event.getInstant().toEpochMilli())
            );

            if (event.getResult().getError() != null) {
                StatusDetails statusDetails = new StatusDetails()
                        .setMessage(event.getResult().getError().getMessage())
                        .setTrace(getStackTrace(event.getResult().getError()));

                lifecycle.updateTestCase(uuid, testResult ->
                        testResult.setStatusDetails(statusDetails)
                );
            }

            lifecycle.stopTestCase(uuid);
            lifecycle.writeTestCase(uuid);
            scenarioUuid.remove();
            
            System.out.println("📊 Allure test finished: " + status);
        }
    }

    private void handleTestStepStarted(TestStepStarted event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {

            PickleStepTestStep testStep = (PickleStepTestStep) event.getTestStep();
            
            // Format step text properly
            String stepText = testStep.getStep().getKeyword() + " " + testStep.getStep().getText();

            String uuid = UUID.randomUUID().toString();
            stepUuids.get().push(uuid);

            StepResult stepResult = new StepResult()
                    .setName(stepText)
                    .setStart(event.getInstant().toEpochMilli());

            lifecycle.startStep(uuid, stepResult);

            // Handle step arguments
            if (testStep.getStep().getArgument() != null) {
                StepArgument argument = testStep.getStep().getArgument();
                
                if (argument instanceof DataTableArgument) {
                    DataTableArgument dataTable = (DataTableArgument) argument;
                    addDataTableAttachment(dataTable);
                } else if (argument instanceof DocStringArgument) {
                    DocStringArgument docString = (DocStringArgument) argument;
                    addDocStringAttachment(docString);
                }
            }
        }
    }

    private void handleTestStepFinished(TestStepFinished event) {
        if (event.getTestStep() instanceof PickleStepTestStep) {

            String uuid = stepUuids.get().poll();

            if (uuid != null) {

                Status status = mapStatus(event.getResult().getStatus());

                lifecycle.updateStep(uuid, step ->
                        step.setStatus(status)
                                .setStop(event.getInstant().toEpochMilli())
                );

                if (event.getResult().getError() != null) {
                    lifecycle.updateStep(uuid, step ->
                            step.setStatusDetails(new StatusDetails()
                                    .setMessage(event.getResult().getError().getMessage())
                                    .setTrace(getStackTrace(event.getResult().getError())))
                    );
                }

                lifecycle.stopStep(uuid);
            }
        }
    }

    private void handleWrite(WriteEvent event) {
        Allure.addAttachment("Output", "text/plain",
                new ByteArrayInputStream(event.getText().getBytes()), "txt");
    }

    private void handleEmbed(EmbedEvent event) {
        String mimeType = event.getMediaType();
        String extension = getExtensionFromMimeType(mimeType);
        String name = event.getName() != null ? event.getName() : "Screenshot";

        Allure.addAttachment(name, mimeType,
                new ByteArrayInputStream(event.getData()), extension);
        
        System.out.println("📸 Screenshot attached: " + name);
    }

    private void addDataTableAttachment(DataTableArgument dataTable) {
        StringBuilder tableContent = new StringBuilder();
        tableContent.append("Data Table:\n");
        for (int i = 0; i < dataTable.cells().size(); i++) {
            tableContent.append("  Row ").append(i + 1).append(": ")
                       .append(String.join(" | ", dataTable.cells().get(i)))
                       .append("\n");
        }
        Allure.addAttachment("Data Table", "text/plain",
                new ByteArrayInputStream(tableContent.toString().getBytes()), "txt");
    }

    private void addDocStringAttachment(DocStringArgument docString) {
        Allure.addAttachment("Doc String", "text/plain",
                new ByteArrayInputStream(docString.getContent().getBytes()), "txt");
    }

    private String extractFeatureName(String uri) {
        if (uri.contains("/")) {
            String fileName = uri.substring(uri.lastIndexOf("/") + 1);
            return fileName.replace(".feature", "");
        }
        return "Unknown Feature";
    }

    private String getExtensionFromMimeType(String mimeType) {
        switch (mimeType) {
            case "image/png":
                return "png";
            case "image/jpeg":
            case "image/jpg":
                return "jpg";
            case "text/plain":
                return "txt";
            case "text/html":
                return "html";
            case "application/json":
                return "json";
            case "application/xml":
                return "xml";
            default:
                return "txt";
        }
    }

    private Status mapStatus(io.cucumber.plugin.event.Status status) {
        switch (status) {
            case PASSED:
                return Status.PASSED;
            case FAILED:
                return Status.FAILED;
            case SKIPPED:
            case PENDING:
                return Status.SKIPPED;
            case AMBIGUOUS:
            case UNDEFINED:
                return Status.BROKEN;
            default:
                return Status.BROKEN;
        }
    }

    private String getStackTrace(Throwable error) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        error.printStackTrace(pw);
        return sw.toString();
    }

    private Label createLabel(String name, String value) {
        Label label = new Label();
        label.setName(name);
        label.setValue(value);
        return label;
    }
}