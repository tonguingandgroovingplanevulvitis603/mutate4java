package mutate4java;

import mutate4java.analysis.*;
import mutate4java.cli.*;
import mutate4java.coverage.*;
import mutate4java.engine.*;
import mutate4java.exec.*;
import mutate4java.manifest.*;
import mutate4java.model.*;
import mutate4java.project.*;
import mutate4java.report.*;
import mutate4java.selection.*;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoverageRunnerTest {

    @Test
    void defaultGenerateCoverageRefreshesCoverage() throws Exception {
        Path projectRoot = TestProjectFactory.createProject("coverage-runner-default");

        CoverageRun run = new CoverageRunner(new ProcessCommandExecutor()).generateCoverage(projectRoot);

        assertEquals(0, run.baseline().exitCode());
        assertFalse(run.reused());
        assertTrue(run.reportAvailable());
    }

    @Test
    void generatesCoverageByRunningMavenAndParsingJacocoXml() throws Exception {
        Path projectRoot = TestProjectFactory.createProject("coverage-runner-unit");

        CoverageRun run = new CoverageRunner(new ProcessCommandExecutor()).generateCoverage(projectRoot, false);

        assertEquals(0, run.baseline().exitCode());
        assertFalse(run.reused());
        assertTrue(run.reportAvailable());
        assertTrue(run.report().covers("mutate4java/Sample.java", 5));
    }

    @Test
    void reusesExistingJacocoXmlWhenRequested() throws Exception {
        Path projectRoot = TestProjectFactory.createProject("coverage-runner-reuse-unit");
        CoverageRunner runner = new CoverageRunner(new ProcessCommandExecutor());
        runner.generateCoverage(projectRoot, false);

        CoverageRun run = runner.generateCoverage(projectRoot, true);

        assertEquals(null, run.baseline());
        assertTrue(run.reused());
        assertTrue(run.reportAvailable());
        assertTrue(run.report().covers("mutate4java/Sample.java", 5));
    }

    @Test
    void reportsMissingCoverageWhenReuseRequestedWithoutJacocoXml() throws Exception {
        Path projectRoot = Files.createTempDirectory("coverage-runner-missing-unit");
        Files.createDirectories(projectRoot);

        CoverageRun run = new CoverageRunner(new ProcessCommandExecutor()).generateCoverage(projectRoot, true);

        assertEquals(null, run.baseline());
        assertTrue(run.reused());
        assertFalse(run.reportAvailable());
        assertFalse(run.report().covers("mutate4java/Sample.java", 5));
    }
}
