package com.drain.puzzlegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.drain.puzzlegame.ui.theme.PuzzleGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PuzzleGameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PuzzleGameScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PuzzleGameScreen(modifier: Modifier = Modifier) {
    val gameState = remember { GameState(gridSize = 5) }

    GameGrid(
        gameState = gameState,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PuzzleGamePreview() {
    PuzzleGameTheme {
        PuzzleGameScreen()
    }
}