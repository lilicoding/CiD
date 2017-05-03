#! /bin/sh

CURRENT_DIR=`pwd`

DIR=repos

KEYFILE=$CURRENT_DIR/joey.keystore
STORE_PASSWORD=qwe123
KEY_ALIAS=joey
KEY_PASSWORD=qwe123

for repo in `ls $CURRENT_DIR/$DIR | grep APICompatibility`;
do
    cd $DIR/$repo
    ./gradlew assembleRelease -Pandroid.injected.signing.store.file=$KEYFILE -Pandroid.injected.signing.store.password=$STORE_PASSWORD -Pandroid.injected.signing.key.alias=$KEY_ALIAS -Pandroid.injected.signing.key.password=$KEY_PASSWORD

    cd $CURRENT_DIR

    cp $DIR/$repo/app/build/outputs/apk/app-release.apk apks/$repo.apk
done
