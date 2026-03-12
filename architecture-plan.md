# mutate4java Package Reorganization Plan

## Goal
Break the flat `mutate4java` namespace into cohesive components without forcing a full rewrite at once.

## Steps
1. Move the infrastructure-heavy groups first:
   - `mutate4java.analysis`
   - `mutate4java.coverage`
   - `mutate4java.exec`
   - `mutate4java.manifest`

2. Move mutation-selection behavior into its own package:
   - `mutate4java.selection`

3. Move entrypoint and orchestration code last:
   - `mutate4java.cli`
   - `mutate4java.engine`

4. Revisit shared records and value types only after package pressure is clear:
   - decide whether a `mutate4java.model` package is justified

## Notes
- Do not move code just because of line count.
- Use mutation-site density and cohesion to justify package boundaries.
- Verify `mvn -pl tools/mutate4java test` after each completed step.
