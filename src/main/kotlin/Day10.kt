package de.derniklaas

import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.IntNum
import com.microsoft.z3.Status
import de.derniklaas.utils.Pathfinding
import utils.AoCDay
import utils.splitAndMapToInt

class Day10 : AoCDay() {

    private val machines = input.map { line ->
        val parts = line.split(" ")
        val targetLights =
            parts.first().removeSurrounding("[", "]").split("").filter { it.isNotBlank() }.map { it == "#" }
        val targetJoltages = parts.last().removeSurrounding("{", "}").splitAndMapToInt(",")
        val buttons = parts.subList(1, parts.size - 1).map { button ->
            button.removeSurrounding("(", ")").splitAndMapToInt(",").toSet()
        }
        MachineConfiguration(buttons, targetLights, targetJoltages)
    }

    override fun part1(): Any {
        var sum = 0
        for (machine in machines) {
            val cost = Pathfinding.search(
                start = List(machine.targetLights.size) { false },
                goalFunction = { state -> state == machine.targetLights },
                neighbours = { state ->
                    machine.buttons.map { flips ->
                        state.mapIndexed { index, state -> if (index in flips) !state else state }
                    }.map { it to 1 }
                }
            )?.cost ?: error("No targets found")
            sum += cost
        }
        return sum
    }

    override fun part2(): Any {
        var sum = 0
        for (machine in machines) {
            Context().use { context ->
                val solver = context.mkOptimize()
                val zero = context.mkInt(0)

                // Count number of presses, ensure it's positive.
                val buttons = machine.buttons.indices
                    .map { context.mkIntConst("button$it") }
                    .onEach { button -> solver.Add(context.mkGe(button, zero)) }
                    .toTypedArray()

                // For each joltage counter, require that the sum of all presses of all buttons that increment
                // it is equal to the target value specified.
                machine.targetJoltages.forEachIndexed { counter, targetValue ->
                    val buttonsThatIncrement = machine.buttons
                        .withIndex()
                        .filter { (_, counters) -> counter in counters }
                        .map { buttons[it.index] }
                        .toTypedArray()
                    val target = context.mkInt(targetValue)
                    val sumOfPresses = context.mkAdd(*buttonsThatIncrement) as IntExpr
                    solver.Add(context.mkEq(sumOfPresses, target))
                }

                // Describe that the preses is the sum of all button presses and try to minimize it.
                val presses = context.mkIntConst("presses")
                solver.Add(context.mkEq(presses, context.mkAdd(*buttons)))
                solver.MkMinimize(presses)

                if (solver.Check() != Status.SATISFIABLE) error("No solution found $machine")
                sum += solver.model.evaluate(presses, false).let { it as IntNum }.int
            }
        }
        return sum
    }

    private data class MachineConfiguration(
        val buttons: List<Set<Int>>,
        val targetLights: List<Boolean>,
        val targetJoltages: List<Int>
    )
}

fun main() {
    Day10().execute()
}
