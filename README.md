# Library System Application Native 

chmod +x /home/reaksa/Documents/OneEgineer/app-libraries-book-android-native/generate-keystore.sh


# Generate both keystores with environment variables
export RELEASE_KEYSTORE_PASSWORD="secure-password-123"
export RELEASE_KEY_ALIAS="release-key"
export RELEASE_KEY_PASSWORD="key-password-123"
./generate-keystore.sh all