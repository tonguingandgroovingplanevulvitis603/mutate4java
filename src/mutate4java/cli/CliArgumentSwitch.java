package mutate4java.cli;

import mutate4java.project.*;
import mutate4java.report.*;

import mutate4java.model.*;

import mutate4java.*;
import mutate4java.analysis.*;
import mutate4java.coverage.*;
import mutate4java.engine.*;
import mutate4java.exec.*;
import mutate4java.manifest.*;
import mutate4java.selection.*;

import mutate4java.selection.*;

import mutate4java.analysis.*;
import mutate4java.coverage.*;
import mutate4java.exec.*;
import mutate4java.manifest.*;

import java.util.function.Consumer;

final class CliArgumentSwitch {

    private CliArgumentSwitch() {
    }

    static int parse(String[] args, int index, CliArgumentParseState state, CliArgumentValidators validators) {
        String arg = args[index];
        Integer parsedIndex = parseModeFlag(arg, index, state);
        if (parsedIndex != null) {
            return parsedIndex;
        }
        parsedIndex = parseSelectionFlag(arg, index, state);
        if (parsedIndex != null) {
            return parsedIndex;
        }
        parsedIndex = parseValueFlag(args, index, state, validators, arg);
        if (parsedIndex != null) {
            return parsedIndex;
        }
        return addFileArgument(arg, index, state);
    }

    private static Integer parseModeFlag(String arg, int index, CliArgumentParseState state) {
        return switch (arg) {
            case "--help" -> set(index, () -> state.help = true);
            case "--verbose" -> set(index, () -> state.verbose = true);
            default -> null;
        };
    }

    private static Integer parseSelectionFlag(String arg, int index, CliArgumentParseState state) {
        return switch (arg) {
            case "--scan" -> set(index, () -> state.scan = true);
            case "--update-manifest" -> set(index, () -> state.updateManifest = true);
            case "--since-last-run" -> set(index, () -> state.sinceLastRun = true);
            case "--mutate-all" -> set(index, () -> state.mutateAll = true);
            default -> null;
        };
    }

    private static Integer parseValueFlag(String[] args,
                                          int index,
                                          CliArgumentParseState state,
                                          CliArgumentValidators validators,
                                          String arg) {
        return switch (arg) {
            case "--lines" -> parseFlagValue(args, index, "--lines",
                    value -> state.lines = validators.parseLines(value), validators);
            case "--timeout-factor" -> parseFlagValue(args, index, "--timeout-factor",
                    value -> state.timeoutFactor = validators.parsePositiveInt(value, "--timeout-factor"), validators);
            case "--mutation-warning" -> parseFlagValue(args, index, "--mutation-warning",
                    value -> state.mutationWarning = validators.parsePositiveInt(value, "--mutation-warning"), validators);
            case "--max-workers" -> parseFlagValue(args, index, "--max-workers",
                    value -> state.maxWorkers = validators.parsePositiveInt(value, "--max-workers"), validators);
            case "--test-command" -> parseFlagValue(args, index, "--test-command",
                    value -> setTestCommand(state, value), validators);
            default -> null;
        };
    }

    private static int parseFlagValue(String[] args, int index, String flag, Consumer<String> consumer,
                                      CliArgumentValidators validators) {
        int valueIndex = index + 1;
        validators.ensureHasValue(args, valueIndex, flag);
        consumer.accept(args[valueIndex]);
        return valueIndex;
    }

    private static int addFileArgument(String arg, int index, CliArgumentParseState state) {
        if (arg.startsWith("--")) {
            throw new IllegalArgumentException("Unknown option: " + arg);
        }
        state.values.add(arg);
        return index;
    }

    private static int set(int index, Runnable action) {
        action.run();
        return index;
    }

    private static void setTestCommand(CliArgumentParseState state, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("--test-command must not be blank");
        }
        state.testCommand = value;
    }
}

/* mutate4java-manifest
version=1
moduleHash=f6bd8ff2bb849d2cd02b79567a4251972bf535252f9939bf8ad333c049c23f92
scope.0.id=Y2xhc3M6Q2xpQXJndW1lbnRTd2l0Y2gjQ2xpQXJndW1lbnRTd2l0Y2g6MjU
scope.0.kind=class
scope.0.startLine=25
scope.0.endLine=112
scope.0.semanticHash=3f45ad69d866df5e9f933470451943f550a8f17c11901050637b670ae215b3cb
scope.1.id=bWV0aG9kOkNsaUFyZ3VtZW50U3dpdGNoI2FkZEZpbGVBcmd1bWVudCgzKTo5Mw
scope.1.kind=method
scope.1.startLine=93
scope.1.endLine=99
scope.1.semanticHash=e79ce1b21e269131df4c9168472b66e9b3defd525be0b9b91fa339d9db4c6c90
scope.2.id=bWV0aG9kOkNsaUFyZ3VtZW50U3dpdGNoI2N0b3IoMCk6Mjc
scope.2.kind=method
scope.2.startLine=27
scope.2.endLine=28
scope.2.semanticHash=e19a94c888b1e756b5303a85f722863e6573d6277effd5693354a7996df33155
scope.3.id=bWV0aG9kOkNsaUFyZ3VtZW50U3dpdGNoI3BhcnNlKDQpOjMw
scope.3.kind=method
scope.3.startLine=30
scope.3.endLine=45
scope.3.semanticHash=c7c16aac86ce987b141609695c59add0bf8641f5b2df7848597b430cb58ef9e1
scope.4.id=bWV0aG9kOkNsaUFyZ3VtZW50U3dpdGNoI3BhcnNlRmxhZ1ZhbHVlKDUpOjg1
scope.4.kind=method
scope.4.startLine=85
scope.4.endLine=91
scope.4.semanticHash=771578bf467e1e7729242c1810a84d9b46fc6fd13b216428c6ad0ad52ece0181
scope.5.id=bWV0aG9kOkNsaUFyZ3VtZW50U3dpdGNoI3BhcnNlTW9kZUZsYWcoMyk6NDc
scope.5.kind=method
scope.5.startLine=47
scope.5.endLine=53
scope.5.semanticHash=34d1a9a33454c159c17bfc42fe75a50b18f34cd01d16e50bb4d194e9b2e4b3a0
scope.6.id=bWV0aG9kOkNsaUFyZ3VtZW50U3dpdGNoI3BhcnNlU2VsZWN0aW9uRmxhZygzKTo1NQ
scope.6.kind=method
scope.6.startLine=55
scope.6.endLine=63
scope.6.semanticHash=47142efa843e6b2087a13e3c3cb4d39ddc0147a6842557ef7d6ec5cb4723b174
scope.7.id=bWV0aG9kOkNsaUFyZ3VtZW50U3dpdGNoI3BhcnNlVmFsdWVGbGFnKDUpOjY1
scope.7.kind=method
scope.7.startLine=65
scope.7.endLine=83
scope.7.semanticHash=4e081f989582eb2c0769a76e01ec1cdc6e2ef88a635fa6804724672573a86d3f
scope.8.id=bWV0aG9kOkNsaUFyZ3VtZW50U3dpdGNoI3NldCgyKToxMDE
scope.8.kind=method
scope.8.startLine=101
scope.8.endLine=104
scope.8.semanticHash=d70bd0cb67b6cd33ec25c3c7e3f6a954965cce5f69e7aa2ceb83c159e151bde4
scope.9.id=bWV0aG9kOkNsaUFyZ3VtZW50U3dpdGNoI3NldFRlc3RDb21tYW5kKDIpOjEwNg
scope.9.kind=method
scope.9.startLine=106
scope.9.endLine=111
scope.9.semanticHash=c1b09b560d2cee69a1d8f2c8239c8659b6f4e50b628e77be881c996f4a2f5cb8
*/
