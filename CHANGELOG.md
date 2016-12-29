Change Log
==========

## Version 1.1.6

_2016-12-29_

 * Fix support Android 4.3 and below (issue with `onVisibilityChanged`).


## Version 1.1.5

_2016-12-29_

 * Changed min API level to 16 (Android 4.1).


## Version 1.1.4

_2016-12-28_

 * Fixed crash in case if view has initially visibility GONE.


## Version 1.1.3

_2016-12-28_

 * Snowflakes are reset when visibility changed to GONE.
 * Fixed Android Studio Preview rendering issue.


## Version 1.1.2

_2016-12-27_

 * Draw optimization.


## Version 1.1.1

_2016-12-26_

 * Support different start position on y-axis for snowflakes (attr `snowflakesAlreadyFalling`).
 * Rename attr `snowflakeFadingEnabled` to `snowflakesFadingEnabled`.
 * Fixed animation lags.
 * Fixed API 15 support.


## Version 1.1.0

_2016-12-25_

 * Support custom snowflake image (attr `snowflakeImage`).
 * Support custom snowflake angle (attr `snowflakeAngleMax`).
 * Support custom snowflake speed (attrs `snowflakeSpeedMin`, `snowflakeSpeedMax`).
 * All snowflakes are unique (size, speed, angle, alpha, start position).
 * Calculate size by using gaussian ("normally") distribution.


## Version 1.0.0

_2016-12-23_

Initial release.
