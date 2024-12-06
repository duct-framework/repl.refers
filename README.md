# Duct REPL Refers [![Build Status](https://github.com/duct-framework/repl.refers/actions/workflows/test.yml/badge.svg)](https://github.com/duct-framework/repl.refers/actions/workflows/test.yml)

[Integrant][] methods for adding functions to the `user` namespace.
Used by [Duct][] modules to add useful development functions to the
REPL.

[integrant]: https://github.com/weavejester/integrant
[duct]: https://github.com/duct-framework/duct

## Installation

Add the following dependency to your deps.edn file:

    org.duct-framework/repl.refers {:mvn/version "0.1.0"}

Or to your Leiningen project file:

    [org.duct-framework/repl.refers "0.1.0"]

## Usage

To use, add the `:duct.repl/refers` key to your configuration with a map
of aliases to fully-qualified symbols. For example:

```edn
{:duct.repl/refers {trim clojure.string/trim}}
```

When the configuration is initiated, `trim` will be added to the `user`
namespace. When the running system is halted, `trim` will be removed.

## License

Copyright © 2024 James Reeves

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
