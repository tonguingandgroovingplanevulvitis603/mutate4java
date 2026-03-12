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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

final class CliArgumentParseState {

    boolean help;
    boolean verbose;
    Set<Integer> lines = Set.of();
    boolean scan;
    boolean updateManifest;
    boolean sinceLastRun;
    boolean mutateAll;
    int timeoutFactor = CliArgumentsParserDefaults.DEFAULT_TIMEOUT_FACTOR;
    int mutationWarning = CliArgumentsParserDefaults.DEFAULT_MUTATION_WARNING;
    int maxWorkers = CliArgumentsParserDefaults.DEFAULT_MAX_WORKERS;
    String testCommand;
    final List<String> values = new ArrayList<>();

    CliArguments helpArguments() {
        return new CliArguments(CliMode.HELP, List.of(), Set.of(), false, false, false, false,
                timeoutFactor, mutationWarning, maxWorkers, null, verbose);
    }

    CliArguments toCliArguments() {
        return new CliArguments(
                CliMode.EXPLICIT_FILES,
                List.copyOf(values),
                lines,
                scan,
                updateManifest,
                sinceLastRun,
                mutateAll,
                timeoutFactor,
                mutationWarning,
                maxWorkers,
                testCommand,
                verbose
        );
    }
}

/* mutate4java-manifest
version=1
moduleHash=45bdb32d19aab81f0c72b574eba82b313da49936f065bef88bb4640168163abf
scope.0.id=Y2xhc3M6Q2xpQXJndW1lbnRQYXJzZVN0YXRlI0NsaUFyZ3VtZW50UGFyc2VTdGF0ZToyNw
scope.0.kind=class
scope.0.startLine=27
scope.0.endLine=63
scope.0.semanticHash=3e009fc5b2be3080650a045379ec079171431705fd8e166e3fec1952e202f0e4
scope.1.id=ZmllbGQ6Q2xpQXJndW1lbnRQYXJzZVN0YXRlI2hlbHA6Mjk
scope.1.kind=field
scope.1.startLine=29
scope.1.endLine=29
scope.1.semanticHash=35690be3e31a7c92ccb81daa48fc0f4ac93fa04005d069bcc023686b0d10f7fd
scope.2.id=ZmllbGQ6Q2xpQXJndW1lbnRQYXJzZVN0YXRlI2xpbmVzOjMx
scope.2.kind=field
scope.2.startLine=31
scope.2.endLine=31
scope.2.semanticHash=51bcc7b5a5573eac9b86a4d0bb0af46236536f2e49ab3f6f9afc803848529193
scope.3.id=ZmllbGQ6Q2xpQXJndW1lbnRQYXJzZVN0YXRlI21heFdvcmtlcnM6Mzg
scope.3.kind=field
scope.3.startLine=38
scope.3.endLine=38
scope.3.semanticHash=0f5577370cea62f6ae143cc63050873157c1d682394b264e8d85282f4477bc2c
scope.4.id=ZmllbGQ6Q2xpQXJndW1lbnRQYXJzZVN0YXRlI211dGF0ZUFsbDozNQ
scope.4.kind=field
scope.4.startLine=35
scope.4.endLine=35
scope.4.semanticHash=301f08316018fb615f6f3d7413ea5d1472ef9f164f50f1d8db0720da4607d2db
scope.5.id=ZmllbGQ6Q2xpQXJndW1lbnRQYXJzZVN0YXRlI211dGF0aW9uV2FybmluZzozNw
scope.5.kind=field
scope.5.startLine=37
scope.5.endLine=37
scope.5.semanticHash=c05340e1234b2aa2077b929d78d71d59a4271367f1c1fc9d3bf1744ef9d1609b
scope.6.id=ZmllbGQ6Q2xpQXJndW1lbnRQYXJzZVN0YXRlI3NjYW46MzI
scope.6.kind=field
scope.6.startLine=32
scope.6.endLine=32
scope.6.semanticHash=996f1e8e0bbfab797a7897f07d2236945659723362fb03e6ba8e702f11c7526e
scope.7.id=ZmllbGQ6Q2xpQXJndW1lbnRQYXJzZVN0YXRlI3NpbmNlTGFzdFJ1bjozNA
scope.7.kind=field
scope.7.startLine=34
scope.7.endLine=34
scope.7.semanticHash=e5d2024b253e415abe50124d8b3f92018df8bbb4efdafad4b40881b30b44b5b2
scope.8.id=ZmllbGQ6Q2xpQXJndW1lbnRQYXJzZVN0YXRlI3Rlc3RDb21tYW5kOjM5
scope.8.kind=field
scope.8.startLine=39
scope.8.endLine=39
scope.8.semanticHash=3d3aa181c72cfe9c957932f7666554a01446f8dd58c192f43605e78f6491b7a5
scope.9.id=ZmllbGQ6Q2xpQXJndW1lbnRQYXJzZVN0YXRlI3RpbWVvdXRGYWN0b3I6MzY
scope.9.kind=field
scope.9.startLine=36
scope.9.endLine=36
scope.9.semanticHash=f1b2d467846a92ed76456958bbc911723128834d07fab35730d228883ed4ccaf
scope.10.id=ZmllbGQ6Q2xpQXJndW1lbnRQYXJzZVN0YXRlI3VwZGF0ZU1hbmlmZXN0OjMz
scope.10.kind=field
scope.10.startLine=33
scope.10.endLine=33
scope.10.semanticHash=1f702c8ab025d5c71925c52d1e2bc9f67068fde0613d7546e6a2a38d457cf6a1
scope.11.id=ZmllbGQ6Q2xpQXJndW1lbnRQYXJzZVN0YXRlI3ZhbHVlczo0MA
scope.11.kind=field
scope.11.startLine=40
scope.11.endLine=40
scope.11.semanticHash=b5d1c7a4e0b651e4d120fa7ed33f622ebb8894bbb99e195d970909759a3e000b
scope.12.id=ZmllbGQ6Q2xpQXJndW1lbnRQYXJzZVN0YXRlI3ZlcmJvc2U6MzA
scope.12.kind=field
scope.12.startLine=30
scope.12.endLine=30
scope.12.semanticHash=166515b0f52e3ce745db1f16f2bf6848bc4584b19174b2f481ab37f53505256c
scope.13.id=bWV0aG9kOkNsaUFyZ3VtZW50UGFyc2VTdGF0ZSNjdG9yKDApOjI3
scope.13.kind=method
scope.13.startLine=1
scope.13.endLine=63
scope.13.semanticHash=218c62956b060f55f74e413a08a13e8c9e8aff1a7534fa1a617c5925f10967df
scope.14.id=bWV0aG9kOkNsaUFyZ3VtZW50UGFyc2VTdGF0ZSNoZWxwQXJndW1lbnRzKDApOjQy
scope.14.kind=method
scope.14.startLine=42
scope.14.endLine=45
scope.14.semanticHash=7e42b1c3e79815f54e300b8b91ccd60f75d411f3f6f01b525ed2564f1c6973ce
scope.15.id=bWV0aG9kOkNsaUFyZ3VtZW50UGFyc2VTdGF0ZSN0b0NsaUFyZ3VtZW50cygwKTo0Nw
scope.15.kind=method
scope.15.startLine=47
scope.15.endLine=62
scope.15.semanticHash=1cf16d33f2270d91e07e9e9d5921aca9ba28880dfe4763c2f8607e0894298fda
*/
