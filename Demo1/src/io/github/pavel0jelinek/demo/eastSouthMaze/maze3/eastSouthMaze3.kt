package io.github.pavel0jelinek.demo.eastSouthMaze.maze3

/**
 * The competition where I encountered this problem was held in 1995/96, we programmed in Pascal or C.
 * There were no such things like BigInteger or MutableMap, if we did not implement them ourselves.
 *
 *  I will use none of my helpers except [MyBigInt], and I will use interface [ObstacleRow] to
 *  - separate the data representation of the maze from the problem solution
 *  - and to allow for memory-efficient data representations.
 */
fun maze3(obstacles: List<ObstacleRow>): MyBigInt {
    val sizeY = obstacles.size
    require(sizeY > 0)
    val sizeX = obstacles.map { it.sizeX }.distinct().singleOrNull()
        ?: throw IllegalArgumentException("All rows must have the same length")
    require(sizeX > 0)

    // For each tile, we compute the number of legat paths to that tile.
    // To save memory, we can drop old, no longer needed rows of path counts.
    // [currentRow] contains the path we're just processing.

    // First row of path counts
    var currentRow:List<MyBigInt> = buildList {
        var obstacleFound = false
        for (x in 0..<sizeX) {
            if (obstacles[0].isObstacle(x)!!) obstacleFound = true
            add(if (obstacleFound) MyBigInt.ZERO else MyBigInt.ONE)
        }
    }

    // Other rows of path counts
    for (y in 1..<sizeY) {
        val nextRow = buildList {
            for (x in 0..<sizeX) {
                val newValue = if (obstacles[y].isObstacle(x)!!) MyBigInt.ZERO
                else
                    // Number of paths from the north
                    currentRow[x] +
                    // Number of paths from the west is the last element of the currently constructed list.
                            (lastOrNull()?:MyBigInt.ZERO)
                add(newValue)
            }
        }
        currentRow = nextRow
    }
    return currentRow.last()
}
