#!/bin/bash

set -e

### -----------------------------
###  PARAMETERS
### -----------------------------
ENV_FILE=".env"
OUTPUT_DIR="./.mongo-backup"

COLLECTIONS=(
  'users'
  'reviews'
  'product_versions'
)

QUERIES=(
  '{_id: {$in: ["user"]}}'
  ''
  ''
)




### -----------------------------
### LOAD .env FILE
### -----------------------------
MONGO_REMOTE_URI=""
MONGO_LOCAL_URI=""

if [[ ! -f "$ENV_FILE" ]]; then
  echo "ERROR: .env file not found: $ENV_FILE"
  exit 1
fi

# Export every line that is KEY=VALUE
export $(grep -v '^#' "$ENV_FILE" | xargs)

# Validate env file
if [[ -z "$MONGO_REMOTE_URI" ]]; then
  echo "ERROR: The .env file must define MONGO_REMOTE_URI"
  exit 1
fi

if [[ -z "$MONGO_LOCAL_URI" ]]; then
  echo "ERROR: The .env file must define MONGO_LOCAL_URI."
  exit 1
fi



### -----------------------------
###  SYNC
### -----------------------------
# Exportation
echo "Starting export…"

rm -rf "$OUTPUT_DIR"
mkdir -p "$OUTPUT_DIR"

for idx in "${!COLLECTIONS[@]}"; do
  COLLECTION="${COLLECTIONS[$idx]}"
  QUERY="${QUERIES[$idx]}"
  ARCHIVE_FILE="$OUTPUT_DIR/${COLLECTION}.archive"

  echo "[EXPORT] $COLLECTION → $ARCHIVE_FILE"

  if [[ -z "$QUERY" ]]; then
    # FULL COLLECTION BACKUP
    echo "  No query specified → exporting entire collection"
    mongo-archive \
      --uri "$MONGO_REMOTE_URI" \
      --collection "$COLLECTION" \
      --out "$ARCHIVE_FILE"
  else
    # QUERY BACKUP
    echo "  Using query: $QUERY"
    mongo-archive \
      --uri "$MONGO_REMOTE_URI" \
      --collection "$COLLECTION" \
      --query "$QUERY" \
      --out "$ARCHIVE_FILE"
  fi
done


# Importation
echo ""
echo "Starting import…"

for idx in "${!COLLECTIONS[@]}"; do
  COLLECTION="${COLLECTIONS[$idx]}"
  ARCHIVE_FILE="$OUTPUT_DIR/${COLLECTION}.archive"

  echo "[IMPORT] $ARCHIVE_FILE → $COLLECTION"
  mongo-urchive \
    --uri "$MONGO_LOCAL_URI" \
    --collection "$COLLECTION" \
    --in "$ARCHIVE_FILE"
  echo "[IMPORT DONE] $COLLECTION"
done


# Final message
echo ""
echo "Backup + Restore process completed."
