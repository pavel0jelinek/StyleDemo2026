package io.github.pavel0jelinek.demo.eastSouthMaze

import io.github.pavel0jelinek.demo.eastSouthMaze.maze3.maze3
import io.github.pavel0jelinek.demo.eastSouthMaze.maze3.toObstacleRows

fun mazeOf(printedMaze: String): Maze {
    val obstacles: List<List<Boolean>> = printedMaze.lines().filterNot { it.isBlank() }
        .map { line ->
            line.trim().map { cell: Char ->
                when (cell) {
                    '.' -> false
                    'X' -> true
                    else -> throw IllegalArgumentException("Unexpected char '$cell'. Use only dots and capital X.")
                }
            }
        }
    return Maze(obstacles)
}

fun mazeDefinedBy(sizeX: Int, sizeY: Int, isObstacle: Position.()-> Boolean)= Maze(
    List(sizeY){ y->
        List(sizeX) { x ->
            isObstacle(Position(x, y))
        }
    }
)



fun solveAndPrint(description:String, maze: Maze) {
    println("\n$description\n${ maze.to2DString()}"+
        "\nReturned results: maze1: ${maze1(maze.obstacles)}, " +
                "maze2a: ${maze2a(maze.obstacles)}, " +
                "maze2b: ${maze2b(maze)}, " +
                "maze3: ${maze3(maze.toObstacleRows())}"
    )
}

fun main() {
    solveAndPrint("3 choices to move south",mazeOf("""
        ...
        ...
    """.trimIndent()))

    solveAndPrint("One choice to move south",mazeOf("""
        .X.
        ...
    """.trimIndent()))

    solveAndPrint("Impenetrable wall",mazeDefinedBy(10, 10){
        x==5
    })
    solveAndPrint("Wall with one passage", mazeDefinedBy(10, 10){
        x==5 && y!=5
    })

}