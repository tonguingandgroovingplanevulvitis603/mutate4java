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

final class IntegerArgumentParser {

    int parsePositiveInt(String text, String flag) {
        try {
            int value = Integer.parseInt(text);
            if (value <= 0) {
                throw new IllegalArgumentException(flag + " must be a positive integer");
            }
            return value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(flag + " must be a positive integer");
        }
    }
}

/* mutate4java-manifest
version=1
moduleHash=ea6802e0bb307dc70634220d8c63931eeb7b4726ed47fda79eea15e5dbd85836
scope.0.id=Y2xhc3M6SW50ZWdlckFyZ3VtZW50UGFyc2VyI0ludGVnZXJBcmd1bWVudFBhcnNlcjoyMw
scope.0.kind=class
scope.0.startLine=23
scope.0.endLine=36
scope.0.semanticHash=b8facd130cf7dbc6eea7a8465c71b79c923b353b85ee7b46afc5752a339509f1
scope.1.id=bWV0aG9kOkludGVnZXJBcmd1bWVudFBhcnNlciNjdG9yKDApOjIz
scope.1.kind=method
scope.1.startLine=1
scope.1.endLine=36
scope.1.semanticHash=1bd9c856b29fb0341f28dda680e6e8c577ffecc957f84b58a0e62ace075d7e66
scope.2.id=bWV0aG9kOkludGVnZXJBcmd1bWVudFBhcnNlciNwYXJzZVBvc2l0aXZlSW50KDIpOjI1
scope.2.kind=method
scope.2.startLine=25
scope.2.endLine=35
scope.2.semanticHash=5a92c7a4851c063b323a5941dc443726be331d24c3b334027867fd6150d11663
*/
