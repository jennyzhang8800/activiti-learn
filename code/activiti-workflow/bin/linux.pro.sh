#!/usr/bin/env bash

cd ../
git pull
cd bin/
./package.sh
cd ../target/
java -Xms2048m -Xmx2048m -Duser.timezone=GMT+8 -jar work-flow.jar --spring.profiles.active=pro --server.port=8080
