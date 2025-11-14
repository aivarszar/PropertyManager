#!/bin/bash

# Property Manager - Reset to Remote Script
# Šis skripts pārraksta visas lokālās izmaiņas ar remote versiju

echo "🔄 Sākam ielādēt projektu no Git..."
echo ""

# Krāsas output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Branch nosaukums
BRANCH="claude/property-manager-android-app-01SKf6hNvrFLNzsS4iA7mQRi"

# Pārbaudīt vai ir necommit'otas izmaiņas
if [[ -n $(git status -s) ]]; then
    echo -e "${YELLOW}⚠️  Tika atrastas lokālās izmaiņas:${NC}"
    git status -s
    echo ""
    read -p "Vai tiešām vēlaties DZĒST visas lokālās izmaiņas? (yes/no): " confirm
    if [ "$confirm" != "yes" ]; then
        echo -e "${RED}❌ Atcelts${NC}"
        exit 1
    fi
fi

echo "📥 Lejupielādēju jaunākās izmaiņas..."
git fetch origin || { echo -e "${RED}❌ Neizdevās fetch${NC}"; exit 1; }

echo "🗑️  Dzēšu lokālās izmaiņas..."
git reset --hard origin/$BRANCH || { echo -e "${RED}❌ Neizdevās reset${NC}"; exit 1; }

echo "🧹 Tīru untracked failus..."
git clean -fd

echo "✅ Pārslēdzos uz pareizo branch..."
git checkout $BRANCH

echo ""
echo -e "${GREEN}✅ Gatavs! Projekts ir atjaunots uz jaunāko versiju.${NC}"
echo ""
echo "📊 Status:"
git status
echo ""
echo "📝 Pēdējie commits:"
git log --oneline -3
