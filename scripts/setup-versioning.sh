#!/bin/bash

# Accounting App - Automated Versioning Setup Script

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Accounting App - Automated Versioning Setup ===${NC}"
echo ""

# Check if we're in a git repository
if ! git rev-parse --git-dir > /dev/null 2>&1; then
    echo -e "${RED}Error: This must be run in a git repository${NC}"
    exit 1
fi

# Check if version.properties exists
if [ ! -f "$PROJECT_ROOT/version.properties" ]; then
    echo -e "${YELLOW}Creating version.properties file...${NC}"
    cat > "$PROJECT_ROOT/version.properties" << 'EOF'
# Version configuration for Accounting App
# This file is automatically updated by CI/CD pipeline

# Major version - increment for breaking changes
VERSION_MAJOR=1

# Minor version - increment for new features  
VERSION_MINOR=0

# Patch version - increment for bug fixes
VERSION_PATCH=0

# Build number - automatically incremented by CI
VERSION_BUILD=1
EOF
    echo -e "${GREEN}Created version.properties${NC}"
else
    echo -e "${GREEN}version.properties already exists${NC}"
fi

# Check if GitHub workflows exist
if [ ! -d "$PROJECT_ROOT/.github/workflows" ]; then
    echo -e "${RED}Warning: GitHub workflows directory not found${NC}"
    echo -e "${YELLOW}Please ensure .github/workflows/ directory exists with the versioning workflows${NC}"
else
    echo -e "${GREEN}GitHub workflows directory exists${NC}"
    
    # List workflow files
    if ls "$PROJECT_ROOT/.github/workflows"/*.yml > /dev/null 2>&1; then
        echo -e "${BLUE}Found workflow files:${NC}"
        for file in "$PROJECT_ROOT/.github/workflows"/*.yml; do
            echo "  - $(basename "$file")"
        done
    fi
fi

# Check if scripts directory exists and is executable
if [ ! -f "$PROJECT_ROOT/scripts/version.sh" ]; then
    echo -e "${RED}Warning: version.sh script not found${NC}"
else
    echo -e "${GREEN}version.sh script found${NC}"
    if [ ! -x "$PROJECT_ROOT/scripts/version.sh" ]; then
        echo -e "${YELLOW}Making version.sh executable...${NC}"
        chmod +x "$PROJECT_ROOT/scripts/version.sh"
        echo -e "${GREEN}Made version.sh executable${NC}"
    fi
fi

# Test build with current version
echo ""
echo -e "${BLUE}Testing build with current version...${NC}"
cd "$PROJECT_ROOT"

if ./gradlew assembleDebug > /dev/null 2>&1; then
    echo -e "${GREEN}Build successful!${NC}"
    
    # Show current version
    if [ -f "$PROJECT_ROOT/scripts/version.sh" ] && [ -x "$PROJECT_ROOT/scripts/version.sh" ]; then
        echo ""
        echo -e "${BLUE}Current version information:${NC}"
        "$PROJECT_ROOT/scripts/version.sh" show
    fi
else
    echo -e "${RED}Build failed! Please check your configuration.${NC}"
    exit 1
fi

echo ""
echo -e "${GREEN}=== Setup Complete! ===${NC}"
echo ""
echo -e "${BLUE}Next steps:${NC}"
echo "1. Commit the versioning setup files:"
echo "   ${YELLOW}git add version.properties .github/ scripts/ VERSIONING.md${NC}"
echo "   ${YELLOW}git commit -m \"feat: add automated versioning system\"${NC}"
echo ""
echo "2. Push to your repository:"
echo "   ${YELLOW}git push origin $(git branch --show-current)${NC}"
echo ""
echo "3. Create a pull request to main/master to trigger the first automated release"
echo ""
echo -e "${BLUE}Available commands:${NC}"
echo "  ${YELLOW}./scripts/version.sh show${NC}           - Show current version"
echo "  ${YELLOW}./scripts/version.sh patch${NC}          - Increment patch version"
echo "  ${YELLOW}./scripts/version.sh minor --commit${NC} - Increment minor version and commit"
echo "  ${YELLOW}./scripts/version.sh major --commit --tag${NC} - Increment major version, commit, and tag"
echo ""
echo -e "${BLUE}Documentation:${NC}"
echo "  Read ${YELLOW}VERSIONING.md${NC} for complete documentation"
echo ""
echo -e "${GREEN}Happy coding! 🚀${NC}"