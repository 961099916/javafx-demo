# JavaFX + Bootstrap Demo Application

A modern JavaFX desktop application demonstrating the integration of Bootstrap CSS styling principles with JavaFX components.

## 🆕 Java 21 Support

This project has been upgraded to **Java 21** with full support for:
- **Virtual Threads**: Lightweight concurrency for improved performance
- **Record Classes**: Immutable data structures with pattern matching
- **Modern JavaFX 21**: Latest graphics and UI framework
- **Enhanced Async Processing**: CompletableFuture improvements

## Features

- **Bootstrap CSS Integration**: Custom CSS stylesheet that implements Bootstrap 5 styling principles for JavaFX components
- **Java 21 Modern Features**: Virtual threads, record classes, pattern matching, enhanced async processing
- **Comprehensive UI Components**: Buttons, forms, tables, alerts, progress bars, and more
- **Multiple Demo Tabs**: Form demo, table demo, and components showcase
- **Modern Design**: Clean, responsive design following Bootstrap design patterns
- **Data Binding**: JavaFX properties and observable collections
- **Form Validation**: Client-side form validation with user feedback
- **CRUD Operations**: Create, read, update, and delete operations on table data
- **High Performance**: Virtual threads for better concurrency

## Project Structure

```
javafx-demo/
├── pom.xml                              # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/javafxdemo/
│   │   │       ├── JavaFXDemoApplication.java    # Main application class
│   │   │       └── controller/
│   │   │           └── MainController.java       # Main UI controller
│   │   └── resources/
│   │       ├── css/
│   │       │   └── bootstrap-javafx.css         # Bootstrap styling for JavaFX
│   │       ├── fxml/
│   │       │   └── main-view.fxml               # Main UI layout
│   │       └── images/
│   │           └── app-icon.png                 # Application icon (optional)
│   └── test/
└── README.md
```

## Technologies Used

- **JavaFX 21**: Latest Java GUI framework with modern graphics support
- **Java 21**: Cutting-edge Java with virtual threads and record classes
- **BootstrapFX**: Bootstrap styling integration for JavaFX
- **Maven**: Build automation and dependency management
- **CSS3**: Custom Bootstrap-inspired styling with modern features
- **Virtual Threads**: Lightweight concurrency for improved performance

## Prerequisites

- Java 21 (recommended) or Java 17+ with JavaFX support
- Maven 3.6 or higher
- JavaFX SDK (if not using Maven dependencies)

## Getting Started

### 🆕 Java 21 Quick Start

```bash
# Option 1: Use Java 21 specific script
chmod +x run-java21.sh
./run-java21.sh

# Option 2: Standard Maven (auto-detects Java version)
mvn clean javafx:run

# Option 3: Build and run JAR
mvn clean package
java -jar target/javafx-demo-1.0-SNAPSHOT.jar
```

### 1. Clone or Download the Project

```bash
# Navigate to your project directory
cd /Users/zhangjiahao/IdeaProjects/javafx-demo
```

### 2. Build the Project

```bash
# Clean and compile the project
mvn clean compile
```

### 3. Run the Application

```bash
# Run the application using JavaFX Maven plugin
mvn javafx:run

# Or create and run the executable JAR
mvn package
java -jar target/javafx-demo-1.0-SNAPSHOT.jar
```

## Bootstrap CSS Classes

The application includes a comprehensive Bootstrap CSS stylesheet with the following components:

### Buttons
- `.btn-primary`, `.btn-secondary`, `.btn-success`
- `.btn-danger`, `.btn-warning`, `.btn-info`
- `.btn-light`, `.btn-dark`

### Form Controls
- `.form-control` - Text fields, text areas, combo boxes
- `.check-box` - Check box styling
- `.radio-button` - Radio button styling
- `.spinner` - Spinner control styling

### Layout Components
- `.card` - Card containers with shadows and rounded corners
- `.card-header`, `.card-body` - Card sections
- `.titled-pane` - Collapsible sections

### Alerts and Notifications
- `.alert`, `.alert-primary`, `.alert-success`
- `.alert-danger`, `.alert-warning`, `.alert-info`

### Tables
- `.table-view` - Table styling with Bootstrap appearance
- Row selection highlighting
- Header styling

### Progress Indicators
- `.progress-bar` - Progress bar styling

### Navigation
- `.menu-bar` - Menu bar styling
- `.tool-bar` - Tool bar styling

## Demo Features

### Form Demo Tab
- User registration form with validation
- Personal information fields
- Contact information
- Preferences section
- Form submission with progress indication

### Table Demo Tab
- User management table
- CRUD operations (Create, Read, Update, Delete)
- Bootstrap-styled table with selection highlighting
- Action buttons for table operations

### Components Demo Tab
- Alert messages showcase
- Button style demonstrations
- Progress bar examples
- Various UI component styling

## Customization

### Adding New Components

1. **Create FXML Layout**: Add new `.fxml` files in `src/main/resources/fxml/`
2. **Create Controller**: Add controller classes in `com.example.javafxdemo.controller`
3. **Apply Styling**: Use Bootstrap CSS classes in your components
4. **Update Main Application**: Modify navigation or add new tabs

### Modifying Bootstrap Styling

Edit `src/main/resources/css/bootstrap-javafx.css` to customize:
- Color schemes
- Component sizes
- Spacing and padding
- Typography
- Effects and animations

### Adding New CSS Classes

Add custom CSS classes to `bootstrap-javafx.css`:

```css
.my-custom-button {
    -fx-background-color: #custom-color;
    -fx-text-fill: white;
    /* Additional styling properties */
}
```

Then apply in FXML:

```xml
<Button text="Custom Button" styleClass="my-custom-button" />
```

## Building for Distribution

### Create Executable JAR

```bash
mvn clean package
```

This creates a standalone JAR file in `target/javafx-demo-1.0-SNAPSHOT.jar`

### Create Native Installer

For platform-specific installers (Windows MSI, macOS DMG, Linux DEB/RPM):

```bash
# Requires JavaFX jpackage tool
jpackage --input target/ \
         --name JavaFXBootstrapDemo \
         --main-jar javafx-demo-1.0-SNAPSHOT.jar \
         --main-class com.example.javafxdemo.JavaFXDemoApplication \
         --type app-image
```

## Building Installers for Different Platforms

### Prerequisites
- JDK 16 or higher (for jpackage tool)
- Maven 3.6+

### Java Version Requirements
The jpackage tool for creating native installers requires JDK 16 or higher. 
If you're using an older Java version, please upgrade to JDK 16+ from one of these sources:
- [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
- [OpenJDK](https://adoptium.net/)
- [Azul Zulu](https://www.azul.com/downloads/?package=jdk)

### Using Shell Scripts

#### For Unix/Linux/macOS:
```bash
# Make the script executable
chmod +x build-installers.sh

# Build installers for all platforms
./build-installers.sh

# Build installer for specific platform
./build-installers.sh windows
./build-installers.sh linux
./build-installers.sh mac
```

#### For Windows:
```cmd
# Build installer for Windows
build-installers.bat
```

### Using Maven Profiles

```bash
# Build for Windows
mvn package -Pwindows

# Build for Linux
mvn package -Plinux

# Build for Mac
mvn package -Pmac
```

### Output Locations
- Windows: `target/installers/windows/`
- Linux: `target/installers/linux/`
- Mac: `target/installers/mac/`

## Troubleshooting

### Common Issues

1. **JavaFX Runtime Not Found**
   - Ensure JavaFX dependencies are properly configured in `pom.xml`
   - Check that Maven is downloading dependencies correctly

2. **CSS Not Applied**
   - Verify CSS file path in `JavaFXDemoApplication.java`
   - Check that CSS classes match those used in FXML

3. **FXML Loading Errors**
   - Verify FXML file path and controller class
   - Check that all imported classes are available

4. **Performance Issues**
   - Consider reducing CSS complexity for better performance
   - Optimize large table views with pagination

### Development Tips

- Use Scene Builder for visual FXML editing
- Test CSS changes incrementally
- Follow JavaFX threading rules for UI updates
- Use proper property binding for reactive UI

## Java 21 New Features

### 🧵 Virtual Threads
The application now uses Java 21 virtual threads for background processing:
```java
Thread.ofVirtual().start(() -> {
    processFormSubmission(formData);
});
```

### 📋 Record Classes
Modern immutable data structures:
```java
public record FormData(String name, String email, String password,
                      String description, String country, int age,
                      boolean newsletter, String gender, double rating) {}
```

### ⚡ Enhanced Concurrency
Improved async processing with CompletableFuture:
```java
CompletableFuture.runAsync(() -> {
    // UI updates
}, javafx.application.Platform::runLater);
```

## Future Enhancements

- Add more comprehensive form validation
- Implement data export/import functionality
- Add charting and visualization components
- Data persistence with database integration
- Internationalization support
- Custom animations and transitions
- More Java 21 features (pattern matching, sealed classes)

## License

This project is open source and available under the MIT License.

## Contributing

Feel free to contribute by:
- Adding new Bootstrap-styled components
- Improving existing CSS styling
- Adding new demo features
- Fixing bugs and improving performance
- Adding documentation and examples