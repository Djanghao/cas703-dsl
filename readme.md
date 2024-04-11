# Potluck Party Planner DSL

## Overview

The Potluck Party Planner DSL is a Domain-Specific Language designed for the planning and organization of party events. Built upon model-driven engineering principles, this repository integrates features such as Emf/Ecore models, Xtext grammar definition, and validation rules through EVL (Epsilon Validation Language), facilitating a code generation process using the Epsilon Generation Language (EGL).

## System Requirements

- **Eclipse IDE:** 2024-03 (4.31.0) or later
- **Epsilon:** 2.4
- **Eclipse Xtext:** 2.34.0
- **Gradle:**  8.7
- **Java:** 17

## Environment Setup

### Eclipse Installation

1. Download and install Eclipse IDE version 2024-03 (4.31.0) from Eclipse Downloads.
2. Ensure Eclipse is configured with the correct JDK version in the installed JREs settings.

### Plugin Configuration

Install the following plugins via the Eclipse Marketplace or 'Help > Install New Software' option within Eclipse:

- **Epsilon 2.4:** For model validation and transformation.
- **Eclipse Xtext 2.34.0:** For DSL grammar definition and integration.

Restart Eclipse to apply the new plugin installations.

## Project Configuration

Clone the repository to your local development environment using:

```
git clone https://github.com/Djanghao/cas703-dsl.git
cd cas703-dsl
```

## Execution and Code Generation

To activate the DSL processing and initiate code generation, execute the following command within the project's root directory:

```
gradle run
```

This command uses Gradle to compile and run the DSL project, generating code (the result will be index.html) based on the defined DSL models and specifications. See example at https://djanghao.github.io/cas703-dsl/.
