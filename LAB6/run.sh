#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
LAB_DIR="$ROOT_DIR/LAB6"

usage() {
    echo "用法: bash LAB6/run.sh <题号>"
    echo "可用题号: 1 2 3 4 5"
    echo "示例: bash LAB6/run.sh 5"
}

if [[ $# -lt 1 ]]; then
    usage
    exit 1
fi

case "$1" in
    1|2|3|4|5)
        QUESTION="$1"
        ;;
    *)
        usage
        exit 1
        ;;
esac

SRC_DIR="$LAB_DIR/$QUESTION/src"
BIN_DIR="$LAB_DIR/$QUESTION/bin"

mkdir -p "$BIN_DIR"
javac -d "$BIN_DIR" "$SRC_DIR"/*.java
java -cp "$BIN_DIR" Main
