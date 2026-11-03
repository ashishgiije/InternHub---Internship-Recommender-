# Source Audit — InternHub---Internship-Recommender-

Reviewed 4 November 2026 against source commit `c3baa46f4a8956c5761eb885ecc60b9702b45907`. This is a focused code/documentation review and selected local validation, not a penetration test or an exhaustive vulnerability scan. Application files and credentials were not changed. Secret values are intentionally omitted.

## Severity summary

| Critical | High | Medium | Low |
|---:|---:|---:|---:|
| 1 | 2 | 1 | 2 |

Critical: direct resource/identity boundary failure. High: serious exposure or a core reproducibility blocker. Medium: correctness, validation, or deployment issue. Low: maintenance or coverage gap. Static findings describe potential impact; only explicitly listed runtime checks were executed.

## Findings

### 1. Critical — User profile, application, and resume routes accept a path userId without checking an authenticated principal

- **Affected file:** [backend/src/main/java/com/example/recommender/controller/UserController.java](<backend/src/main/java/com/example/recommender/controller/UserController.java>)
- **Evidence:** User profile, application, and resume routes accept a path userId without checking an authenticated principal.
- **Potential impact:** A caller can read or modify another user’s resources by changing the ID.
- **Recommended correction:** Introduce authenticated sessions/tokens and enforce ownership in every user-scoped operation.

### 2. High — Database credentials and a shared administrative passcode are hardcoded; values are deliberately omitted

- **Affected file:** [backend/src/main/resources/application.properties](<backend/src/main/resources/application.properties>)
- **Evidence:** Database credentials and a shared administrative passcode are hardcoded; values are deliberately omitted.
- **Potential impact:** Deployments reusing these settings expose database or administrative access.
- **Recommended correction:** Privately review and replace exposed credentials, externalize configuration, and replace shared-passcode administration.

### 3. High — Database strings are interpolated into innerHTML; similar patterns appear in internship and recommendation pages

- **Affected file:** [backend/src/main/resources/static/js/admin-applications.js](<backend/src/main/resources/static/js/admin-applications.js>)
- **Evidence:** Database strings are interpolated into innerHTML; similar patterns appear in internship and recommendation pages.
- **Potential impact:** Untrusted stored values may become executable markup in the browser.
- **Recommended correction:** Render user data with textContent or validated escaping and add stored-XSS regression tests.

### 4. Medium — The admin passcode is retained in localStorage

- **Affected file:** [backend/src/main/resources/static/js/admin-common.js](<backend/src/main/resources/static/js/admin-common.js>)
- **Evidence:** The admin passcode is retained in localStorage.
- **Potential impact:** Any same-origin script execution can read the administrative credential.
- **Recommended correction:** Use a proper short-lived authenticated session and eliminate shared passcodes.

### 5. Low — Compiled artifacts are committed alongside source

- **Affected file:** [backend/target](<backend/target>)
- **Evidence:** Compiled artifacts are committed alongside source.
- **Potential impact:** Stale output can obscure whether the current source builds.
- **Recommended correction:** Remove generated output from tracking in a separately reviewed maintenance change.

### 6. Low — Test dependencies exist but no test sources were found

- **Affected file:** [backend/pom.xml](<backend/pom.xml>)
- **Evidence:** Test dependencies exist but no test sources were found.
- **Potential impact:** Critical authorization and recommendation behavior lack regression coverage.
- **Recommended correction:** Add controller authorization and repository/service integration tests.

## Validation evidence

Controllers, services, entities, configuration, static UI, and Maven manifest inspected. Maven/MySQL runtime validation has not been completed. No existing Java test source suite was found.

## Review scope and limits

Reviewed tracked application source, entry points, manifests/configuration, routes and data models where present, existing documentation, and available test definitions. Generated dependencies and binary artifacts were inventoried rather than treated as authored application logic. Provider calls, cloud deployment, and external data/model claims remain unverified unless explicitly listed above. No secret was rotated and no migration was run against a user database.

## Priorities

Address Critical and High findings before public deployment. Then add regression tests for the affected boundary or workflow, fix Medium issues, and resolve Low maintenance findings. All application corrections require a separate reviewed coding change.
