#!/bin/bash
rm -rf ./src/main/resources/webapp/index.html
rm -rf ./src/main/resources/webapp/static
cp -rfa /home/gangqiangsun/MYPROJECT/FRONTEND/licenseServer/dist/*  ./src/main/resources/webapp/
ls -l ./src/main/resources/webapp/
