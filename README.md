# Low HP Warning 1.0.0

A compact current-health badge sits below the crosshair when your health is
**strictly below** the configured threshold, such as `7 HP` or `7.5 HP`.
It uses actual current health, not the extra yellow absorption hearts.
One red heart is 2 HP. The default threshold is 10 HP, so 10 and 20 HP are
hidden while 5 HP is shown. The badge is also hidden in menus, spectator mode,
when dead, and when the HUD is disabled with F1.

## Install and configure

Place the release JAR in the client `mods` folder. This mod is client-only,
for Minecraft Java **1.21.11**, Fabric Loader **0.18.4 or later**, and Java 21.
Fabric API is not required by the mod.

With optional **Mod Menu 17**, open **Mods → Low HP Warning → Configure** to
adjust the threshold or switch the warning off. Values from 0.01 to 1024 HP
are accepted, with up to two decimal places. Click **Done** to save. You can
edit `config/lowhpwarning.json` while the game is closed instead:

```json
{
  "enabled": true,
  "threshold": 10.0
}
```

## Build

Run `./gradlew build` (`gradlew.bat build` on Windows) using Java 21.
Run the standalone logic checks using:

```sh
javac -d build/health-tests src/client/java/com/lowhpwarning/LowHealth.java tests/LowHealthTest.java
java -cp build/health-tests LowHealthTest
```
