# Shadow Donkey Kong Game

A Java-based game using the Bagel Library where players control Mario to defeat Donkey Kong.

## Overview

Shadow Donkey Kong is a 2D platformer game implemented in Java using the Bagel Library. The player controls Mario, navigating through platforms, collecting items, and ultimately defeating Donkey Kong.


## Gameplay

- **Controls:**
    - Arrow keys (←, →, ↑, ↓): Move Mario
    - S: Shoot bullets (Level 2 only)

- **Objective:**
    - Level 1: Touch Donkey Kong with a hammer to win
    - Level 2: Shoot Donkey Kong with bullets five times to win

## Project Structure

### Resources (`res/`)

This directory contains all game assets and configuration files:

- `app.properties`: Contains attributes of game objects (starting positions, font sizes, etc.)
- `message.properties`: Contains messages for different game states (winning, losing, etc.)
- Various image files for game objects

### Source Code (`src/`)

#### Game Objects (`src/objects/`)

- `Physic`: Contains all constants for physics logic
- `Entity`: Base class for all game objects
    - `Platform`: Represents platforms in the game
    - `CollectibleEntity`: Base class for collectible items
        - `Blaster`: Represents the blaster item
        - `Hammer`: Represents the hammer item
    - `ProjectileEntity`: Base class for projectiles
        - `Banana`: Bananas thrown by intelligent monkeys
        - `Bullet`: Bullets shot by Mario
    - `StaticOnPlatformEntity`: Objects that remain static on platforms after gravity
        - `Donkey`: Represents Donkey Kong
        - `Barrel`: Represents barrels
        - `Ladder`: Represents ladders
    - `Mario`: The main character controlled by the player
    - `NormalMonkey`: Monkeys that move automatically based on predefined rules
        - `IntelligentMonkey`: Enhanced monkeys that can shoot bananas every 5 seconds

#### Game Screens (`src/screens/`)

- `AbstractScreen`: Base class for all game screens
    - `HomeScreen`: The initial game screen
    - `Level1Screen`: First level of the game
    - `Level2Screen`: Second level of the game
    - `GameEndScreen`: Screen displayed when the game ends

#### Main Classes (`src/`)

- `IOUtils`: Utility class to load properties from resource files
- `ShadowDonkeyKong`: Main class with entry point, manages game screens and flow

## Installation and Running

1. Ensure you have Java installed on your system
2. Make sure the Bagel Library is properly configured in your project
3. Clone the repository
4. Compile and run the `ShadowDonkeyKong` class

## Game Features

- Two challenging levels with different objectives
- Various enemies: normal monkeys and intelligent monkeys that shoot bananas
- Collectible items: hammer and blaster
- Physics-based gameplay with platforms and gravity
- Multiple game screens: home, levels, and end game


## Author

Minh Triet Pham

---

*This game is developed as an educational project and is not intended for commercial use.*