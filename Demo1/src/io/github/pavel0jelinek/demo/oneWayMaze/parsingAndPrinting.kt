package io.github.pavel0jelinek.demo.oneWayMaze

import io.github.pavel0jelinek.demo.oneWayMaze.oneWayMaze1996.maze3
import io.github.pavel0jelinek.demo.oneWayMaze.oneWayMaze1996.toObstacleRows

fun mazeOf(printedMaze: String): ObstacleMaze {
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
    return ObstacleMaze(obstacles)
}

fun mazeFromPredicate(sizeX: Int, sizeY: Int, isObstacle: Position.()-> Boolean)= ObstacleMaze(
    List(sizeY){ y->
        List(sizeX) { x ->
            isObstacle(Position(x, y))
        }
    }
)



fun solveAndPrint(description:String, maze: ObstacleMaze) {
    println("\n$description\n${ maze.to2DString()}"+
        "\nReturned results: maze1: ${oneWayMaze1(maze.obstacles)}, " +
                "maze2a: ${oneWayMaze2a(maze.obstacles)}, " +
                "maze2b: ${oneWayMaze2b(maze)}, " +
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

    solveAndPrint("Impenetrable wall",mazeFromPredicate(10, 10){
        x==5
    })
    solveAndPrint("Wall with one passage", mazeFromPredicate(10, 10){
        x==5 && y!=5
    })

}