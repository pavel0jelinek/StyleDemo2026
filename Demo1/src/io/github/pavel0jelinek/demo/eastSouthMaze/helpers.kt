package io.github.pavel0jelinek.demo.eastSouthMaze

fun obstaclesOf(printedMaze: String): Array<Array<Boolean>> = printedMaze.lines().filterNot { it.isBlank() }
    .map { line ->
        line.trim().map { cell: Char ->
            when (cell) {
                '.' -> false
                'X' -> true
                else -> throw IllegalArgumentException("Unexpected char '$cell'")
            }
        }.toTypedArray()
    }.toTypedArray()

fun mazeOf(printedMaze: String) = Maze(obstaclesOf(printedMaze))

fun solveAndPrint(printedMaze: String) {
    println(printedMaze)
    val obstacles = obstaclesOf(printedMaze)
    println("maze1() = ${eastSouthMaze1(obstacles)}")
    println()
}
