# CornerBox Component

A dynamically resizable and draggable box with rounded corners, developed using Jetpack Compose. This component supports smooth resizing, boundary constraints, and center dragging, making it a versatile UI element for Android applications.

---

## Features

- **Resizable Corners**: Drag any corner to resize the box.
- **Center Dragging**: Move the entire box by dragging its center.
- **Boundary Constraints**: Ensures the box remains within its parent boundaries.
- **Corner Radius Validation**: Prevents the box size from shrinking below twice the corner radius.
- **Dynamic Boundaries**: Automatically adapts to the size of the parent container.
- **Smooth Animations**: Includes seamless transitions for resizing and dragging.

---

## Screenshots

| Resizing from Corners | Dragging from Center |
|------------------------|----------------------|
| ![Resizing](./screenshots/resizing.gif) | ![Dragging](./screenshots/dragging.gif) |

---

## Installation

1. Clone this repository:
   ```bash
   git clone https://github.com/your-username/corner-box.git
   ```
2. Add the `CornerBox` component to your project.

---

## Usage

### Adding the Component
```kotlin
@Composable
fun MyScreen() {
    MaterialTheme {
        CornerBox()
    }
}
```

### Customizing the Component
You can modify the initial position, size, and corner radius:
```kotlin
@Composable
fun MyCustomScreen() {
    MaterialTheme {
        CornerBox(
            modifier = Modifier.fillMaxSize()
        )
    }
}
```

---

## Key Functions

### `isValidSize`
Ensures the box remains within boundaries and doesn't shrink below twice the corner radius:
```kotlin
private fun isValidSize(
    holeOffset: Offset,
    holeSize: Size,
    boundaryOffset: Offset,
    boundarySize: Size,
    cornerRadius: Float
): Boolean
```

### `isNearCorner`
Checks if a touch point is near a corner:
```kotlin
private fun isNearCorner(
    touchPoint: Offset,
    cornerOffset: Offset,
    radius: Float
): Boolean
```

### `isInsideBox`
Verifies if a touch point is inside the box:
```kotlin
private fun isInsideBox(point: Offset, boxOffset: Offset, boxSize: Size): Boolean
```

---

## Future Enhancements

- **Customizable Animations**: Allow developers to adjust animation duration and style.
- **Multi-Box Support**: Enable multiple resizable boxes within the same parent.
- **Advanced Styling**: Support additional visual effects like shadows and gradients.

---

## Contributing

We welcome contributions! Please follow these steps:
1. Fork the repository.
2. Create a feature branch:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add feature-name"
   ```
4. Push your branch:
   ```bash
   git push origin feature-name
   ```
5. Open a pull request.

---

## License

This project is licensed under the [MIT License](./LICENSE).

---

## Contact

For questions or feedback, please reach out to [your-email@example.com](mailto:your-email@example.com).

Happy coding! 🚀