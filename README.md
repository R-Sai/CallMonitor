## Call Monitor
- Monitors and records a log of incoming and outgoing calls on the device.
- Runs a web server in the background which can be used to fetch the current call status and call log by any device on the same Wifi network.

## Stack
- Jetpack compose
- Kotlin flows
- Hilt
- Room for persistence
- Ktor
- Multi module

## Usage
The web server runs on port 12345. The following endpoints are available:
- Available endpoints - Lists the available endpoints:
  - `curl <device ip>:12345/`
- Status - Gets the current active call status:
  - `curl <device ip>:12345/status`
- Log - Get the recorded call log:
  - `curl <device ip>:12345/log`

The device IP should be visible on the main UI.

