#!/bin/bash

#######################################################################
# Keystore Generation Script for Android Build Variants
# Supports: release, internal
# This script generates keystores for different build variants
#######################################################################

set -e

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$SCRIPT_DIR"
KEYSTORE_DIR="$PROJECT_ROOT"

# Keystore configurations
declare -A KEYSTORES=(
    [release]="release.jks"
    [internal]="internal.jks"
)

declare -A STORE_PASSWORDS=(
    [release]="${RELEASE_KEYSTORE_PASSWORD:-your-release-password}"
    [internal]="${INTERNAL_KEYSTORE_PASSWORD:-your-internal-password}"
)

declare -A KEY_ALIASES=(
    [release]="${RELEASE_KEY_ALIAS:-release-key}"
    [internal]="${INTERNAL_KEY_ALIAS:-internal-key}"
)

declare -A KEY_PASSWORDS=(
    [release]="${RELEASE_KEY_PASSWORD:-your-release-key-password}"
    [internal]="${INTERNAL_KEY_PASSWORD:-your-internal-key-password}"
)

declare -A DNNAMES=(
    [release]="CN=CI Release, OU=Android, O=OneEngineer, L=Phnom Penh, C=KH"
    [internal]="CN=Development, OU=Android, O=OneEngineer, L=Phnom Penh, C=KH"
)

# Function to print messages
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

# Function to generate keystore
generate_keystore() {
    local variant=$1
    local keystore_name=${KEYSTORES[$variant]}
    local store_pass=${STORE_PASSWORDS[$variant]}
    local key_alias=${KEY_ALIASES[$variant]}
    local key_pass=${KEY_PASSWORDS[$variant]}
    local dname=${DNNAMES[$variant]}
    local keystore_path="$KEYSTORE_DIR/$keystore_name"

    print_info "Generating $variant keystore..."

    if [ -f "$keystore_path" ]; then
        print_warning "Keystore already exists at $keystore_path"
        read -p "Do you want to overwrite it? (y/n) " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            print_info "Skipping $variant keystore generation"
            return
        fi
    fi

    keytool -genkeypair \
        -keystore "$keystore_path" \
        -storepass "$store_pass" \
        -alias "$key_alias" \
        -keypass "$key_pass" \
        -keyalg RSA \
        -keysize 2048 \
        -validity 10000 \
        -dname "$dname" \
        -noprompt

    if [ $? -eq 0 ]; then
        print_info "✓ $variant keystore generated successfully at $keystore_path"
    else
        print_error "Failed to generate $variant keystore"
        exit 1
    fi
}

# Function to display keystore info
display_keystore_info() {
    local variant=$1
    local keystore_name=${KEYSTORES[$variant]}
    local keystore_path="$KEYSTORE_DIR/$keystore_name"

    if [ -f "$keystore_path" ]; then
        echo
        print_info "Keystore Information for $variant:"
        keytool -list -v -keystore "$keystore_path" -storepass "${STORE_PASSWORDS[$variant]}"
    else
        print_warning "Keystore not found at $keystore_path"
    fi
}

# Function to show usage
show_usage() {
    cat << EOF
Usage: $(basename "$0") [OPTION]

OPTIONS:
    all             Generate both release and internal keystores
    release         Generate only release keystore
    internal        Generate only internal keystore
    info            Display keystore information
    help            Show this help message

ENVIRONMENT VARIABLES:
    RELEASE_KEYSTORE_PASSWORD    Password for release keystore
    RELEASE_KEY_ALIAS            Key alias for release keystore
    RELEASE_KEY_PASSWORD         Key password for release keystore
    
    INTERNAL_KEYSTORE_PASSWORD   Password for internal keystore
    INTERNAL_KEY_ALIAS           Key alias for internal keystore
    INTERNAL_KEY_PASSWORD        Key password for internal keystore

EXAMPLE:
    # Generate all keystores
    ./generate-keystore.sh all
    
    # Generate release keystore with environment variables
    export RELEASE_KEYSTORE_PASSWORD="my-secure-password"
    export RELEASE_KEY_ALIAS="my-release-key"
    export RELEASE_KEY_PASSWORD="my-key-password"
    ./generate-keystore.sh release

EOF
}

# Main script
main() {
    local command=${1:-all}

    case $command in
        all)
            generate_keystore "release"
            generate_keystore "internal"
            print_info "All keystores generated successfully!"
            ;;
        release)
            generate_keystore "release"
            ;;
        internal)
            generate_keystore "internal"
            ;;
        info)
            display_keystore_info "release"
            display_keystore_info "internal"
            ;;
        help)
            show_usage
            ;;
        *)
            print_error "Unknown command: $command"
            show_usage
            exit 1
            ;;
    esac
}

# Run main function
main "$@"
