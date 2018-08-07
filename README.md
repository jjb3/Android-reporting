# Android-reporting
An Android service that reports everything the device can know about the world (GPS, power, acceleration, etc.)

It reports at a regular interval to a url. Both interval and url can be set by the user.

Because this program constantly polls all available sensors, it rapidly consumes battery life. For comparison, a
typical phone or tablet might be drained in an hour.
