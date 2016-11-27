## About

This is an approximation of the agar.io game implemented in Clojure and Clojurescript with [Sente](https://github.com/ptaoussanis/sente) for websockets and [Reagent](https://github.com/reagent-project/reagent) for HTML/JS.

This is a fork of [timothypratley](https://github.com/timothypratley/)'s [Snakelake](https://github.com/timothypratley/snakelake) repo. Highly recommend checking it out for a Sente, Reagent, and Figwheel example complete with a Heroku deployment script!

## Demo

[https://dianjin.github.io/cljs-agar/](https://dianjin.github.io/cljs-agar/)

If you find bugs feel free to open an issue and/or submit a PR!

## Development

Start server: `lein run`

Start client: `lein figwheel`

Interactive development at [localhost:3000](http://localhost:3000/)

Clean all compiled files: `lein clean`

Production build: `lein with-profile uberjar do clean, cljsbuild once min`

## Deploy

Deploy server: `git push heroku master`

Deploy static assets: `./deploy.sh`

## License

Copyright © 2016 Timothy Pratley, Dian Jin

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
