# Android Signing Configuration Setup

## Overview
This document describes how to set up and use signing configurations for release and internal builds.

## Keystores
Two keystores are maintained in the project root:
- `release.jks` - For production releases
- `internal.jks` - For internal/development builds

## Signing Configuration

### Environment Variables
The build system reads signing credentials from environment variables. Set them before building:

### Generate run script

```bash
chmod +x generate-keystore.sh
```

### Generate both keystores with environment variables

```bash
export RELEASE_KEYSTORE_PASSWORD="secure-password-123"
export RELEASE_KEY_ALIAS="release-key"
export RELEASE_KEY_PASSWORD="key-password-123"
./generate-keystore.sh all
```

**For Release Build:**
```bash
export RELEASE_KEYSTORE_PASSWORD="your-password"
export RELEASE_KEY_ALIAS="release-key"
export RELEASE_KEY_PASSWORD="your-key-password"
```

**For Internal Build:**
```bash
export INTERNAL_KEYSTORE_PASSWORD="your-password"
export INTERNAL_KEY_ALIAS="internal-key"
export INTERNAL_KEY_PASSWORD="your-key-password"
```

### Fallback Behavior
If environment variables are not set, the build will attempt to use:
1. Legacy env vars: `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`
2. Default values (will cause build to fail with keystore password error)

## Building Signed Releases

### Build Release Bundle
```bash
export RELEASE_KEYSTORE_PASSWORD="password123"
export RELEASE_KEY_ALIAS="release-key"
export RELEASE_KEY_PASSWORD="password123"
./gradlew :app:bundleRelease
```

Output: `app/build/outputs/bundle/release/app-release.aab`

### Build Release APK
```bash
export RELEASE_KEYSTORE_PASSWORD="password123"
export RELEASE_KEY_ALIAS="release-key"
export RELEASE_KEY_PASSWORD="password123"
./gradlew :app:assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

### Build Internal Bundle
```bash
export INTERNAL_KEYSTORE_PASSWORD="password123"
export INTERNAL_KEY_ALIAS="internal-key"
export INTERNAL_KEY_PASSWORD="password123"
./gradlew :app:bundleInternal
```

Output: `app/build/outputs/bundle/internal/app-internal.aab`

### Build Internal APK
```bash
export INTERNAL_KEYSTORE_PASSWORD="password123"
export INTERNAL_KEY_ALIAS="internal-key"
export INTERNAL_KEY_PASSWORD="password123"
./gradlew :app:assembleInternal
```

Output: `app/build/outputs/apk/internal/app-internal.apk`

## Keystore Generation

If you need to regenerate keystores:

```bash
export RELEASE_KEYSTORE_PASSWORD="secure-password"
export RELEASE_KEY_ALIAS="release-key"
export RELEASE_KEY_PASSWORD="secure-password"
export INTERNAL_KEYSTORE_PASSWORD="secure-password"
export INTERNAL_KEY_ALIAS="internal-key"
export INTERNAL_KEY_PASSWORD="secure-password"

./generate-keystore.sh all
```

Or individually:
```bash
./generate-keystore.sh release
./generate-keystore.sh internal
```

## Verifying Keystore Information

```bash
./generate-keystore.sh info
```

Or use keytool directly:
```bash
keytool -list -v -keystore release.jks -storepass "your-password"
keytool -list -v -keystore internal.jks -storepass "your-password"
```

## CI/CD Integration

For CI/CD pipelines (e.g., Codemagic), set the environment variables in your pipeline configuration:

**Codemagic (codemagic.yaml):**
```yaml
environment:
  vars:
    RELEASE_KEYSTORE_PASSWORD: $RELEASE_KEYSTORE_PASSWORD
    RELEASE_KEY_ALIAS: $RELEASE_KEY_ALIAS
    RELEASE_KEY_PASSWORD: $RELEASE_KEY_PASSWORD
```

## Build Variants

### Release Configuration
- Minification: **Enabled** (ProGuard/R8)
- Resource Shrinking: **Enabled**
- Debuggable: **No**
- Signing: Required

### Internal Configuration
- Minification: **Disabled**
- Resource Shrinking: **Disabled**
- Debuggable: **Yes**
- Signing: Required

## Troubleshooting

### "keystore password was incorrect"
- Verify the keystore file exists at project root
- Check that `RELEASE_KEYSTORE_PASSWORD` or `INTERNAL_KEYSTORE_PASSWORD` is correct
- Regenerate the keystore if necessary

### "Keystore file not found"
- Ensure keystores are in the project root directory
- Run `ls -la *.jks` to verify

### Key alias not found
- Verify the key alias matches what's in the keystore
- Use `./generate-keystore.sh info` to list available aliases

## Security Notes

⚠️ **Important:**
- Never commit keystore passwords in version control
- Store sensitive credentials in environment variables or CI/CD secrets
- Use strong passwords for production keystores
- Keep keystores backed up securely
- Rotate keystores periodically for security

## References
- [Android Signing Your App](https://developer.android.com/training/articles/sign-your-app)
- [Gradle Android Plugin - Signing](https://developer.android.com/studio/publish/app-signing)


# run build release app

# Remove old keystores
~~~rm -f release.jks internal.jks && \
# Set environment variables and generate keystores
export RELEASE_KEYSTORE_PASSWORD="password123" && \
export RELEASE_KEY_ALIAS="release-key" && \
export RELEASE_KEY_PASSWORD="password123" && \
export INTERNAL_KEYSTORE_PASSWORD="password123" && \
export INTERNAL_KEY_ALIAS="internal-key" && \
export INTERNAL_KEY_PASSWORD="password123" && \
./generate-keystore.sh all~~~

## build app
export RELEASE_KEYSTORE_PASSWORD="password123" && \
export RELEASE_KEY_ALIAS="release-key" && \
export RELEASE_KEY_PASSWORD="password123" && \
./gradlew :app:bundleRelease 2>&1 | tail -30