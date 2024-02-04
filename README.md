# ARM Assembler in Java

## Overview

This project is an implementation of an ARM assembler in Java. It allows you to write ARM assembly code and convert it into machine code that can be executed on ARM processors.

## Features

- **Syntax Support:** Provides support for a subset of ARM assembly language syntax.
- **Assembler:** Converts ARM assembly code into machine code.



## Getting Started

### Prerequisites

- Java 8 or later


### Installation

Clone the repository to your local machine:

```bash
git clone git@github.com:mouhamadalmounayar/arm-assembler.git
```

### Using the assembler
- Create a `.s` or `.txt` file in the root of the directory and write your assembly code.
- Run the `Main.java` file.
- You will be prompted to enter the file name.
- The assembler will then generate a `.bin` file if the code is syntactically correct. It will fault otherwise.

### Notes

When declaring a label, for example `start :`, make sure to leave a space between `start` and `:` .

