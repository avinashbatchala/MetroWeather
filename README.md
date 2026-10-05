# MetroWeather

A **Windows 10 Mobile (MSN Weather) style** weather app for Android — part of the
**MetroSuite**.

MetroWeather is the weather companion to the **Win10 Start** launcher. It reimagines the
weather experience in the Windows 10 Mobile "Metro" design language rather than the Material
look it was forked from: a flat, square, accent-driven UI built around an immersive animated
condition scene.

## Features

- **Immersive condition scene** — a day/night gradient plus animated sun, clouds, rain,
  snow, fog and stars, with scroll parallax and an automatic light/dark foreground.
- **`now · hourly · daily · places` pivot** with a centred hero temperature.
- **Hourly temperature graph** — a curve with weather icons and precipitation.
- **Detail tiles** for wind, humidity, pressure, UV index, visibility, precipitation,
  sunrise and sunset.
- **Windows 10 Mobile settings pivots** (appearance / weather / notifications / about) with
  flat Metro dialogs, toggles and lists.
- **Live Tile** — pin the current city to the **Win10 Start** launcher's Start screen.
- **Multiple weather sources** (Open-Meteo, Met Norway, NWS, DWD, SMHI, FMI and more) with a
  per-location source picker.

## Design system

The UI is built on the shared **`:metro-ui`** Windows Metro design system
(`MetroSuite/design`), consumed as a Gradle composite build, so the launcher and this app
stay visually consistent.

## Build

Requires JDK 21 and an Android SDK. Always pass `--no-configuration-cache` on the CLI
(the Java compile task cannot be serialized into the configuration cache).

```
./gradlew :app:assembleDebug --no-configuration-cache
./gradlew :app:testDebugUnitTest --no-configuration-cache
```

This checkout expects the shared design module at `../../design` (the MetroSuite layout).
Set `sdk.dir` in a local, gitignored `local.properties`.

## License

GPL-3.0. See [LICENSE](LICENSE).

## Attribution

MetroWeather is a fork of [WeatherMaster](https://github.com/PranshulGG/WeatherMaster) by
PranshulGG, licensed under GPL-3.0. The upstream weather-source integrations and forecast
data model originate there.
