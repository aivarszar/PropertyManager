#!/bin/bash

# Property Manager - Status Checker
# Pārbauda projekta statusu

# Krāsas
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📊 Property Manager - Projekta Status"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

# Git Status
echo -e "${BLUE}🔹 Git Status:${NC}"
if [[ -z $(git status -s) ]]; then
    echo -e "${GREEN}  ✅ Working tree clean${NC}"
else
    echo -e "${YELLOW}  ⚠️  Lokālās izmaiņas:${NC}"
    git status -s | sed 's/^/     /'
fi
echo ""

# Current Branch
echo -e "${BLUE}🔹 Current Branch:${NC}"
current_branch=$(git branch --show-current)
echo -e "  📍 ${GREEN}$current_branch${NC}"
echo ""

# Remote Sync Status
echo -e "${BLUE}🔹 Remote Sync:${NC}"
git fetch origin --quiet
local_commit=$(git rev-parse HEAD)
remote_commit=$(git rev-parse origin/$current_branch)

if [ "$local_commit" = "$remote_commit" ]; then
    echo -e "${GREEN}  ✅ Sinhronizēts ar remote${NC}"
else
    echo -e "${YELLOW}  ⚠️  Nav sinhronizēts ar remote${NC}"
    ahead=$(git rev-list --count origin/$current_branch..HEAD)
    behind=$(git rev-list --count HEAD..origin/$current_branch)
    if [ $ahead -gt 0 ]; then
        echo -e "  📤 $ahead commit(s) ahead of remote"
    fi
    if [ $behind -gt 0 ]; then
        echo -e "  📥 $behind commit(s) behind remote"
    fi
fi
echo ""

# Last 3 Commits
echo -e "${BLUE}🔹 Pēdējie 3 Commits:${NC}"
git log --oneline -3 | sed 's/^/  📝 /'
echo ""

# File Count
echo -e "${BLUE}🔹 Projekta Faili:${NC}"
kotlin_files=$(find app/src/main/java -name "*.kt" 2>/dev/null | wc -l)
echo "  📄 Kotlin faili: $kotlin_files"
screens=$(find app/src/main/java/com/propertymanager/ui/screens -name "*.kt" 2>/dev/null | wc -l)
echo "  🖼  UI Screens: $screens"
viewmodels=$(find app/src/main/java/com/propertymanager/ui/viewmodels -name "*.kt" 2>/dev/null | wc -l)
echo "  🧠 ViewModels: $viewmodels"
models=$(find app/src/main/java/com/propertymanager/models -name "*.kt" 2>/dev/null | wc -l)
echo "  📦 Models: $models"
echo ""

# Important Files
echo -e "${BLUE}🔹 Svarīgie Faili:${NC}"
if [ -f "app/google-services.json" ]; then
    echo -e "${GREEN}  ✅ google-services.json${NC}"
else
    echo -e "${RED}  ❌ google-services.json (NEPIECIEŠAMS!)${NC}"
fi

if [ -f "app/build.gradle.kts" ]; then
    echo -e "${GREEN}  ✅ app/build.gradle.kts${NC}"
else
    echo -e "${RED}  ❌ app/build.gradle.kts${NC}"
fi

if [ -f "gradlew" ]; then
    echo -e "${GREEN}  ✅ gradlew${NC}"
else
    echo -e "${RED}  ❌ gradlew${NC}"
fi

if [ -f ".idea/runConfigurations/app.xml" ]; then
    echo -e "${GREEN}  ✅ Run Configuration${NC}"
else
    echo -e "${YELLOW}  ⚠️  Run Configuration${NC}"
fi
echo ""

# Gradle Status
echo -e "${BLUE}🔹 Gradle:${NC}"
if [ -d ".gradle" ]; then
    echo -e "${GREEN}  ✅ Gradle cache exists${NC}"
else
    echo -e "${YELLOW}  ⚠️  Nav Gradle cache (run sync)${NC}"
fi
echo ""

# Build Status
if [ -d "app/build" ]; then
    echo -e "${GREEN}  ✅ Build directory exists${NC}"
    build_size=$(du -sh app/build 2>/dev/null | cut -f1)
    echo "  📦 Build size: $build_size"
else
    echo -e "${YELLOW}  ⚠️  Nav build directory (nav vēl build'ots)${NC}"
fi
echo ""

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo -e "${GREEN}✅ Status pārbaude pabeigta!${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
