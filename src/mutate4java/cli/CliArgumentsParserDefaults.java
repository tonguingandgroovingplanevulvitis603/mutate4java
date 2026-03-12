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

final class CliArgumentsParserDefaults {

    static final int DEFAULT_TIMEOUT_FACTOR = 10;
    static final int DEFAULT_MUTATION_WARNING = 50;
    static final int DEFAULT_MAX_WORKERS = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);

    private CliArgumentsParserDefaults() {
    }
}

/* mutate4java-manifest
version=1
moduleHash=d9135fd1e9cb6ea543bd3f9343f8cc7c66882a7ba99ba85c4bd20fb87ffd1bab
scope.0.id=Y2xhc3M6Q2xpQXJndW1lbnRzUGFyc2VyRGVmYXVsdHMjQ2xpQXJndW1lbnRzUGFyc2VyRGVmYXVsdHM6MjM
scope.0.kind=class
scope.0.startLine=23
scope.0.endLine=31
scope.0.semanticHash=63c9b5708dc61e797cd9dbb6588020dae5b44be6fbea1ce3f240dfecdbeeee3a
scope.1.id=ZmllbGQ6Q2xpQXJndW1lbnRzUGFyc2VyRGVmYXVsdHMjREVGQVVMVF9NQVhfV09SS0VSUzoyNw
scope.1.kind=field
scope.1.startLine=27
scope.1.endLine=27
scope.1.semanticHash=3d9a0d08aa2ef24c983a8f914001004fa7463ba60f677a7dbe501b7f06ea60f9
scope.2.id=ZmllbGQ6Q2xpQXJndW1lbnRzUGFyc2VyRGVmYXVsdHMjREVGQVVMVF9NVVRBVElPTl9XQVJOSU5HOjI2
scope.2.kind=field
scope.2.startLine=26
scope.2.endLine=26
scope.2.semanticHash=5e190b9155117ddb4704599dabfed73c0ae01cc59b88434fb0047ba8af0482ed
scope.3.id=ZmllbGQ6Q2xpQXJndW1lbnRzUGFyc2VyRGVmYXVsdHMjREVGQVVMVF9USU1FT1VUX0ZBQ1RPUjoyNQ
scope.3.kind=field
scope.3.startLine=25
scope.3.endLine=25
scope.3.semanticHash=41fb2d93031685afd425ad20bad8ee44d0cf66968eabe827ceaa1f5c231804e2
scope.4.id=bWV0aG9kOkNsaUFyZ3VtZW50c1BhcnNlckRlZmF1bHRzI2N0b3IoMCk6Mjk
scope.4.kind=method
scope.4.startLine=29
scope.4.endLine=30
scope.4.semanticHash=200a3962673b78ab2d5b63a7ae3f81e23a4f71e1e427171acb3cb0c92b031280
*/
