#!/bin/bash

set -e

INSTALL_DIR="$HOME/.treetrunk"
LOCAL_JAR_SRC="$(dirname "$0")/../cli/build/libs/treetrunk-cli-all.jar"
JAR_PATH="$INSTALL_DIR/treetrunk-cli.jar"
LAUNCHER_PATH="$INSTALL_DIR/treetrunk"

echo "Installing TreeTrunk CLI to: $INSTALL_DIR"

# Create install dir
mkdir -p "$INSTALL_DIR"

# Verify Jar exists
if [ ! -f "$LOCAL_JAR_SRC" ]; then
  echo "Local jar not found at: $LOCAL_JAR_SRC"
  echo "Make sure you run './gradlew :cli:shadowJar' first"
  exit 1
fi

# Copy the jar
cp "$LOCAL_JAR_SRC" "$JAR_PATH"
echo "Copied jar to: $JAR_PATH"

# Write launcher script
cat > "$LAUNCHER_PATH" <<EOF
#!/bin/bash
java -jar "\$(dirname "\$0")/treetrunk-cli.jar" "\$@"
EOF

chmod +x "$LAUNCHER_PATH"
echo "Created Launcher at: $LAUNCHER_PATH"

# Verify path
if [[ ":$PATH" != *":$INSTALL_DIR" ]]; then
  echo ""
  echo "Please add '$INSTALL_DIR' to PATH"
else
  echo "'$INSTALL_DIR' is in your PATH - and ready to use"
fi

echo ""
echo "TreeTrunk installed!"