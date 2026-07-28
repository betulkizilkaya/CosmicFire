# CosmicFire

CosmicFire is a retro-style desktop space shooter developed with **Java Swing**. The player controls a spaceship, fires lasers at alien enemies, earns points, and tries to survive until the timer ends.

The game includes animated enemy movement, sound effects, background music, score tracking, and persistent high-score storage with MySQL.

## Features

- Retro-style 2D space shooter gameplay
- Keyboard-controlled spaceship movement
- Laser shooting mechanics
- Randomly positioned alien enemies
- Collision detection between lasers and enemies
- Score and high-score tracking
- 60-second game timer
- Increasing gameplay pressure as enemies move downward
- Background music and sound effects
- Restart and exit options after the game ends
- Persistent high-score storage with MySQL
- Java Swing-based graphical interface

## Controls

| Action | Key |
|---|---|
| Move left | Left Arrow |
| Move right | Right Arrow |
| Fire laser | Ctrl |

## Technologies

- Java
- Java Swing
- Java 2D Graphics
- Java Sound API
- MySQL
- JDBC
- NetBeans
- Apache Ant

## Project Structure

```text
CosmicFire/
├── src/
│   ├── Oyun.java          # Main game logic, rendering, controls, audio, and collisions
│   ├── OyunEkrani.java    # Game window and application entry point
│   └── DBManager.java     # MySQL score storage and high-score queries
├── nbproject/             # NetBeans project configuration
├── dist/                  # Generated application files
├── build.xml              # Apache Ant build file
├── arkaplan.png           # Background image
├── uzaygemisi.png         # Spaceship image
├── uzayli.png             # Alien image
├── background.wav         # Background music
├── laser.wav              # Laser sound effect
└── win.wav                # End-of-round sound effect
```

## AI-Generated Assets

The visual and audio assets used in this project were created with the assistance of artificial intelligence tools.

This includes the game background, spaceship and alien images, background music, and sound effects. These assets were then selected, organized, and integrated into the Java game by the project developer.

## Database Setup

CosmicFire uses MySQL to save scores and retrieve the highest score.

Create the database and table with the following SQL commands:

```sql
CREATE DATABASE game_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE game_db;

CREATE TABLE highscores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    score INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Database Configuration

Open `src/DBManager.java` and update the connection settings according to your local MySQL installation:

```java
private static final String URL = "jdbc:mysql://localhost:3306/game_db";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
```

Do not commit real production credentials to a public repository.

## Installation and Usage

### 1. Clone the repository

```bash
git clone https://github.com/betulkizilkaya/CosmicFire.git
cd CosmicFire
```

### 2. Install the requirements

Make sure the following are installed:

- Java Development Kit (JDK)
- MySQL Server
- MySQL Connector/J
- NetBeans or Apache Ant

### 3. Configure MySQL

Create the `game_db` database and `highscores` table, then update the credentials in `src/DBManager.java`.

### 4. Add MySQL Connector/J

Add the MySQL Connector/J JAR file to the project's classpath in NetBeans.

### 5. Run with NetBeans

Open the project in NetBeans and run `OyunEkrani.java`.

### 6. Build with Apache Ant

```bash
ant clean jar
```

After building, run the generated JAR file from the `dist/` directory:

```bash
java -jar dist/Uzay_Oyunu.jar
```

## Game Rules

- Each destroyed alien adds `10` points to the score.
- A new alien appears after an enemy is destroyed.
- The player has `60` seconds to achieve the highest possible score.
- The game ends if an alien reaches Earth.
- The final score is saved to MySQL.
- The highest recorded score is displayed during gameplay and on the game-over screen.

## Required Asset Files

The game loads image and audio files using relative paths. Keep these files in the project root when running the application:

```text
arkaplan.png
uzaygemisi.png
uzayli.png
background.wav
laser.wav
win.wav
```

Moving or renaming these files without updating the Java code may prevent images or sounds from loading.

## Security Notes

This repository is an educational desktop game. For a production-quality application:

- Store database credentials outside the source code.
- Use environment variables or a configuration file excluded by `.gitignore`.
- Use try-with-resources for JDBC connections and statements.
- Handle missing audio and image assets with user-friendly error messages.
- Avoid running the application with a highly privileged database account.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

© 2025 [Betül Kızılkaya](https://github.com/betulkizilkaya)
