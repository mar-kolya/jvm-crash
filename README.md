
Running `./gradlew run` makes jvm crash in various places. By the
looks of it it may be caused by memory write out of bounds somewhere.

Looks like having some allocations and
`-XX:FlightRecorderOptions=stackdepth=512` is important to make bug
more reproducible.

Tried with adobopenjdk 11.0.6 and 13.
