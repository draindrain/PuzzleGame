# Project Context

## Overview
Building a simple card game for Android using Kotlin and Jetpack Compose.

## Developer Background
- Experienced with React/TypeScript web development
- New to Android development
- Prefers "vibe coding" with AI assistance

## Tech Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **IDE:** Android Studio
- **Min SDK:** API 24 (Android 7.0)

## Game Description (from user)
- Cards rendered on a square grid
- Cards in hand area
- Simple design (will use existing assets or AI-generated art)
- Needs help with layout

## React → Compose Quick Reference

| React | Compose |
|-------|---------|
| `useState` | `remember { mutableStateOf() }` |
| `useEffect` | `LaunchedEffect` |
| Props | Function parameters |
| JSX elements | `@Composable` functions |
| CSS/className | `Modifier` chains |
| `onClick` | `Modifier.clickable { }` |
| `children` | `content: @Composable () -> Unit` |
| Context | `CompositionLocal` |

## Example Compose Pattern

```kotlin
@Composable
fun GameCard(
    title: String,
    onClick: () -> Unit
) {
    var flipped by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(80.dp)
            .clickable { onClick() }
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(text = title)
    }
}
```

## Layout Patterns

```kotlin
// Vertical stack (like flex-direction: column)
Column { ... }

// Horizontal stack (like flex-direction: row)
Row { ... }

// Grid
LazyVerticalGrid(columns = GridCells.Fixed(4)) { ... }

// Absolute positioning
Box {
    // Children can use Modifier.align()
}
```

## Setup Status
- [x] Android Studio installed
- [x] Android SDK downloaded
- [ ] Project created in Android Studio
- [ ] Emulator or phone configured for testing

## Next Steps
1. Create Empty Activity project in Android Studio
2. User will describe game mechanics in detail
3. Build out the card grid and hand UI
4. Implement game logic
