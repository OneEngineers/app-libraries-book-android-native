#!/bin/bash
set -e

KEYSTORE=release.jks

keytool -genkeypair \
  -keystore $KEYSTORE \
  -storepass "$KEYSTORE_PASSWORD" \
  -alias "$KEY_ALIAS" \
  -keypass "$KEY_PASSWORD" \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000 \
  -dname "CN=CI Release, OU=Android, O=OneEngineer, L=Phnom Penh, C=KH"

echo "Keystore generated"
