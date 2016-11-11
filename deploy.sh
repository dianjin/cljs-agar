#!/bin/bash
set -e
cd $(dirname $0)
lein with-profile uberjar do clean, cljsbuild once min
cd resources/public
git init
git add .
git commit -m "Deploy to GitHub Pages"
git push --force --quiet "git@github.com:timothypratley/sente_reagent_starter.git" master:gh-pages
rm -fr resources/public/.git
