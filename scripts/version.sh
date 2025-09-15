#!/bin/bash

# Accounting App Version Management Script
# Usage: ./scripts/version.sh [major|minor|patch] [--commit]

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
VERSION_FILE="$PROJECT_ROOT/version.properties"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Help function
show_help() {
    echo "Accounting App Version Management Script"
    echo ""
    echo "Usage: $0 [COMMAND] [OPTIONS]"
    echo ""
    echo "Commands:"
    echo "  major          Increment major version (x.0.0)"
    echo "  minor          Increment minor version (x.x.0)"
    echo "  patch          Increment patch version (x.x.x)"
    echo "  show           Show current version"
    echo "  help           Show this help message"
    echo ""
    echo "Options:"
    echo "  --commit       Commit the version changes to git"
    echo "  --tag          Create a git tag for the new version"
    echo ""
    echo "Examples:"
    echo "  $0 patch --commit --tag    # Increment patch and commit with tag"
    echo "  $0 minor --commit          # Increment minor and commit"
    echo "  $0 show                    # Show current version"
}

# Function to read current version
read_version() {
    if [ ! -f "$VERSION_FILE" ]; then
        echo -e "${RED}Error: version.properties file not found at $VERSION_FILE${NC}"
        exit 1
    fi
    
    source "$VERSION_FILE"
    echo "$VERSION_MAJOR.$VERSION_MINOR.$VERSION_PATCH"
}

# Function to read current version components
read_version_components() {
    if [ ! -f "$VERSION_FILE" ]; then
        echo -e "${RED}Error: version.properties file not found at $VERSION_FILE${NC}"
        exit 1
    fi
    
    source "$VERSION_FILE"
    MAJOR=$VERSION_MAJOR
    MINOR=$VERSION_MINOR
    PATCH=$VERSION_PATCH
    BUILD=$VERSION_BUILD
}

# Function to write new version
write_version() {
    local major=$1
    local minor=$2
    local patch=$3
    local build=$4
    
    cat > "$VERSION_FILE" << EOF
# Version configuration for Accounting App
# This file is automatically updated by CI/CD pipeline

# Major version - increment for breaking changes
VERSION_MAJOR=$major

# Minor version - increment for new features  
VERSION_MINOR=$minor

# Patch version - increment for bug fixes
VERSION_PATCH=$patch

# Build number - automatically incremented by CI
VERSION_BUILD=$build
EOF
    
    echo -e "${GREEN}Updated version to $major.$minor.$patch (build $build)${NC}"
}

# Function to commit changes
commit_changes() {
    local version=$1
    
    if ! git diff --quiet "$VERSION_FILE"; then
        echo -e "${BLUE}Committing version update...${NC}"
        git add "$VERSION_FILE"
        git commit -m "chore: bump version to $version"
        echo -e "${GREEN}Committed version update${NC}"
    else
        echo -e "${YELLOW}No changes to commit${NC}"
    fi
}

# Function to create tag
create_tag() {
    local version=$1
    
    echo -e "${BLUE}Creating git tag v$version...${NC}"
    git tag -a "v$version" -m "Release version $version"
    echo -e "${GREEN}Created tag v$version${NC}"
    echo -e "${YELLOW}Run 'git push origin v$version' to push the tag to remote${NC}"
}

# Function to show current version
show_version() {
    read_version_components
    local current_version="$MAJOR.$MINOR.$PATCH"
    local version_code=$((MAJOR * 10000 + MINOR * 1000 + PATCH * 100 + BUILD))
    
    echo -e "${BLUE}Current Version Information:${NC}"
    echo "  Version: $current_version"
    echo "  Version Code: $version_code"
    echo "  Components: Major=$MAJOR, Minor=$MINOR, Patch=$PATCH, Build=$BUILD"
}

# Main script logic
COMMAND=${1:-help}
COMMIT_FLAG=false
TAG_FLAG=false

# Parse options
shift || true
while [[ $# -gt 0 ]]; do
    case $1 in
        --commit)
            COMMIT_FLAG=true
            shift
            ;;
        --tag)
            TAG_FLAG=true
            shift
            ;;
        *)
            echo -e "${RED}Unknown option: $1${NC}"
            show_help
            exit 1
            ;;
    esac
done

case $COMMAND in
    major)
        read_version_components
        NEW_MAJOR=$((MAJOR + 1))
        NEW_MINOR=0
        NEW_PATCH=0
        NEW_BUILD=1
        NEW_VERSION="$NEW_MAJOR.$NEW_MINOR.$NEW_PATCH"
        
        echo -e "${YELLOW}Incrementing major version: $MAJOR.$MINOR.$PATCH -> $NEW_VERSION${NC}"
        write_version $NEW_MAJOR $NEW_MINOR $NEW_PATCH $NEW_BUILD
        
        if [ "$COMMIT_FLAG" = true ]; then
            commit_changes $NEW_VERSION
        fi
        
        if [ "$TAG_FLAG" = true ]; then
            create_tag $NEW_VERSION
        fi
        ;;
        
    minor)
        read_version_components
        NEW_MAJOR=$MAJOR
        NEW_MINOR=$((MINOR + 1))
        NEW_PATCH=0
        NEW_BUILD=$((BUILD + 1))
        NEW_VERSION="$NEW_MAJOR.$NEW_MINOR.$NEW_PATCH"
        
        echo -e "${YELLOW}Incrementing minor version: $MAJOR.$MINOR.$PATCH -> $NEW_VERSION${NC}"
        write_version $NEW_MAJOR $NEW_MINOR $NEW_PATCH $NEW_BUILD
        
        if [ "$COMMIT_FLAG" = true ]; then
            commit_changes $NEW_VERSION
        fi
        
        if [ "$TAG_FLAG" = true ]; then
            create_tag $NEW_VERSION
        fi
        ;;
        
    patch)
        read_version_components
        NEW_MAJOR=$MAJOR
        NEW_MINOR=$MINOR
        NEW_PATCH=$((PATCH + 1))
        NEW_BUILD=$((BUILD + 1))
        NEW_VERSION="$NEW_MAJOR.$NEW_MINOR.$NEW_PATCH"
        
        echo -e "${YELLOW}Incrementing patch version: $MAJOR.$MINOR.$PATCH -> $NEW_VERSION${NC}"
        write_version $NEW_MAJOR $NEW_MINOR $NEW_PATCH $NEW_BUILD
        
        if [ "$COMMIT_FLAG" = true ]; then
            commit_changes $NEW_VERSION
        fi
        
        if [ "$TAG_FLAG" = true ]; then
            create_tag $NEW_VERSION
        fi
        ;;
        
    show)
        show_version
        ;;
        
    help|--help|-h)
        show_help
        ;;
        
    *)
        echo -e "${RED}Unknown command: $COMMAND${NC}"
        show_help
        exit 1
        ;;
esac

# Test build after version change
if [[ "$COMMAND" == "major" || "$COMMAND" == "minor" || "$COMMAND" == "patch" ]]; then
    echo -e "${BLUE}Testing build with new version...${NC}"
    cd "$PROJECT_ROOT"
    if ./gradlew assembleDebug > /dev/null 2>&1; then
        echo -e "${GREEN}Build successful!${NC}"
    else
        echo -e "${RED}Build failed! Please check the configuration.${NC}"
        exit 1
    fi
fi