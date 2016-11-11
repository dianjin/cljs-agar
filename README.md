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

Copyright © 2016 Timothy Pratley

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
