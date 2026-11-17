<div align="center">
<h1>Low HP Warning</h1>
<hr>
<p><strong>3 controls in one warning: current health, a configurable threshold, HUD visibility.</strong></p>
<p>
<img src="https://img.shields.io/badge/Minecraft-1.21.11-62B47A?style=flat-square&logo=minecraft" alt="Minecraft 1.21.11">
<img src="https://img.shields.io/badge/Fabric%20Loader-0.18.4-DBD0B4?style=flat-square&logo=fabric" alt="Fabric Loader 0.18.4">
<img src="https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk" alt="Java 21">
<img src="https://img.shields.io/badge/Gradle-9.2.1-02303A?style=flat-square&logo=gradle" alt="Gradle 9.2.1">
</p>
<p>
<a href="https://github.com/hhhhhffd/LowHPWarning/releases"><img src="https://img.shields.io/github/v/release/hhhhhffd/LowHPWarning?style=flat-square" alt="Latest release"></a>
<a href="https://github.com/hhhhhffd/LowHPWarning/releases"><img src="https://img.shields.io/github/downloads/hhhhhffd/LowHPWarning/total?style=flat-square" alt="Downloads"></a>
<a href="LICENSE"><img src="https://img.shields.io/github/license/hhhhhffd/LowHPWarning?style=flat-square" alt="MIT license"></a>
</p>
<p><a href="https://github.com/hhhhhffd/LowHPWarning/releases">Download</a> · <a href="#architecture">Architecture</a> · <a href="#build">Build</a></p>
<img src="docs/media/preview.png" alt="Low HP Warning in-game preview">
</div>

## Overview

Low HP Warning is a client-side mod that shows a compact current-health badge below the crosshair, such as `7 HP` or `7.5 HP`. It uses current health, excluding yellow absorption hearts, and appears only below a configurable threshold.

## Technical Stack

| Component | Version / role |
| --- | --- |
| Minecraft Java | 1.21.11 |
| Fabric Loader | 0.18.4 or later |
| Java | 21 or later; build toolchain 21 |
| Fabric Loom / Gradle | 1.13.6 / 9.2.1 |
| Mod Menu | 17 or later, optional settings UI; compiled against 17.0.1 |

## Architecture

```mermaid
flowchart LR
    G[HUD render mixin] --> O[WarningOverlay]
    H[Current player health] --> O
    C[Local configuration] --> O
    O --> L[LowHealth threshold and formatting]
    L --> B[Badge below crosshair]
```

- **Current health only:** absorption is excluded; one red heart represents 2 HP.
- **Strict threshold:** the default is 10 HP, so 10 and 20 HP are hidden while 5 HP is shown.
- **HUD-aware rendering:** the badge is hidden in menus, spectator mode, when dead, and when F1 disables the HUD.

## Project Structure

```text
.
├── src/client/          # Health logic, settings, HUD and mixin
├── src/main/            # Fabric metadata and mixin declarations
├── tests/               # Standalone health logic checks
├── docs/media/          # In-game preview
├── build.gradle         # Loom build configuration
└── gradle.properties    # Minecraft and tool versions
```

## Install and configure

1. Install Java 21 or later and Fabric Loader 0.18.4 or later for Minecraft Java 1.21.11.
2. Place the [release JAR](https://github.com/hhhhhffd/LowHPWarning/releases) in the client `mods` folder. The mod is client-only and does not require Fabric API.
3. Optionally install Mod Menu 17 and open **Mods → Low HP Warning → Configure** to adjust the threshold or switch the warning off. Values from 0.01 to 1024 HP with up to two decimal places are accepted. Click **Done** to save.

Alternatively, edit `config/lowhpwarning.json` while Minecraft is closed:

```json
{
  "enabled": true,
  "threshold": 10.0
}
```

## Build

With Java 21, run `./gradlew build` (Windows: `gradlew.bat build`).

<details><summary>Standalone logic checks</summary>

```sh
javac -d build/health-tests src/client/java/com/lowhpwarning/LowHealth.java tests/LowHealthTest.java
java -cp build/health-tests LowHealthTest
```
</details>

## Links

- [Releases and downloads](https://github.com/hhhhhffd/LowHPWarning/releases)
- [Source repository](https://github.com/hhhhhffd/LowHPWarning)

## License

[MIT](LICENSE) © 2026 hhhhhffd.
