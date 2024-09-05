package com.example.fininfotechtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fininfotechtask.ui.theme.FinInfotechTaskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxSize()) {
                FinInfotechTaskTheme {
                    NumberHighlighterApp()
                }
            }
        }
    }
}

@Composable
fun NumberHighlighterApp() {
    val numbers = remember { (1..100).toList() }
    var selectedRule by remember { mutableStateOf(HighlightRule.NONE) }
    var highlightedNumbers by remember { mutableStateOf(setOf<Int>()) }

    LaunchedEffect(selectedRule) {
        highlightedNumbers = when (selectedRule) {
            HighlightRule.NONE -> emptySet()
            HighlightRule.ODD -> getOddNumbers(1..100)
            HighlightRule.EVEN -> getEvenNumbers(1..100)
            HighlightRule.PRIME -> getPrimeNumbers(1..100)
            HighlightRule.FIBONACCI -> getFibonacciNumbers(100)
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        RuleSelector(selectedRule = selectedRule, onRuleSelected = { selectedRule = it })
        Spacer(modifier = Modifier.height(16.dp))
        NumberGrid(numbers = numbers, highlightedNumbers = highlightedNumbers)
    }
}

@Composable
fun RuleSelector(
    selectedRule: HighlightRule,
    onRuleSelected: (HighlightRule) -> Unit
) {
    Column {
        Text("Select a Rule:", style = MaterialTheme.typography.headlineMedium)
        HighlightRule.entries.forEach { rule ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedRule == rule,
                    onClick = { onRuleSelected(rule) }
                )
                Text(rule.displayName)
            }
        }
    }
}

@Composable
fun NumberGrid(
    numbers: List<Int>,
    highlightedNumbers: Set<Int>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(numbers) { number ->
            val isHighlighted = highlightedNumbers.contains(number)
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .size(100.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighlighted) MaterialTheme.colorScheme.primary else Color.LightGray
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = number.toString(),
                        fontSize = 26.sp,
                        textAlign = TextAlign.Center,
                        color = if (isHighlighted) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

enum class HighlightRule(val displayName: String) {
    NONE("None"),
    ODD("Odd Numbers"),
    EVEN("Even Numbers"),
    PRIME("Prime Numbers"),
    FIBONACCI("Fibonacci Numbers")
}

fun getOddNumbers(range: IntRange): Set<Int> {
    return range.filter { it % 2 != 0 }.toSet()
}

fun getEvenNumbers(range: IntRange): Set<Int> {
    return range.filter { it % 2 == 0 }.toSet()
}

fun getPrimeNumbers(range: IntRange): Set<Int> {
    return range.filter { isPrime(it) }.toSet()
}

fun isPrime(n: Int): Boolean {
    if (n <= 1) return false
    if (n <= 3) return true
    if (n % 2 == 0 || n % 3 == 0) return false
    var i = 5
    while (i * i <= n) {
        if (n % i == 0 || n % (i + 2) == 0) return false
        i += 6
    }
    return true
}

fun getFibonacciNumbers(max: Int): Set<Int> {
    val fibSet = mutableSetOf(0, 1)
    var a = 0
    var b = 1
    while (true) {
        val next = a + b
        if (next > max) break
        fibSet.add(next)
        a = b
        b = next
    }
    return fibSet.filter { it in 1..max }.toSet()
}


@Preview(showBackground = true)
@Composable
private fun NumberHighlighterPrev() {
    NumberHighlighterApp()
}