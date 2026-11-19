## Problem

In a **rectangular maze** with free spaces and walls,
find the **number of legal paths** from the northwest to southeast corner
when **moving only east and south**.

## My Solutions

* `oneWayMaze1()` – an obvious slow recursive solution on 6 lines.
* `oneWayMaze2()` – a fast solution taking little more than 6 lines.
* `oneWayMaze3()` – like version 2, but using a domain-specific language to improve readability.
* `oneWayMaze1996()` – a solution imitating the technological status of year 1996 when I solved this task at a republic
  round of the Mathematical Olympiad category P (Programming) in Pascal language. I do use the convenient Kotlin syntax
  etc., but I refrain from using two crucial utilities: `MutableMap` and `BigInteger`.     