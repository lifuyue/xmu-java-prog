#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
LAB_DIR="$ROOT_DIR/LAB8"

usage() {
    echo "用法: bash LAB8/run.sh <题号> [console]"
    echo "可用题号: 1 2 3 5"
    echo "示例: bash LAB8/run.sh 1"
    echo "第 2 题控制台版: bash LAB8/run.sh 2 console"
}

if [[ $# -lt 1 ]]; then
    usage
    exit 1
fi

case "$1" in
    1) QUESTION="1"; MAIN_CLASS="LoginFrameApp" ;;
    2) QUESTION="2"; MAIN_CLASS="Main" ;;
    3) QUESTION="3"; MAIN_CLASS="EventDemoApp" ;;
    5) QUESTION="5"; MAIN_CLASS="Main" ;;
    *)
        usage
        exit 1
        ;;
esac

SRC_DIR="$LAB_DIR/$QUESTION/src"
BIN_DIR="$LAB_DIR/$QUESTION/bin"

mkdir -p "$BIN_DIR"
javac -d "$BIN_DIR" "$SRC_DIR"/*.java

if [[ "$QUESTION" == "2" && "${2:-}" == "console" ]]; then
    exec java -cp "$BIN_DIR" "$MAIN_CLASS" console
else
    exec java -cp "$BIN_DIR" "$MAIN_CLASS"
fi
